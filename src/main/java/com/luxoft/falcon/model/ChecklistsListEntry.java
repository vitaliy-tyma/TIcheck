package com.luxoft.falcon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ChecklistsListEntry {

    @Getter @Setter
    private String nameOfChecklist = "";

    @Getter @Setter
    private String simpleFileNameOfChecklist = "";

    @Getter @Setter
    private String fileNameOfChecklist = "";

    @Getter @Setter
    private Boolean defaultFlag = false;

}
