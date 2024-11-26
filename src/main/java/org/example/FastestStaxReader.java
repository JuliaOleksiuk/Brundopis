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

    public static List<ImportedTestExecutionBean> parseTestExecutions(String filePath) throws Exception {
        List<ImportedTestExecutionBean> testExecutions = new ArrayList<>();
        ImportedTestExecutionBean currentBean = null;
        String currentElement = "";
        boolean skipTestCase = false;

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filePath));

        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    currentElement = reader.getLocalName();
                    if ("testcase".equals(currentElement)) {
                        currentBean = new ImportedTestExecutionBean();
                        skipTestCase = false;
                        processTestCaseAttributes(reader, currentBean);
                    } else if ("property".equals(currentElement) && currentBean != null) {
                        skipTestCase = processTestCaseProperties(reader, currentBean) || skipTestCase;
                    } else if (("failure".equals(currentElement) || "error".equals(currentElement)) && currentBean != null) {
                        processFailureOrError(reader, currentBean);
                    } else if ("skipped".equals(currentElement)) {
                        skipTestCase = true;
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    if ("testcase".equals(reader.getLocalName()) && currentBean != null) {
                        if (!skipTestCase) {
                            determineStatus(currentBean);
                            testExecutions.add(currentBean);
                        }
                        currentBean = null;
                    }
                    currentElement = "";
                    break;
            }
        }

        reader.close();
        return testExecutions;
    }

    private static void processTestCaseAttributes(XMLStreamReader reader, ImportedTestExecutionBean bean) {
        bean.setName(reader.getAttributeValue(null, "name"));
        bean.setClassName(reader.getAttributeValue(null, "classname"));
    }

    private static boolean processTestCaseProperties(XMLStreamReader reader, ImportedTestExecutionBean bean) {
        String propertyName = reader.getAttributeValue(null, "name");
        String propertyValue = reader.getAttributeValue(null, "value");

        if ("Jira".equals(propertyName)) {
            bean.setJiraIssueKey(propertyValue);
        }
        return "skip".equals(propertyName);
    }

    private static void processFailureOrError(XMLStreamReader reader, ImportedTestExecutionBean bean) throws Exception {
        String message = reader.getAttributeValue(null, "message");
        StringBuilder stackTrace = new StringBuilder();

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.CHARACTERS) {
                stackTrace.append(reader.getText().trim());
            } else if (event == XMLStreamConstants.END_ELEMENT &&
                    ("failure".equals(reader.getLocalName()) || "error".equals(reader.getLocalName()))) {
                break;
            }
        }

        bean.setStatus(ImportedTestExecutionStatus.FAILED);
        bean.setComment(!stackTrace.isEmpty() ? stackTrace.toString() : message);
    }

    private static void determineStatus(ImportedTestExecutionBean bean) {
        if (bean.getStatus() == null) {
            bean.setStatus(ImportedTestExecutionStatus.PASSED);
        }
    }

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();

            String filePath = "src/main/resources/big-junit-results.xml";
            List<ImportedTestExecutionBean> executions = parseTestExecutions(filePath);
            executions.forEach(System.out::println);

            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
