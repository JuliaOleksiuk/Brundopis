package org.example.jackson_beans;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCase {
    @JsonProperty("name")
    private String name;

    @JsonProperty("classname")
    private String className;

    @JsonProperty("time")
    private String time;

    @JsonProperty("failure")
    private Failure failure;

    @JsonProperty("skipped")
    private Skipped skipped;

    @JsonProperty("properties")
    private Properties properties;

    private String status;

    public TestCase() {
        determineStatus();
    }

    void determineStatus() {
        if (failure != null) {
            status = "FAILED";
        } else if (skipped != null) {
            status = "SKIPPED";
        } else {
            status = "PASSED";
        }
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Failure getFailure() {
        return failure;
    }

    public void setFailure(Failure failure) {
        this.failure = failure;
    }

    public Skipped getSkipped() {
        return skipped;
    }

    public void setSkipped(Skipped skipped) {
        this.skipped = skipped;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", time='" + time + '\'' +
                ", failure=" + failure +
                ", skipped='" + skipped + '\'' +
                ", properties=" + properties +
                ", status='" + getStatus() + '\'' +
                '}' + "\n";
    }
}