package org.example;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.jackson_beans.TestSuites;

import java.io.File;

public class JacksonXMLReader {

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();

            String filePath = "src/main/resources/big-junit-results.xml";
            File xmlFile = new File(filePath);

            XmlMapper xmlMapper = new XmlMapper();
            TestSuites testSuites = xmlMapper.readValue(xmlFile, TestSuites.class);

            long endTime = System.currentTimeMillis();
            System.out.println("Parsing completed in " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
