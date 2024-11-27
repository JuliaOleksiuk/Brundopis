package org.example;

import java.io.InputStream;

public class ResourceUtil {
    public static InputStream readXMLFileFromResources(String fileName) {
        ClassLoader classLoader = ResourceUtil.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found in resources: " + fileName);
        }

        return inputStream;
    }
}