package org.example;

import org.example.bean.ImportedTestExecutionBean;
import org.example.bean.ImportedTestExecutionStatus;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class FastestStaxReader {

    public static List<ImportedTestExecutionBean> parseFile(String filePath) throws Exception {
        List<ImportedTestExecutionBean> testExecutions = new ArrayList<>();

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filePath));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "testcase".equals(reader.getLocalName())) {
                ImportedTestExecutionBean testCase = processTestCase(reader);
                if (testCase != null) {
                    testExecutions.add(testCase);
                }
            }
        }

        reader.close();
        return testExecutions;
    }

    private static ImportedTestExecutionBean processTestCase(XMLStreamReader reader) throws Exception {
        ImportedTestExecutionBean testCase = new ImportedTestExecutionBean();
        testCase.setName(reader.getAttributeValue(null, "name"));
        testCase.setClassName(reader.getAttributeValue(null, "classname"));

        boolean skipTestCase = false;

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String elementName = reader.getLocalName();
                switch (elementName) {
                    case "property":
                        skipTestCase = processTestCaseProperty(reader, testCase) || skipTestCase;
                        break;
                    case "failure":
                    case "error":
                        processFailureOrError(reader, testCase);
                        break;
                    case "skipped":
                        skipTestCase = true;
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT && "testcase".equals(reader.getLocalName())) {
                break;
            }
        }

        if (skipTestCase) {
            return null; // Skip the test case if flagged
        }

        determineStatus(testCase);
        return testCase;
    }

    private static boolean processTestCaseProperty(XMLStreamReader reader, ImportedTestExecutionBean testCase) {
        String propertyName = reader.getAttributeValue(null, "name");
        String propertyValue = reader.getAttributeValue(null, "value");

        if ("Jira".equals(propertyName)) {
            testCase.setJiraIssueKey(propertyValue);
        }
        return "skip".equals(propertyName);
    }

    private static void processFailureOrError(XMLStreamReader reader, ImportedTestExecutionBean testCase) throws Exception {
        String message = reader.getAttributeValue(null, "message");
        StringBuilder details = new StringBuilder();

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.CHARACTERS) {
                details.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT &&
                    ("failure".equals(reader.getLocalName()) || "error".equals(reader.getLocalName()))) {
                break;
            }
        }

        testCase.setStatus(ImportedTestExecutionStatus.FAILED);
        testCase.setComment(details.length() > 0 ? details.toString().trim() : message);
    }

    private static void determineStatus(ImportedTestExecutionBean testCase) {
        if (testCase.getStatus() == null) {
            testCase.setStatus(ImportedTestExecutionStatus.PASSED);
        }
    }


    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();

            String filePath = "src/main/resources/big-junit-results.xml";
            List<ImportedTestExecutionBean> executions = parseFile(filePath);
            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
