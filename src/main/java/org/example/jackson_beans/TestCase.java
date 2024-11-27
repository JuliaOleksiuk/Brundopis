package org.example.jackson_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCase {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String classname;

    @JacksonXmlProperty(isAttribute = true)
    private double time;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "properties")
    private List<Property> properties;

    @JacksonXmlProperty(localName = "failure")
    private Failure failure;

    @JacksonXmlProperty(localName = "skipped")
    private Skipped skipped;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
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
}

