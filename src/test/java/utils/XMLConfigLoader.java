package utils;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XMLConfigLoader {

    private static final Map<String, String> configMap = new HashMap<>();

    static {
        try {
            File configFile = new File("src/test/resources/config/config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document doc = builder.parse(configFile);
            doc.getDocumentElement().normalize();

            NodeList childNodes = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node instanceof Element element) {
                    String key = element.getTagName();
                    String value = element.getTextContent().trim();
                    configMap.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.xml", e);
        }
    }

    public static String get(String key) {
        if (!configMap.containsKey(key)) {
            throw new RuntimeException("Tag not found in config.xml: " + key);
        }
        return configMap.get(key);
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
