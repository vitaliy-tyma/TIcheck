package com.luxoft.falcon.model;

import com.luxoft.falcon.config.MainConfig;
import lombok.*;

import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;


@NoArgsConstructor
//@AllArgsConstructor
@ToString
@EqualsAndHashCode
public final class Pon {

    @Getter
    private String name;
    public void setName(String name){
        String result = name.trim();

        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()!-]");

        if (regex.matcher(result).find()) {
            this.outputOfErrors = "\nSome extra symbols have been removed from the PON name!\n";
        }

        result = result.replaceAll("[^A-Za-z0-9_%]", "");
        if (result.length() > 30) {
            result = result.substring(0, 30);
            this.outputOfErrors = this.outputOfErrors + "\nLength of the PON name has been reduced to 30 symbols!\n";
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
    private Map<String, Boolean> nds;

    @Getter
    @Setter
    private String outputOfErrors;

}



