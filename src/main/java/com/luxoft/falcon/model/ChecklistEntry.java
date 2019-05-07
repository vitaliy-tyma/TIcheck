package com.luxoft.falcon.model;

import lombok.Getter;
import lombok.Setter;


public class ChecklistEntry {
    public ChecklistEntry(String nameOfErrorToCheckFor) {
        this.nameOfErrorToCheckFor = nameOfErrorToCheckFor;
    }

    public ChecklistEntry(String nameOfErrorToCheckFor,
                          Boolean stepIsChecked,
                          Boolean resultOfCheckIsNOK,
                          String fullQuery,
                          String fullNameOfPon) {
        this.nameOfErrorToCheckFor = nameOfErrorToCheckFor;
        this.stepIsChecked = stepIsChecked;
        this.resultOfCheckIsNOK = resultOfCheckIsNOK;
        this.fullQuery = fullQuery;
        this.fullNameOfPon = fullNameOfPon;
    }



    @Getter
    private String nameOfErrorToCheckFor;
    @Getter
    @Setter
    private Boolean stepIsChecked = Boolean.FALSE; //Set after step has been checked
    @Getter
    @Setter
    private Boolean resultOfCheckIsNOK = Boolean.FALSE; // Set TRUE in case of error founded

    @Getter
    @Setter
    private String resultOfCheckText; // For BIRT (in case of SKIPPED TEST)

    @Getter
    @Setter
    private String fullQuery = null;

    @Getter
    @Setter
    private String fullNameOfPon = null;


    @Getter
    @Setter
    private String isRegression = "Not analysed";


}
