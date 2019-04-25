package com.luxoft.falcon.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/** Entity/Data/POJO to connect to the SPIDER */
@Data
public class Checklist {
    private Map<String, Boolean> spiderSteps = new HashMap<>();

    private Map<String, Boolean> birtSteps = new HashMap<>();
    private Map<String, Boolean> ndsSteps = new HashMap<>();

    public Checklist(){
        spiderSteps.put("NndbTmcNamesBuilderTarget:118", Boolean.FALSE);
        spiderSteps.put("NndbCountriesBuilderTarget:227", Boolean.FALSE);



        birtSteps.put("ti.TmcLocationTableIdTest.locationTableIdCountTest", Boolean.FALSE);
        birtSteps.put("ti.TmcLocationExporterTest.tmcNameTableTest", Boolean.FALSE);
        birtSteps.put("anaconda.inds.ti.TmcLocationNameTest.locationNameCountTest", Boolean.FALSE);
        birtSteps.put("anaconda.inds.ti.TmcLocationNameTest.nndbTmcNamesExportTest", Boolean.FALSE);
        birtSteps.put("anaconda.inds.ti.TmcStringTableTest.stringCountTest", Boolean.FALSE);
        birtSteps.put("anaconda.inds.ti.TmcStringTableTest.tmcStringTest", Boolean.FALSE);


        ndsSteps.put("0MXXX.NDS tmcLocationTableIdTable", Boolean.FALSE);
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
