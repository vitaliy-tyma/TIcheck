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



    public Checklist() {
//        spiderSteps.add("NndbTmcNamesBuilderTarget:118");
//        spiderSteps.add("NndbCountriesBuilderTarget:227");
//
//        birtSteps.add("anaconda.inds.ti.TmcLocationTableIdTest.locationTableIdCountTest");
//        birtSteps.add("anaconda.inds.ti.TmcLocationExporterTest.tmcNameTableTest");
//        birtSteps.add("anaconda.inds.ti.TmcLocationNameTest.locationNameCountTest");
//        birtSteps.add("anaconda.inds.ti.TmcLocationNameTest.nndbTmcNamesExportTest");
//        birtSteps.add("anaconda.inds.ti.TmcStringTableTest.stringCountTest");
//        birtSteps.add("anaconda.inds.ti.TmcStringTableTest.tmcStringTest");
//
//        /* !!!!!!!!!!!!!!UNDER CONSTRUCTION!!!!!!!!!!!!!!!!!!!!*/
//        ndsSteps.add("tmcLocationTableIdTable");
        /*
         * 0MXXXX.NDS //One file must be processed!!!!
         * NHXXXX.NDS //Many files must be processed!!!!
         * */
        //        ndsSteps.add("TI version tmcLocationTableIdTable"); //Find out what versions are current!!!







        /* !!!!!!!!!!!!!!NOT IMPLEMENTED!!!!!!!!!!!!!!!!!!!!
         * Filled Local_tmc_location on Route links
         * TI Browser - TI icons and messages are present for TMC and TPEG_TEC
         * */
    }


//    @Getter
//    private String checklistName;
//    //TODO Clarify usage of report.checklistName - seems that it is not in use
//    public void setChecklistName(String checklistName) {
//        if (checklistName == null) {
//            this.checklistName = MainConfig.getCHECKLISTS_NAME_UNDEF();
//        }
//    }


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