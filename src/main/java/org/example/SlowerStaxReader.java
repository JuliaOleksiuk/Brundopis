package org.example;

import org.example.bean.ImportedTestExecutionBean;
import org.example.bean.ImportedTestExecutionStatus;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SlowerStaxReader {

    public static List<ImportedTestExecutionBean> parseTestExecutions(String filePath) throws Exception {
        List<ImportedTestExecutionBean> testExecutions = new ArrayList<>();
        ImportedTestExecutionBean currentBean = null;
        boolean skipTestCase = false;

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(new FileInputStream(filePath));

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String elementName = startElement.getName().getLocalPart();

                switch (elementName) {
                    case "testcase":
                        currentBean = new ImportedTestExecutionBean();
                        skipTestCase = false;
                        processTestCaseAttributes(startElement, currentBean);
                        break;
                    case "property":
                        if (currentBean != null) {
                            skipTestCase = processTestCaseProperties(startElement, currentBean) || skipTestCase;
                        }
                        break;
                    case "failure":
                    case "error":
                        if (currentBean != null) {
                            processFailureOrError(startElement, eventReader, currentBean);
                        }
                        break;
                    case "skipped":
                        skipTestCase = true;
                        break;
                }
            } else if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                String elementName = endElement.getName().getLocalPart();

                if ("testcase".equals(elementName) && currentBean != null) {
                    if (!skipTestCase) {
                        determineStatus(currentBean);
                        testExecutions.add(currentBean);
                    }
                    currentBean = null;
                }
            }
        }

        eventReader.close();
        return testExecutions;
    }

    private static void processTestCaseAttributes(StartElement startElement, ImportedTestExecutionBean bean) {
        Iterator<Attribute> attributes = startElement.getAttributes();
        while (attributes.hasNext()) {
            Attribute attribute = attributes.next();
            switch (attribute.getName().getLocalPart()) {
                case "name":
                    bean.setName(attribute.getValue());
                    break;
                case "classname":
                    bean.setClassName(attribute.getValue());
                    break;
            }
        }
    }

    private static boolean processTestCaseProperties(StartElement startElement, ImportedTestExecutionBean bean) {
        Iterator<Attribute> attributes = startElement.getAttributes();
        String propertyName = null;
        while (attributes.hasNext()) {
            Attribute attribute = attributes.next();
            if ("name".equals(attribute.getName().getLocalPart())) {
                propertyName = attribute.getValue();
            }
            if ("value".equals(attribute.getName().getLocalPart()) && "Jira".equals(propertyName)) {
                bean.setJiraIssueKey(attribute.getValue());
            }
        }
        // If there's a "skip" property, mark this test case to be skipped
        return "skip".equals(propertyName);
    }

    private static void processFailureOrError(StartElement startElement, XMLEventReader eventReader, ImportedTestExecutionBean bean) throws Exception {
        Iterator<Attribute> attributes = startElement.getAttributes();
        String message = null;
        while (attributes.hasNext()) {
            Attribute attribute = attributes.next();
            if ("message".equals(attribute.getName().getLocalPart())) {
                message = attribute.getValue();
            }
        }

        String stacktrace = "";
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.peek();
            if (event.isCharacters()) {
                stacktrace += event.asCharacters().getData();
                eventReader.nextEvent(); // Consume the event
            } else if (event.isEndElement() &&
                    ("failure".equals(event.asEndElement().getName().getLocalPart()) ||
                            "error".equals(event.asEndElement().getName().getLocalPart()))) {
                break;
            } else {
                eventReader.nextEvent(); // Consume the event
            }
        }

        bean.setStatus(ImportedTestExecutionStatus.FAILED);
        bean.setComment(stacktrace.isEmpty() ? message : stacktrace.trim());
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
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
