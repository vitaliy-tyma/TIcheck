package com.luxoft.falcon.model;

import lombok.Setter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Objects;

/** Entity/Data/POJO to be extracted from Spider
 * Composed with SpiderErrorClass to keep the list of errors */
@Slf4j
public class SpiderDataClass {
    private String run = null;
    private String market = null;
    private String project = null;
    private String iteration = null;
    private ArrayList<SpiderErrorClass> errorsList;
    private final String NO_ERRORS = " No Errors have been extracted";

    @Setter @Getter
    private String jdbcError = null;

    @Setter @Getter
    private String query = null;

    public SpiderDataClass() {
    }

    public SpiderDataClass(String run, String market, String project, String iteration) {
        log.debug("********** Run SpiderDataClass constructor **********");
        this.run = run;
        this.market = market;
        this.project = project;
        this.iteration = iteration;
    }

    public void setErrorsList(ArrayList<SpiderErrorClass> errorsList) {
        this.errorsList = new ArrayList<SpiderErrorClass>();
        this.errorsList = errorsList;
    }

    public ArrayList<SpiderErrorClass> getErrorsList() {
        return errorsList;
    }

    @Override
    public String toString() {
        return ("Request is" +
                " " + run +
                " " + market +
                " " + project +
                " " + iteration +
                ": " +
                /** EXAMPLE OF TERNARY OPERATOR - TWO CONSEQUENT!!!! */
                (jdbcError == null ?
                        (errorsList != null && !errorsList.isEmpty() ? errorsList.toString() : NO_ERRORS) :
                        getJdbcError())/** Set error message as output*/
                + "");
    }

    public String getHtml(String delimiter) {
        /** Output for HTML */
        StringBuilder result = new StringBuilder();
        result.append(
                "Request is <b>" + run +
                        " " + market +
                        " " + project +
                        " " + iteration +
                        "</b>: " + delimiter + "\n");

        /** SQL request in Spoiler*/
        result.append(
                "<details>\n" +
                        " <summary>" +
                        "<u>Open to see Query (or use <i>Ctrl+U</i>)</u>" +
                        "</summary>\n" +
                        "<i><b><font color = green>" + query + "</font></b></i>" +
                        "</details>" + "\n");

        result.append(
                "<div style=\"display:none;\">" +
                        query +
                        "</div>" + "\n");

        if (getJdbcError() == null) {
            if (errorsList != null && !errorsList.isEmpty()) {
                for (SpiderErrorClass sError : errorsList) {
                    result.append(sError.getHtml("<br/>" + "\n"));
                }
            } else {
                result.append(NO_ERRORS);
            }
        } else {
            /** Set error message as output*/
            result.append(getJdbcError());
        }
        return result.toString();
    }


    /** Works only with limited set of fields */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpiderDataClass that = (SpiderDataClass) o;
        return run.equals(that.run) &&
                iteration.equals(that.iteration) &&
                Objects.equals(errorsList, that.errorsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(run, iteration);
    }
}

