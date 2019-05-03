package com.luxoft.falcon.util;

import com.luxoft.falcon.model.ChecklistTI;
import com.luxoft.falcon.model.ChecklistTiEntry;
import com.luxoft.falcon.model.ErrorRecord;
import com.luxoft.falcon.model.Pon;

import java.util.ArrayList;


/* Class forms HTML string with checklist steps*/
public class ChecklistMonitor {

    public static String getData(ChecklistTI checklistTI, Pon pon) {
        Integer i = 0;
        StringBuilder checklistMonitor = new StringBuilder();

        checklistMonitor.append("<div>");
//        checklistMonitor.append(
//                String.format("<details><summary><u><b>Full log of passed steps of %s checklist</b></u></summary>\n",
//                        pon.getChecklistName()));


        checklistMonitor.append("<table border=1>");
        checklistMonitor.append("<tr>");
        checklistMonitor.append("<th>##</th>");
        checklistMonitor.append("<th>Step</th>");
        checklistMonitor.append("<th>Checked</th>");
        checklistMonitor.append("<th>Result</th>");
        checklistMonitor.append("<th>Query</th>");
        checklistMonitor.append("</tr>");

        checklistMonitor.append(getRows("SPIDER", checklistTI.getSpiderSteps(), i, pon));
        checklistMonitor.append(getRows("BIRT", checklistTI.getBirtSteps(), i, pon));

        checklistMonitor.append("</table>");
//        checklistMonitor.append("</font></b></details>\n");
        checklistMonitor.append("</div>");

        return checklistMonitor.toString();
    }



    private static String getRows(String serviceName, ArrayList<ChecklistTiEntry> steps, int i, Pon pon) {

        StringBuilder checklistMonitor = new StringBuilder();

        checklistMonitor.append("<tr>");
        checklistMonitor.append("<td colspan = 5 align=center>");
        checklistMonitor.append(serviceName);
        checklistMonitor.append("</td>");
        checklistMonitor.append("</tr>");


        for (ChecklistTiEntry entry : steps) {
            checklistMonitor.append("<tr>");
            i++;
            checklistMonitor.append(
                    String.format("<td align=center>%2d</td>",
                            i));

            checklistMonitor.append(
                    String.format("<td>%s</td>",
                            entry.getNameOfError()));


            if (entry.getStepIsChecked()) {
                checklistMonitor.append(
                        String.format("<td align=center><font color = green>%s</font></td>",
                                entry.getStepIsChecked()));
            } else {
                checklistMonitor.append("<font color = red>");
                checklistMonitor.append(
                        String.format("<td align=center><font color = red>%s</font></td>",
                                entry.getStepIsChecked()));
            }

            if (entry.getResultOfCheckIsNOK()) {
                checklistMonitor.append(
                        String.format("<td align=center><font color = red>%s</font></td>",
                                "NOK"));
            } else {
                checklistMonitor.append(
                        String.format("<td align=center><font color = green>%s</font></td>",
                                "OK"));
            }



            checklistMonitor.append(
                    String.format("<td align=center><div class=\"tooltip\"><img src=\"lib/img/glass.jpg\" \n" +
                                    " alt=\"Query\"><span class=\"tooltiptext\">%s</span></div></td>",
                            entry.getFullQuery()));//query



            checklistMonitor.append("</tr>");
        }
        return checklistMonitor.toString();
    }



    public static String getSpiderErrorsData(ChecklistTI checklistTI, Pon pon) {
        StringBuilder result = new StringBuilder();
        if (pon.getSpiderErrors() != null) {
            result.append("<details><summary><u>SPIDER errors</u></summary>\n");
        }

        for (ErrorRecord spiderError : pon.getSpiderErrors()) {
            result.append(
                    String.format(
                            "<div><p>Full Name = %s, Error = %s</p></div>\n",
                            spiderError.getFullName(),
                            spiderError.getError()));
        }

        if (pon.getSpiderErrors() != null) {
            result.append("</details>\n");
        }
        return result.toString();
    }

    public static String getBirtErrorsData(ChecklistTI checklistTI, Pon pon) {
        StringBuilder result = new StringBuilder();
        if (pon.getBirtErrors() != null) {
            result.append("<details><summary><u>BIRT errors</u></summary>\n");
        }
        for (ErrorRecord birtError : pon.getBirtErrors()) {
            result.append(
                    String.format(
                            "<div><p>Full Name = %s, Error = %s</p></div>\n",
                            birtError.getFullName(),
                            birtError.getError()));
        }
        if (pon.getBirtErrors() != null) {
            result.append("</details>\n");
        }
        return result.toString();
    }
}
