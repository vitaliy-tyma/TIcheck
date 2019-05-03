package com.luxoft.falcon.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Entity/Data/POJO to connect to the SPIDER */
@Data
public class ChecklistTI {

    /* Structure: 1) Error to be checked. 2) step has passed. 3) result of check is NOK (field = true)*/
//    private Map<String, Boolean> spiderSteps = new HashMap<>();
//    private Map<String, Boolean> birtSteps = new HashMap<>();
//    private Map<String, Boolean> ndsSteps = new HashMap<>();

    private ArrayList<ChecklistTiEntry> spiderSteps = new ArrayList<>();
    private ArrayList<ChecklistTiEntry> birtSteps = new ArrayList<>();
    private ArrayList<ChecklistTiEntry> ndsSteps = new ArrayList<>();

    public ChecklistTI(){
//        spiderSteps.put("NndbTmcNamesBuilderTarget:118", Boolean.FALSE);
//        spiderSteps.put("NndbCountriesBuilderTarget:227", Boolean.FALSE);
        spiderSteps.add(new ChecklistTiEntry("NndbTmcNamesBuilderTarget:118", Boolean.FALSE, Boolean.FALSE, null));
        spiderSteps.add(new ChecklistTiEntry("NndbCountriesBuilderTarget:227", Boolean.FALSE, Boolean.FALSE, null));



//        birtSteps.put("anaconda.inds.ti.TmcLocationTableIdTest.locationTableIdCountTest", Boolean.FALSE);
//        birtSteps.put("anaconda.inds.ti.TmcLocationExporterTest.tmcNameTableTest", Boolean.FALSE);
//        birtSteps.put("anaconda.inds.ti.TmcLocationNameTest.locationNameCountTest", Boolean.FALSE);
//        birtSteps.put("anaconda.inds.ti.TmcLocationNameTest.nndbTmcNamesExportTest", Boolean.FALSE);
//        birtSteps.put("anaconda.inds.ti.TmcStringTableTest.stringCountTest", Boolean.FALSE);
//        birtSteps.put("anaconda.inds.ti.TmcStringTableTest.tmcStringTest", Boolean.FALSE);
        birtSteps.add(new ChecklistTiEntry("anaconda.inds.ti.TmcLocationTableIdTest.locationTableIdCountTest", Boolean.FALSE, Boolean.FALSE, null));
        birtSteps.add(new ChecklistTiEntry("anaconda.inds.ti.TmcLocationExporterTest.tmcNameTableTest", Boolean.FALSE, Boolean.FALSE, null));
        birtSteps.add(new ChecklistTiEntry("anaconda.inds.ti.TmcLocationNameTest.locationNameCountTest", Boolean.FALSE, Boolean.FALSE, null));
        birtSteps.add(new ChecklistTiEntry("anaconda.inds.ti.TmcLocationNameTest.nndbTmcNamesExportTest", Boolean.FALSE, Boolean.FALSE, null));
        birtSteps.add(new ChecklistTiEntry("anaconda.inds.ti.TmcStringTableTest.stringCountTest", Boolean.FALSE, Boolean.FALSE, null));
        birtSteps.add(new ChecklistTiEntry("anaconda.inds.ti.TmcStringTableTest.tmcStringTest", Boolean.FALSE, Boolean.FALSE, null));



//        ndsSteps.put("0MXXX.NDS tmcLocationTableIdTable", Boolean.FALSE);
        ndsSteps.add(new ChecklistTiEntry("0MXXX.NDS tmcLocationTableIdTable", Boolean.FALSE, Boolean.FALSE, null));

        /* !!!!!!!!!!!!!!NOT IMPLEMENTED!!!!!!!!!!!!!!!!!!!!
        ndsSteps.put("NHXXX.NDS tmcLocationTableIdTable", Boolean.FALSE); //Many files!!!!
        ndsSteps.put("TI version tmcLocationTableIdTable", Boolean.FALSE); //Define what versions are current!!!
        */



        /*
        * !!!!!!!!!!!!!!NOT IMPLEMENTED!!!!!!!!!!!!!!!!!!!!
        * Filled Local_tmc_location on Route links
        * TI Browser - TI icons and messages are present for TMC and TPEG_TEC
        * */
    }

}
