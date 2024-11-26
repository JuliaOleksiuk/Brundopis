package org.example;
import org.example.bean.ImportedTestExecutionBean;
import org.example.bean.ImportedTestExecutionStatus;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SAXReader {

    private static final String TEST_SUITES = "testsuites";

    private static final String TEST_SUITE = "testsuite";

    private static final String TEST_CASE = "testcase";

    private static final String FAILURE = "failure";

    private static final String ERROR = "error";

    private SAXReader() {
    }

    public static List<ImportedTestExecutionBean> parseXML(InputStream file) throws
            ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();

        Element root = document.getDocumentElement();
        List<ImportedTestExecutionBean> importedTestExecutionBeans = new ArrayList<>();
        if (TEST_SUITES.equals(root.getNodeName())) {
            parseTestSuitesElement(root, importedTestExecutionBeans);
        } else if (TEST_SUITE.equals(root.getNodeName())) {
            parseTestSuite(root, importedTestExecutionBeans);
        }

        System.out.println("JUnit XML processing completed successfully.");
        return importedTestExecutionBeans;
    }

    private static void parseTestSuitesElement(Element thisTestSuitesElement,
                                               List<ImportedTestExecutionBean> importedTestExecutionBeans) {
        NodeList testSuites = thisTestSuitesElement.getElementsByTagName(TEST_SUITE);

        for (int i = 0; i < testSuites.getLength(); i++) {
            if (testSuites.item(i).getParentNode() == thisTestSuitesElement) {
                parseTestSuite((Element) testSuites.item(i), importedTestExecutionBeans);
            }
        }
    }

    private static void parseTestSuite(Element thisTestSuite, List<ImportedTestExecutionBean> importedTestExecutionBeans)
    {
        NodeList testCases = thisTestSuite.getElementsByTagName(TEST_CASE);
        NodeList testSuites = thisTestSuite.getElementsByTagName(TEST_SUITE);

        for (int i = 0; i < testCases.getLength(); i++) {
            if (testCases.item(i).getParentNode() == thisTestSuite) {
                parseTestCase((Element) testCases.item(i), importedTestExecutionBeans);
            }
        }

        for (int i = 0; i < testSuites.getLength(); i++) {
            if (testSuites.item(i).getParentNode() == thisTestSuite) {
                parseTestSuite((Element) testCases.item(i), importedTestExecutionBeans);
            }
        }
    }

    private static void parseTestCase(Element thisTestCase, List<ImportedTestExecutionBean> importedTestExecutionBeans)
    {
        ImportedTestExecutionBean importedTestExecutionBean = new ImportedTestExecutionBean(
                thisTestCase.getAttribute("name"),
                thisTestCase.getAttribute("classname"),
                determineStatus(thisTestCase),
                getIssueKey(thisTestCase),
                determineComment(thisTestCase));
        importedTestExecutionBeans.add(importedTestExecutionBean);
    }

    private static ImportedTestExecutionStatus determineStatus(Element testCase) {
        NodeList failures = testCase.getElementsByTagName(FAILURE);
        NodeList errors = testCase.getElementsByTagName(ERROR);

        if (failures.getLength() > 0 || errors.getLength() > 0) {
            return ImportedTestExecutionStatus.FAILED;
        }

        return ImportedTestExecutionStatus.PASSED;
    }

    private static String determineComment(Element testCase) {
        NodeList failures = testCase.getElementsByTagName(FAILURE);
        NodeList errors = testCase.getElementsByTagName(ERROR);

        return getTextContentOrAttribute(failures)
                .or(() -> getTextContentOrAttribute(errors))
                .orElse("");
    }

    private static Optional<String> getTextContentOrAttribute(NodeList nodes) {
        if(nodes.getLength() == 0) {
            return Optional.empty();
        }

        Element element = (Element) nodes.item(0);
        if (!Objects.equals(element.getTextContent(), "")) {
            return Optional.of(element.getTextContent());
        }

        return Optional.of(element.getAttribute("message"));
    }

    private static String getIssueKey(Element thisTestCase) {
        NodeList properties = thisTestCase.getElementsByTagName("properties");

        for (int i = 0; i < properties.getLength(); i++) {
            if (properties.item(i).getParentNode() == thisTestCase) {
                String foundIssueKey = findKeyValueInProperty(properties.item(i));
                if (!foundIssueKey.isBlank()) {
                    return foundIssueKey;
                }
            }
        }

        return "";
    }

    private static String findKeyValueInProperty(Node property) {
        Element propertyElement = (Element) property;
        String issueKey = propertyElement.getAttribute("issue-key");
        String ISSUE_KEY_REGEX = "\\b[A-Z][A-Z0-9]+-\\d+\\b";
        Pattern pattern = Pattern.compile(ISSUE_KEY_REGEX);
        Matcher matcher = pattern.matcher(issueKey);
        if(matcher.find()){
            return matcher.group();
        } else if(!issueKey.isBlank()){
            return issueKey;
        }

        return "";
    }

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();

            InputStream file = Main.class.getClassLoader().getResourceAsStream("big-junit-results.xml");

            if (file == null) {
                System.err.println("File not found in resources: junit-result.xml");
                return;
            }

            // Call the parseXML method
            List<ImportedTestExecutionBean> results = parseXML(file);

            // Print the results
            results.forEach(result -> System.out.println(result.toString()));

            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + "ms");

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

}