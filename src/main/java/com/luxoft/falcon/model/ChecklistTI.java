package com.luxoft.falcon.model;

import com.luxoft.falcon.config.MainConfig;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;


/** Entity/Data/POJO  */
@Data
public class ChecklistTI {














    private LinkedList<ChecklistEntry> spiderSteps = new LinkedList<>();
    private LinkedList<ChecklistEntry> birtSteps = new LinkedList<>();
    private LinkedList<ChecklistEntry> ndsSteps = new LinkedList<>();


    public ChecklistTI(){

        spiderSteps.add(new ChecklistEntry("NndbTmcNamesBuilderTarget:118"));
        spiderSteps.add(new ChecklistEntry("NndbCountriesBuilderTarget:227"));



        birtSteps.add(new ChecklistEntry("anaconda.inds.ti.TmcLocationTableIdTest.locationTableIdCountTest"));
        birtSteps.add(new ChecklistEntry("anaconda.inds.ti.TmcLocationExporterTest.tmcNameTableTest"));
        birtSteps.add(new ChecklistEntry("anaconda.inds.ti.TmcLocationNameTest.locationNameCountTest"));
        birtSteps.add(new ChecklistEntry("anaconda.inds.ti.TmcLocationNameTest.nndbTmcNamesExportTest"));
        birtSteps.add(new ChecklistEntry("anaconda.inds.ti.TmcStringTableTest.stringCountTest"));
        birtSteps.add(new ChecklistEntry("anaconda.inds.ti.TmcStringTableTest.tmcStringTest"));



        ndsSteps.add(new ChecklistEntry("0MXXX.NDS tmcLocationTableIdTable"));

        /* !!!!!!!!!!!!!!NOT IMPLEMENTED - MAP IS NOT USED ANY MORE !!!!!!!!!!!!!!!!!!!!
        ndsSteps.put("NHXXX.NDS tmcLocationTableIdTable", Boolean.FALSE); //Many files!!!!

        ndsSteps.put("TI version tmcLocationTableIdTable", Boolean.FALSE); //Define what versions are current!!!
        */



        /*
        * !!!!!!!!!!!!!!NOT IMPLEMENTED!!!!!!!!!!!!!!!!!!!!
        * Filled Local_tmc_location on Route links
        * TI Browser - TI icons and messages are present for TMC and TPEG_TEC
        * */
    }





    @Getter
    private String name;
    public void setName(String name){
        String result = name.trim();
        result = result.replaceAll("[^A-Za-z0-9_%]", "");
        if (!result.equals(name)) {
            this.logOfErrors.add("\nSome extra symbols have been removed from the PON name!\n");
        }

        if (result.length() > 30) {
            result = result.substring(0, 30);
            this.logOfErrors.add("\nLength of the PON name has been reduced to 30 symbols!\n");
        }
        this.name = result;
    }

    @Getter
    @Setter
    private int iteration;


    @Getter
    @Setter
    private Boolean autocomplete = false;

    @Getter
    @Setter
    private Boolean nokOnly;

    @Getter
    private String prevName;
    public void setPrevName(String name){
        String result = name.trim();
        result = result.replaceAll("[^A-Za-z0-9_%]", "");
        if (!result.equals(name)) {
            this.logOfErrors.add("\nSome extra symbols have been removed from the prevPON name!\n");
        }

        if (result.length() > 30) {
            result = result.substring(0, 30);
            this.logOfErrors.add("\nLength of the prevPON name has been reduced to 30 symbols!\n");
        }
        this.prevName = result;
    }

    @Getter
    @Setter
    private int prevIteration;

    @Getter
    @Setter
    private Boolean prevAutocomplete = false;




    @Getter
    @Setter
    private LinkedList<String> logOfErrors = new LinkedList<>();

    public void addLogOfErrors(String string){
        this.logOfErrors.add(string);
    }

    @Getter
    private String checklistName;
    public void setChecklistName(String checklistName){
        if (checklistName.equals(MainConfig.getCHECKLISTS_NAME_TI()) ||
                false){//add more checks
            this.checklistName = checklistName;
        } else {
            this.checklistName = MainConfig.getCHECKLISTS_NAME_UNDEF();
        }
    }



}
