//package org.example;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
//
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//class TestSuite {
//    @JsonProperty("name")
//    private String name;
//
//    @JsonProperty("time")
//    private double time;
//
//    @JsonProperty("tests")
//    private int tests;
//
//    @JsonProperty("errors")
//    private int errors;
//
//    @JsonProperty("skipped")
//    private int skipped;
//
//    @JsonProperty("failures")
//    private int failures;
//
//    @JacksonXmlElementWrapper(useWrapping = false)
//    @JsonProperty("testcase")
//    private List<TestCase> testCases = new ArrayList<>();
//
//    @JacksonXmlElementWrapper(useWrapping = false)
//    @JsonProperty("testsuite")
//    private List<TestSuite> subTestSuites = new ArrayList<>();
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public double getTime() {
//        return time;
//    }
//
//    public void setTime(double time) {
//        this.time = time;
//    }
//
//    public int getTests() {
//        return tests;
//    }
//
//    public void setTests(int tests) {
//        this.tests = tests;
//    }
//
//    public int getErrors() {
//        return errors;
//    }
//
//    public void setErrors(int errors) {
//        this.errors = errors;
//    }
//
//    public int getSkipped() {
//        return skipped;
//    }
//
//    public void setSkipped(int skipped) {
//        this.skipped = skipped;
//    }
//
//    public int getFailures() {
//        return failures;
//    }
//
//    public void setFailures(int failures) {
//        this.failures = failures;
//    }
//
//    public List<TestCase> getTestCases() {
//        return testCases;
//    }
//
//    public void setTestCases(List<TestCase> testCases) {
//        this.testCases = testCases;
//    }
//
//    public List<TestSuite> getSubTestSuites() {
//        return subTestSuites;
//    }
//
//    public void setSubTestSuites(List<TestSuite> subTestSuites) {
//        this.subTestSuites = subTestSuites;
//    }
//}
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//class TestSuites {
//
//    @JacksonXmlElementWrapper(useWrapping = false)
//    @JsonProperty("testsuite")
//    private List<TestSuite> testSuites = new ArrayList<>();
//
//    public List<TestSuite> getTestSuites() {
//        return testSuites;
//    }
//
//    public void setTestSuites(List<TestSuite> testSuites) {
//        this.testSuites = testSuites;
//    }
//}
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//class TestCase {
//    @JsonProperty("name")
//    private String name;
//
//    @JsonProperty("classname")
//    private String classname;
//
//    @JsonProperty("time")
//    private double time;
//
//    @JsonProperty("failure")
//    private Failure failure;
//
//    @JsonProperty("error")
//    private Error error;
//
//    @JsonProperty("skipped")
//    private Skipped skipped;
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getClassname() {
//        return classname;
//    }
//
//    public void setClassname(String classname) {
//        this.classname = classname;
//    }
//
//    public double getTime() {
//        return time;
//    }
//
//    public void setTime(double time) {
//        this.time = time;
//    }
//
//    public Failure getFailure() {
//        return failure;
//    }
//
//    public void setFailure(Failure failure) {
//        this.failure = failure;
//    }
//
//    public Error getError() {
//        return error;
//    }
//
//    public void setError(Error error) {
//        this.error = error;
//    }
//
//    public Skipped getSkipped() {
//        return skipped;
//    }
//
//    public void setSkipped(Skipped skipped) {
//        this.skipped = skipped;
//    }
//}
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//class Failure {
//    @JsonProperty("type")
//    private String type;
//
//    @JsonProperty("content")
//    private String content; // For CDATA
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//}
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//class Error {
//    @JsonProperty("type")
//    private String type;
//
//    @JsonProperty("message")
//    private String message;
//
//    @JsonProperty("content")
//    private String content; // For CDATA
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//}
//
//@JsonIgnoreProperties(ignoreUnknown = true)
//class Skipped {
//    @JsonProperty("message")
//    private String message;
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//}
//
//public class XMLReader {
//    public static void main(String[] args) {
//        ClassLoader classLoader = XMLReader.class.getClassLoader();
//        InputStream inputStream = classLoader.getResourceAsStream("withoutsuites.xml");
//
//        if (inputStream == null) {
//            throw new IllegalArgumentException("File not found in resources: withoutsuites.xml");
//        }
//
//        try {
//            XmlMapper xmlMapper = new XmlMapper();
//            String xmlContent = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A").next();
//            JsonNode rootNode = xmlMapper.readTree(xmlContent);
//
//            List<TestSuite> testSuites = new ArrayList<>();
//            if (rootNode.has("testcase")) {
//                TestSuite testSuite = xmlMapper.readValue(xmlContent, TestSuite.class);
//                testSuites.add(testSuite);
//            } else {
//                TestSuites result = xmlMapper.readValue(xmlContent, TestSuites.class);
//                testSuites = result.getTestSuites();
//            }
//            System.out.println("Root Test Suite: ");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
