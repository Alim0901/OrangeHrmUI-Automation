package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DataReader {

    private static Properties prop;

    public static String get(String key) {
        if (prop == null) {
            prop = new Properties();
            try {
                prop.load(new FileInputStream("src/test/resources/data/testdata.properties"));
            } catch (IOException e) {
                throw new RuntimeException("Could not load testdata.properties", e);
            }
        }
        return prop.getProperty(key);
    }
}
