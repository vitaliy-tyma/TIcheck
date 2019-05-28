package com.luxoft.falcon.model;

import com.luxoft.falcon.config.MainConfig;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;


/**
 * Report of analysis
 */
public class Report {

    @Getter
    @Setter
    private List<ChecklistEntry> spiderSteps = new LinkedList<>();

    @Getter
    @Setter
    private List<ChecklistEntry> birtSteps = new LinkedList<>();
    public void addBirtSteps(List<ChecklistEntry> list) {
        this.birtSteps.addAll(list);
    }

    @Getter
    @Setter
    private List<ChecklistEntry> ndsSteps = new LinkedList<>();


    public Report() {
    }


    //    @Getter
    private String name;
    public String getName() {
        return (name == null ? "" : name);
    }

    public void setName(String name) throws Exception {
        if (name == null) {
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
        this.name = result.toUpperCase();
    }

    //    @Getter
    private Integer iteration;
    public Integer getIteration() {
//        try {
//            if ((Integer) iteration == null) {
//                return 1;
//            }
//        } catch (Exception e) {
//            return 1;
//        }
        return (iteration == null?1:iteration);
    }
    public void setIteration(String iteration) throws Exception {
        if (iteration == null) {
            throw new Exception("Iteration of PON is not defined");
        }

        String result = iteration.trim();
        if (result.length() > 3) {
            result = result.substring(0, 3);
            this.logOfErrors.add("\nLength of the PON iteration has been reduced to 2 symbols!\n<br/>");
        }

        result = result.replaceAll("[^0-9]", "");
        if (!result.equals(iteration)) {
            this.logOfErrors.add("\nSome extra symbols have been removed from the PON iteration!\n<br/>");
        }
        try {
            this.iteration = Integer.parseInt(iteration);
        } catch (Exception e) {
            this.iteration = 1;
        }
    }

    @Getter
    @Setter
    private Boolean useQueryLike = true;

//    @Getter
    private Integer limit;
    public Integer getLimit() {
//        try {
//            if ((Integer) limit == null) {
//                return 100;
//            }
//        } catch (Exception e) {
//            return 100;
//        }
        return (limit == null? 100: limit);
    }
    public void setLimit(String limit) {
        try {
            this.limit = Integer.parseInt(limit);
        } catch (Exception e) {
            this.limit = 100;
        }
    }

//    @Getter
    private String prevName;
    public String getPrevName() {
        return (prevName == null ? "" : prevName);
    }
    public void setPrevName(String name) throws Exception {
        if (name == null) {
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
        this.prevName = result.toUpperCase();
    }

//    @Getter
    private Integer prevIteration;
    public Integer getPrevIteration() {
//        try {
//            if ((Integer) iteration == null) {
//                return 1;
//            }
//        } catch (Exception e) {
//            return 1;
//        }
        return (prevIteration == null?1:prevIteration);
    }
    public void setPrevIteration(String iteration) throws Exception {
        if (iteration == null) {
            throw new Exception("Iteration of prev PON is not defined");
        }

        String result = iteration.trim();
        if (result.length() > 3) {
            result = result.substring(0, 3);
            this.logOfErrors.add("\nLength of the prev PON iteration has been reduced to 2 symbols!\n<br/>");
        }

        result = result.replaceAll("[^0-9]", "");
        if (!result.equals(iteration)) {
            this.logOfErrors.add("\nSome extra symbols have been removed from the prev PON iteration!\n<br/>");
        }
        try {
            this.prevIteration = Integer.parseInt(iteration);
        } catch (Exception e) {
            this.prevIteration = 1;
        }
    }

    @Getter
    @Setter
    private Boolean useQueryLikeForPrev = true;


    @Getter
    @Setter
    private LinkedList<String> logOfErrors = new LinkedList<>();

    public void addLogOfErrors(String string) {
        this.logOfErrors.add("<br>" + string + "<br/>\n");
    }


    //TODO Clarify usage of report.checklistName - seems that it is not in use (and no setter here)
    @Getter
    @Setter
    private String checklistName;


    @Getter
    @Setter
    private Float elapsedTime;

    @Getter
    @Setter
    private int requestsCount;





    public void clear() {
        spiderSteps.clear();
        birtSteps.clear();
        ndsSteps.clear();
    }
}
