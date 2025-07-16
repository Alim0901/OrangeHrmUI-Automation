package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DataReader {

    private static final Properties prop = new Properties();
    private static final String DATA_FILE_PATH = "src/test/resources/data/testdata.properties";

    static {
        try (FileInputStream fis = new FileInputStream(DATA_FILE_PATH)) {
            prop.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load " + DATA_FILE_PATH, e);
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }
}
