package com.luxoft.falcon.model;

import lombok.Getter;
import lombok.Setter;


public class ChecklistEntry {
    public ChecklistEntry(String nameOfErrorToCheckFor) {
        this.nameOfErrorToCheckFor = nameOfErrorToCheckFor;
    }


    @Getter
    private String nameOfErrorToCheckFor;
    @Getter
    @Setter
    private Boolean stepIsChecked = Boolean.FALSE; //Set after step has been checked
    @Getter
    @Setter
    private Boolean resultOfCheckIsNOK = Boolean.FALSE; // Set in case of founded error

    @Getter
    @Setter
    private String fullQuery = null;

    @Getter
    @Setter
    private String fullNameOfPon = null;
//    public String getFullNameOfPon(){
//        if (this.fullNameOfPon == null) {
//            return "";
//        }
//        return this.fullNameOfPon;
//    }


    @Setter
    private String isRegression = null;
    public String getIsRegression(){
        if (this.isRegression == null) {
            return "Not analysed";
        }
        return this.isRegression;
    }




}
