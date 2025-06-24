package utils;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLConfigLoader {

    private static Document document;

    static {
        try {
            File configFile = new File("src/test/resources/config/config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            document = builder.parse(configFile);
            document.getDocumentElement().normalize();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.xml", e);
        }
    }

    public static String get(String tagName) {
        NodeList nodeList = document.getElementsByTagName(tagName);
        if (nodeList.getLength() == 0) {
            throw new RuntimeException("Tag not found in config.xml: " + tagName);
        }
        return nodeList.item(0).getTextContent();
    }

    public static boolean getBoolean(String tagName) {
        return Boolean.parseBoolean(get(tagName));
    }

    public static int getInt(String tagName) {
        return Integer.parseInt(get(tagName));
    }
}
