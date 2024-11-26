package org.example.bean;

public class ImportedTestExecutionBean {
    private String name;
    private String className;
    private ImportedTestExecutionStatus status;
    private String jiraIssueKey;
    private String comment;

    public ImportedTestExecutionBean() {
    }

    public ImportedTestExecutionBean(String name, String className, ImportedTestExecutionStatus status, String jiraIssueKey, String comment) {
        this.name = name;
        this.className = className;
        this.status = status;
        this.jiraIssueKey = jiraIssueKey;
        this.comment = comment;
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

    public ImportedTestExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ImportedTestExecutionStatus status) {
        this.status = status;
    }

    public String getJiraIssueKey() {
        return jiraIssueKey;
    }

    public void setJiraIssueKey(String jiraIssueKey) {
        this.jiraIssueKey = jiraIssueKey;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ImportedTestExecutionBean{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", status=" + status +
                ", jiraIssueKey='" + jiraIssueKey + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
