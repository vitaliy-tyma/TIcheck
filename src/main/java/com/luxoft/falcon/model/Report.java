package com.luxoft.falcon.model;

import com.luxoft.falcon.config.MainConfig;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;


/** Report of analysis */
@Data
public class Report {

    @Setter
    private List<ChecklistEntry> spiderSteps = new LinkedList<>();
    private List<ChecklistEntry> birtSteps = new LinkedList<>();
    private List<ChecklistEntry> ndsSteps = new LinkedList<>();


    public Report(){
    }


    @Getter
    private String name;
    public void setName(String name){
        String result = name.trim();
        if (result.length() > 30) {
            result = result.substring(0, 30);
            this.logOfErrors.add("\nLength of the PON name has been reduced to 30 symbols!\n");
        }
        result = result.replaceAll("[^A-Za-z0-9_%]", "");
        if (!result.equals(name)) {
            this.logOfErrors.add("\nSome extra symbols have been removed from the PON name!\n");
        }
        this.name = result;
    }

    @Getter
    private int iteration;
    public void setIteration(String iteration) {
        String result = iteration.trim();
        if (result.length() > 2) {
            result = result.substring(0, 2);
            this.logOfErrors.add("\nLength of the PON iteration has been reduced to 2 symbols!\n");
        }

        result = result.replaceAll("[^0-9]", "");
        if (!result.equals(iteration)) {
            this.logOfErrors.add("\nSome extra symbols have been removed from the PON iteration!\n");
        }
        try {
            this.iteration = Integer.parseInt(iteration);
        }
        catch (Exception e) {
            this.iteration = 1;
        }
    }

    @Getter
    @Setter
    private Boolean autocomplete = false;

    @Getter
    private int limit;
    public void setLimit(String limit){
        try {
            this.limit = Integer.parseInt(limit);
        }
        catch (Exception e) {
            this.limit = 10;
        }
    }

    @Getter
    private String prevName;
    public void setPrevName(String name){
        String result = name.trim();
        if (result.length() > 30) {
            result = result.substring(0, 30);
            this.logOfErrors.add("\nLength of the prevPON name has been reduced to 30 symbols!\n");
        }
        result = result.replaceAll("[^A-Za-z0-9_%]", "");
        if (!result.equals(name)) {
            this.logOfErrors.add("\nSome extra symbols have been removed from the prevPON name!\n");
        }
        this.prevName = result;
    }

    @Getter
    private int prevIteration;
    public void setPrevIteration(String iteration) {
        String result = iteration.trim();
        if (result.length() > 2) {
            result = result.substring(0, 2);
            this.logOfErrors.add("\nLength of the prev PON iteration has been reduced to 2 symbols!\n");
        }

        result = result.replaceAll("[^0-9]", "");
        if (!result.equals(iteration)) {
            this.logOfErrors.add("\nSome extra symbols have been removed from the prev PON iteration!\n");
        }
        try {
            this.prevIteration = Integer.parseInt(iteration);
        }
        catch (Exception e) {
            this.prevIteration = 1;
        }
    }

    @Getter
    @Setter
    private Boolean prevAutocomplete = false;




    @Getter
    @Setter
    private LinkedList<String> logOfErrors = new LinkedList<>();
    public void addLogOfErrors(String string){
        this.logOfErrors.add("<br>" + string + "<br/>\n");
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
