package org.example;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.bean.ImportedTestExecutionBean;
import org.example.bean.ImportedTestExecutionStatus;
import org.example.jackson_beans.JUnitReport;
import org.example.jackson_beans.Property;
import org.example.jackson_beans.TestCase;
import org.example.jackson_beans.TestSuite;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JacksonXMLReader {

    public static void main(String[] args) {
        try {

            long start = System.currentTimeMillis();
            ClassLoader classLoader = JacksonXMLReader.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("testSuites.xml");

            if (inputStream == null) {
                throw new IllegalArgumentException("File not found in resources: junit-report.xml");
            }

            XmlMapper xmlMapper = new XmlMapper();
            JUnitReport report = xmlMapper.readValue(inputStream, JUnitReport.class);

            List<ImportedTestExecutionBean> importedTestExecutionBeans = mapJUnitReportToBeans(report);
            long end = System.currentTimeMillis();

            System.out.println("Report: " + report.getTestSuites().size() + " test suites");
            System.out.println("Time taken: " + (end - start) + "ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<ImportedTestExecutionBean> mapJUnitReportToBeans(JUnitReport jUnitReport){
        List<ImportedTestExecutionBean> importedTestExecutionBeans = new ArrayList<>();
        for (TestSuite testSuite : jUnitReport.getTestSuites()) {
            for (TestCase testCase : testSuite.getTestCases()) {
                ImportedTestExecutionStatus status = determineStatus(testCase);

                String jiraIssueKey = null;
                if(testCase.getProperties() != null) {
                    for (Property property : testCase.getProperties()) {
                        if ("Jira".equals(property.getName())) {
                            jiraIssueKey = property.getValue();
                            break;
                        }
                    }
                }

                ImportedTestExecutionBean bean = new ImportedTestExecutionBean(
                        testCase.getName(),
                        testCase.getClassname(),
                        status,
                        jiraIssueKey,
                        testCase.getFailure() != null ? testCase.getFailure().getMessage() : null
                );
                importedTestExecutionBeans.add(bean);
            }
        }
        return importedTestExecutionBeans;
    }

    private static ImportedTestExecutionStatus determineStatus(TestCase testCase) {
        if (testCase.getFailure() != null) {
            return ImportedTestExecutionStatus.FAILED;
        } else if (testCase.getSkipped() != null) {
            return ImportedTestExecutionStatus.SKIPPED;
        } else {
            return ImportedTestExecutionStatus.PASSED;
        }
    }
}
