package com.example.surya2;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by Surya_Bera on 1/29/2018.
 */
public class Utils {
    private static final String[] FILE_HEADER_MAPPING = {"Address"};
    private static Logger LOGGER = LoggerFactory.getLogger(Utils.class);


    public static List<String> getAddressList(String fileName) throws Exception {
        FileReader fileReader = null;
        CSVParser csvFileParser = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
        fileReader = new FileReader(fileName);
        csvFileParser = new CSVParser(fileReader, csvFileFormat);
        List<CSVRecord> csvRecords = csvFileParser.getRecords();
        List<String> addressList = new ArrayList<String>();
        for (CSVRecord csvRecord : csvRecords) {
            if (!"Address".equalsIgnoreCase(csvRecord.get("Address"))) {
                addressList.add(csvRecord.get("Address"));
            }
        }
        return addressList;
    }

    public static void createCsv(Map<String, String> addressLatLngMap) throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("E:\\cts-new\\Mapping-Migration\\Ambigious-Result\\output\\PopulatelatLngComparison-10000.csv"));

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("Address", "LatLng (MQ/GM)", "Match"));


        for (Map.Entry<String, String> entry : addressLatLngMap.entrySet()) {
            String address = entry.getKey();
            String mapValue = entry.getValue();
            String latLng = mapValue.split("=")[0];
            String isLatLngMatch = mapValue.split("=")[1];
            csvPrinter.printRecord(address, latLng, isLatLngMatch);
        }
        csvPrinter.flush();
    }


    public static void createCsvNew(Map<String, String> addressLatLngMap, String outputFile) throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile));

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("Address", "LatLng (MQ)", "LatLng (GM)", "GM Found", "StoreId (MQ)", "StoreId (MSA)", "StoreMatch" ));


        for (Map.Entry<String, String> entry : addressLatLngMap.entrySet()) {
            String address = entry.getKey();
            String mapValue = entry.getValue();
            String mqLatLng = mapValue.split("=")[0];
            String gmLatLng = mapValue.split("=")[1];
            String gmLatLngFound = mapValue.split("=")[2];
            String storeIdMQ = mapValue.split("=")[3];
            String storeIdMSA = mapValue.split("=")[4];
            String storeIdMatch = mapValue.split("=")[5];
            csvPrinter.printRecord(address, mqLatLng, gmLatLng, gmLatLngFound, storeIdMQ, storeIdMSA, storeIdMatch);
        }
        csvPrinter.flush();
    }

    public static void createStoreListCsv(List<String> storeList, String outputFile ) throws Exception {
           BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile));
           String header = "Addr|Zip|Country|City|Territory|StoreId|Result_Message";
           CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(header ));


           for (String str : storeList) {
               csvPrinter.printRecord(str);
           }
           csvPrinter.flush();
    }

    public static int getMyPropertyAsInt(String value) {
        Long tmp = new Long(-1);
        try {
            tmp = new Long(value);
        }  catch  ( Exception e ) {
            LOGGER.error("could not convert property to int " + value);
        }

        return tmp.intValue();
    }

    public static boolean getMyPropertyAsBoolean(String value) {
            boolean retVal = false;
            try {
                Long tmp = new Long(value);
                if ( tmp.intValue() == 1) {
                    retVal = true;
                }
            }  catch  ( Exception e ) {
                LOGGER.error("could not convert property to boolean " + value);
            }

            return retVal;

        }
}

