package org.example.jackson_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestSuite {
    @JsonProperty("name")
    private String name;

    @JsonProperty("tests")
    private int tests;

    @JsonProperty("failures")
    private int failures;

    @JsonProperty("errors")
    private int errors;

    @JsonProperty("skipped")
    private int skipped;

    @JsonProperty("time")
    private String time;

    @JsonProperty("testcase")
    private List<TestCase> testCases;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTests() {
        return tests;
    }

    public void setTests(int tests) {
        this.tests = tests;
    }

    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    @Override
    public String toString() {
        return "Test Suite{" +
                "name='" + name + '\'' +
                ", tests=" + tests +
                ", failures=" + failures +
                ", errors=" + errors +
                ", skipped=" + skipped +
                ", time='" + time + '\'' +
                ", testCases=" + testCases +
                '}' + "\n";
    }
}