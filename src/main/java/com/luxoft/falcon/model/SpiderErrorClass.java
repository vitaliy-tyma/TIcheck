package com.luxoft.falcon.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/** Spider Error Class (POJO) - contains fields requested/queried from Spider DB
 * It is used only within SpiderDataClass by means of composition */
@Getter @Setter
public class SpiderErrorClass {
    private String error = null;
    private String errorJavaClass = null; /** Fields is Character 70 (not null) - contains white spaces after the value!!*/
    private String errorExtracted = null; //May be deleted
    private String jiraTicket = null;
    private String count = null;
    private String task = null;
    private String description = null;
//    private String branchName = null;
//    private String projectName = null;

    @Override
    public String toString() {
        return ("Task='" + task + '\'' +
                "; Error='" + error + '\'' +
                "; ErrorJavaClass='" + errorJavaClass + '\'' +
                "; ErrorExtracted='" + errorExtracted + '\'' +
                "; Count='" + count + '\'' +
//                "; BranchName='" + branchName + '\'' +
//                "; ProjectName='" + projectName + '\'' +
                "; JiraTicket='" + jiraTicket + '\'');
    }

    public String getHtml(String delimiter) {
        return ("<p>"+
                "<b>Error</b>='" + error + '\'' + ";" + delimiter +
                "<b>ErrorJavaClass</b>='" + errorJavaClass + '\'' + ";" + delimiter +
                "<b>ErrorExtracted</b>='" + errorExtracted + '\'' + ";" + delimiter +

                "<b>JiraTicket</b>='" + jiraTicket + '\'' + delimiter +
                "<b>Count</b>='" + count + '\'' + ";" + delimiter +
                "<b>Task</b>='" + task + '\'' + ";" + delimiter +
                "<b>Description</b>='" + description + '\'' + ";" + delimiter +
//                "<b>BranchName</b>='" + branchName + '\'' + ";" + delimiter +
//                "<b>ProjectName</b>='" + projectName + '\'' + ";" + delimiter +
                "</p>" + delimiter );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpiderErrorClass that = (SpiderErrorClass) o;
        return Objects.equals(error, that.error) &&
//                Objects.equals(errorJavaClass, that.errorJavaClass) &&
//                Objects.equals(errorExtracted, that.errorExtracted) &&
                Objects.equals(jiraTicket, that.jiraTicket) &&
                Objects.equals(count, that.count) &&
                Objects.equals(task, that.task);
//                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, errorJavaClass, errorExtracted, jiraTicket, count, task, description);
    }
}
