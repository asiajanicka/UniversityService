package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestUtils {

    private static final String TEST_RESULTS_PATH = "src/test/java/org/example/testResults/";

    private static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
        LocalDateTime timeNow = LocalDateTime.now();
        return formatter.format(timeNow);
    }

    public static String getNameForXMLFile(String name) {
        return String.format("%s%s_%s.xml", TEST_RESULTS_PATH, name, getCurrentDateTime());
    }
}