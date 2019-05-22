package com.luxoft.falcon.model;

import com.luxoft.falcon.config.MainConfig;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;


/** Report of analysis */
public class Report {

    @Getter @Setter
    private List<ChecklistEntry> spiderSteps = new LinkedList<>();
    @Getter @Setter
    private List<ChecklistEntry> birtSteps = new LinkedList<>();
    @Getter @Setter
    private List<ChecklistEntry> ndsSteps = new LinkedList<>();


    public Report(){
    }


    @Getter
    private String name;
    public void setName(String name) throws Exception {
        if (name == null){
            throw new Exception("Name of PON is not defined");
        }

        String result = name.trim();
        if (result.length() > 30) {
            result = result.substring(0, 30);
            this.logOfErrors.add("\nLength of the PON name has been reduced to 30 symbols!\n<br/>");
        }
        result = result.replaceAll("[^A-Za-z0-9_%]", "");
        if (!result.equals(name)) {
            this.logOfErrors.add("\nSome extra symbols have been removed from the PON name!\n<br/>");
        }
        this.name = result;
    }

    @Getter
    private int iteration;
    public void setIteration(String iteration) throws Exception {
        if (iteration == null){
            throw new Exception("Iteration of PON is not defined");
        }

        String result = iteration.trim();
        if (result.length() > 2) {
            result = result.substring(0, 2);
            this.logOfErrors.add("\nLength of the PON iteration has been reduced to 2 symbols!\n<br/>");
        }

        result = result.replaceAll("[^0-9]", "");
        if (!result.equals(iteration)) {
            this.logOfErrors.add("\nSome extra symbols have been removed from the PON iteration!\n<br/>");
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
    private Boolean useQueryLike = false;

    @Getter
    private int limit;
    public void setLimit(String limit){
        try {
            this.limit = Integer.parseInt(limit);
        }
        catch (Exception e) {
            this.limit = 100;
        }
    }

    @Getter
    private String prevName;
    public void setPrevName(String name) throws Exception {
        if (name == null){
            throw new Exception("Name of prev PON is not defined");
        }

        String result = name.trim();
        if (result.length() > 30) {
            result = result.substring(0, 30);
            this.logOfErrors.add("\nLength of the prevPON name has been reduced to 30 symbols!\n<br/>");
        }
        result = result.replaceAll("[^A-Za-z0-9_%]", "");
        if (!result.equals(name)) {
            this.logOfErrors.add("\nSome extra symbols have been removed from the prevPON name!\n<br/>");
        }
        this.prevName = result;
    }

    @Getter
    private int prevIteration;
    public void setPrevIteration(String iteration) throws Exception {
        if (iteration == null){
            throw new Exception("Iteration of prev PON is not defined");
        }

        String result = iteration.trim();
        if (result.length() > 2) {
            result = result.substring(0, 2);
            this.logOfErrors.add("\nLength of the prev PON iteration has been reduced to 2 symbols!\n<br/>");
        }

        result = result.replaceAll("[^0-9]", "");
        if (!result.equals(iteration)) {
            this.logOfErrors.add("\nSome extra symbols have been removed from the prev PON iteration!\n<br/>");
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
    private Boolean useQueryLikeForPrev = false;




    @Getter
    @Setter
    private LinkedList<String> logOfErrors = new LinkedList<>();
    public void addLogOfErrors(String string){
        this.logOfErrors.add("<br>" + string + "<br/>\n");
    }

//TODO Clarify usage of report.checklistName - seems that it is not in use (and no setter here)
    @Getter @Setter
    private String checklistName;


    @Getter @Setter
    private Float elapsedTime;

    @Getter @Setter
    private int requestsCount;


}
