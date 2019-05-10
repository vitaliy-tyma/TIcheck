package com.luxoft.falcon.model;

import com.luxoft.falcon.config.MainConfig;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;


/** Entity/Data/POJO  */
@Data
public class Checklist {

    private List<String> spiderSteps = new LinkedList<>();
    private List<String> birtSteps = new LinkedList<>();
    private List<String> ndsSteps = new LinkedList<>();

    public Checklist(String checklistName){
        /* To be replaced with calling of loader - xml file with name of checklistName*/
        this();
        setChecklistName(checklistName);
    }


    public Checklist(){
        spiderSteps.add("NndbTmcNamesBuilderTarget:118");
        spiderSteps.add("NndbCountriesBuilderTarget:227");

        birtSteps.add("anaconda.inds.ti.TmcLocationTableIdTest.locationTableIdCountTest");
        birtSteps.add("anaconda.inds.ti.TmcLocationExporterTest.tmcNameTableTest");
        birtSteps.add("anaconda.inds.ti.TmcLocationNameTest.locationNameCountTest");
        birtSteps.add("anaconda.inds.ti.TmcLocationNameTest.nndbTmcNamesExportTest");
        birtSteps.add("anaconda.inds.ti.TmcStringTableTest.stringCountTest");
        birtSteps.add("anaconda.inds.ti.TmcStringTableTest.tmcStringTest");

        /* !!!!!!!!!!!!!!NOT IMPLEMENTED!!!!!!!!!!!!!!!!!!!!*/
        ndsSteps.add("0MXXX.NDS tmcLocationTableIdTable");
        ndsSteps.add("NHXXX.NDS tmcLocationTableIdTable"); //Many files must be processed!!!!

        ndsSteps.add("TI version tmcLocationTableIdTable"); //Find out what versions are current!!!

        /* !!!!!!!!!!!!!!NOT IMPLEMENTED!!!!!!!!!!!!!!!!!!!!
        * Filled Local_tmc_location on Route links
        * TI Browser - TI icons and messages are present for TMC and TPEG_TEC
        * */
    }


    @Getter
    private String checklistName;
    public void setChecklistName(String checklistName){
        if (checklistName == null){
            this.checklistName = MainConfig.getCHECKLISTS_NAME_UNDEF();
        }
    }



}
