package org.example.jackson_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestSuite {
    @JsonProperty("name")
    private String name;

    @JsonProperty("time")
    private double time;

    @JsonProperty("tests")
    private int tests;

    @JsonProperty("errors")
    private int errors;

    @JsonProperty("skipped")
    private int skipped;

    @JsonProperty("failures")
    private int failures;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("testcase")
    private List<TestCase> testCases = new ArrayList<>();

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("testsuite")
    private List<TestSuite> subTestSuites = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getTests() {
        return tests;
    }

    public void setTests(int tests) {
        this.tests = tests;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public List<TestSuite> getSubTestSuites() {
        return subTestSuites;
    }

    public void setSubTestSuites(List<TestSuite> subTestSuites) {
        this.subTestSuites = subTestSuites;
    }
}