package com.luxoft.falcon.model;
/* TO BE DELETED*/
import com.luxoft.falcon.config.MainConfig;
import lombok.*;

import java.util.LinkedList;


@NoArgsConstructor
@ToString
@EqualsAndHashCode
public final class Pon {





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





    @Getter
    @Setter
    private LinkedList<ErrorRecord> spiderErrors;
    @Getter
    @Setter
    private Boolean noSpiderErrorsPresent = false;




    @Getter
    @Setter
    private LinkedList<ErrorRecord> birtErrors;
    @Getter
    @Setter
    private Boolean noBirtErrorsPresent = false;


    @Getter
    @Setter
    private LinkedList<ErrorRecord> ndsErrors;
    @Getter
    @Setter
    private Boolean noNdsErrorsPresent = false;





}



