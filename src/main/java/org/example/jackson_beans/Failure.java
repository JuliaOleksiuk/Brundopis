package org.example.jackson_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Failure {
    @JsonProperty("message")
    private String message;

    @JsonProperty("CDATA")
    private String details;

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Failure{" +
                "message='" + message + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}