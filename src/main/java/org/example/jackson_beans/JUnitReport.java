package org.example.jackson_beans;

import java.util.ArrayList;
import java.util.List;

public class JUnitReport {

    private List<TestSuite> testSuites = new ArrayList<>();

    public List<TestSuite> getTestSuites() {
        return testSuites;
    }

    public void setTestSuites(List<TestSuite> testSuites) {
        this.testSuites = testSuites;
    }
}