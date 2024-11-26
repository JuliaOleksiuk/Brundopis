package org.example;

import org.example.bean.ImportedTestExecutionBean;
import org.example.bean.ImportedTestExecutionStatus;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SAXReader {

    public List<ImportedTestExecutionBean> readFile(File file) throws IOException, JDOMException {
        List<ImportedTestExecutionBean> testExecutions = new ArrayList<>();

        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(file);
        Element rootElement = document.getRootElement();

        List<Element> testSuites = rootElement.getChildren("testsuite");
        for (Element testSuite : testSuites) {
            List<Element> testCases = testSuite.getChildren("testcase");
            for (Element testCase : testCases) {
                ImportedTestExecutionBean testExecution = processTestCase(testCase);
                testExecutions.add(testExecution);
            }
        }
        return testExecutions;
    }

    private ImportedTestExecutionBean processTestCase(Element testCase) {
        String name = testCase.getAttributeValue("name");
        String className = testCase.getAttributeValue("classname");
        String jiraIssueKey = extractJiraKey(testCase);
        ImportedTestExecutionStatus status = determineStatus(testCase);
        String comment = extractComment(testCase);

        return new ImportedTestExecutionBean(name, className, status, jiraIssueKey, comment);
    }

    private ImportedTestExecutionStatus determineStatus(Element testCase) {
        if (testCase.getChild("failure") != null || testCase.getChild("error") != null) {
            return ImportedTestExecutionStatus.FAILED;
        }
        return ImportedTestExecutionStatus.PASSED;
    }

    private String extractJiraKey(Element testCase) {
        Element propertiesElement = testCase.getChild("properties");
        if (propertiesElement != null) {
            List<Element> properties = propertiesElement.getChildren("property");
            for (Element property : properties) {
                if ("Jira".equals(property.getAttributeValue("name"))) {
                    return property.getAttributeValue("value");
                }
            }
        }
        return null;
    }

    private String extractComment(Element testCase) {
        Element failureElement = testCase.getChild("failure");
        Element errorElement = testCase.getChild("error");

        Element relevantElement = failureElement != null ? failureElement : errorElement;
        if (relevantElement != null) {
            String stackTrace = relevantElement.getTextTrim();
            String message = relevantElement.getAttributeValue("message");
            return stackTrace != null && !stackTrace.isBlank() ? stackTrace : message;
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();

            SAXReader reader = new SAXReader();
            File file = new File("src/main/resources/big-junit-results.xml");
            List<ImportedTestExecutionBean> testExecutions = reader.readFile(file);
            long endTime = System.currentTimeMillis();

            System.out.println("Parsing completed in " + (endTime - startTime) + "ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
