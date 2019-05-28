package com.luxoft.falcon.model;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;



/* Class contains steps of checklists, loaded from file with the same name from resources folder */
@Data
public class Checklist {


    private List<String> spiderSteps = new LinkedList<>();
    private List<String> birtSteps = new LinkedList<>();
    private List<String> ndsSteps = new LinkedList<>();

    /* NOT USED YET ?*/
    private String nameOfChecklist = "";
    private String fileName = "";
    private boolean defaultFlag = false;


/* !!!!!!!!!!!!!!UNDER CONSTRUCTION!!!!!!!!!!!!!!!!!!!!*/
//TODO Clarify usage of report.checklistName - seems that it is not in use
// ndsSteps.add("tmcLocationTableIdTable");
// 0MXXXX.NDS //One file must be processed!!!!
// NHXXXX.NDS //Many files must be processed!!!!

//TODO
// ndsSteps.add("TI version tmcLocationTableIdTable");
// Find out what versions are current!!!

// DONE
// Task https://jira.harman.com/jira/browse/NAV2010ANA-23009 !!!!!
// Is incorporated to anaconda.nds.ti.TmcLocationExporterTest.tmcNameTableTest

//FIXME - WAY OF PROCESSING IS NOT DEFINED - VISUAL TASK TO BE DEVELOPED !!!!!!!!!!!!!!!!!!!!
// Filled Local_tmc_location on Route links
// TI Browser - TI icons and messages are present for TMC and TPEG_TEC


    public Checklist() {
    }



    public void addSpiderSteps(String string){
        this.spiderSteps.add(string);
    }
    public void addBirtSteps(String string){
        this.birtSteps.add(string);
    }
    public void addNdsSteps(String string){
        this.ndsSteps.add(string);
    }


    public void clear(){
        spiderSteps.clear();
        birtSteps.clear();
        ndsSteps.clear();
    }
}