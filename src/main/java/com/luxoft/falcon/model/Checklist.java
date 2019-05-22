package com.luxoft.falcon.model;
import lombok.Data;
import java.util.LinkedList;
import java.util.List;

//FIXME - main log
// Make tests
// Make eager initialization
// Make AJAX for PON name detection and result displaing
// Make Api for other apps
// Make analyse.jsp as main page (with form as in index.jsp and result data - displayed with AJAX)
// Make NDS part - open DB?
// MAKE AWB part - visual?

/* Class contains steps of checklists, loaded from file with the same name from resources folder */
@Data
public class Checklist {

    private List<String> spiderSteps = new LinkedList<>();
    private List<String> birtSteps = new LinkedList<>();
    private List<String> ndsSteps = new LinkedList<>();


//TODO Clarify usage of report.checklistName - seems that it is not in use
//        /* !!!!!!!!!!!!!!UNDER CONSTRUCTION!!!!!!!!!!!!!!!!!!!!*/
//        ndsSteps.add("tmcLocationTableIdTable");

//FIXME
// find out with https://jira.harman.com/jira/browse/NAV2010ANA-23009 !!!!!

    /*
     * 0MXXXX.NDS //One file must be processed!!!!
     * NHXXXX.NDS //Many files must be processed!!!!
     * */
//        ndsSteps.add("TI version tmcLocationTableIdTable"); //Find out what versions are current!!!

    /* !!!!!!!!!!!!!!NOT IMPLEMENTED!!!!!!!!!!!!!!!!!!!!
     * Filled Local_tmc_location on Route links
     * TI Browser - TI icons and messages are present for TMC and TPEG_TEC
     * */

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
}