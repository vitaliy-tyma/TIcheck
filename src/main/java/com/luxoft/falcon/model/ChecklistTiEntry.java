package com.luxoft.falcon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ChecklistTiEntry {
    @Getter
    private String nameOfError;
    @Getter
    @Setter
    private Boolean stepIsChecked; //Set after step has been checked
    @Getter
    @Setter
    private Boolean resultOfCheckIsNOK; // Set in case of founded error

    @Getter
    @Setter
    private String fullQuery;
}
