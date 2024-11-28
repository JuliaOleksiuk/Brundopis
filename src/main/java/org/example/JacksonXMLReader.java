package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.bean.ImportedTestExecutionBean;
import org.example.bean.ImportedTestExecutionStatus;
import org.example.jackson_beans.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JacksonXMLReader {

    public static void main(String[] args) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("example.xml");

            if (inputStream == null) {
                throw new IllegalArgumentException("File not found in resources: withoutsuites.xml");
            }

            XmlMapper xmlMapper = new XmlMapper();

            String xmlContent = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A").next();
            JsonNode rootNode = xmlMapper.readTree(xmlContent);

            JUnitReport jUnitReport = new JUnitReport();
            if (rootNode.has("testcase")) {
                TestSuite testSuite = xmlMapper.readValue(xmlContent, TestSuite.class);
                jUnitReport.getTestSuites().add(testSuite);
            } else {
                TestSuites result = xmlMapper.readValue(xmlContent, TestSuites.class);
                jUnitReport.setTestSuites(result.getTestSuites());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
