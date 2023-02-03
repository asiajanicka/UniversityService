package utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtils {

    private static final String TEST_RESULTS_PATH = "src/test/java/org/example/testResults/";

    private static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
        LocalDateTime timeNow = LocalDateTime.now();
        return formatter.format(timeNow);
    }

    public static String getNameForJsonFile(String name) {
        return String.format("%s%s_%s.json", TEST_RESULTS_PATH, name, getCurrentDateTime());
    }
}