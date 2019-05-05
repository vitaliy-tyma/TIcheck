package com.luxoft.falcon.util;

import com.luxoft.falcon.model.ChecklistTI;
import com.luxoft.falcon.model.ChecklistEntry;
import com.luxoft.falcon.model.ErrorRecord;
import com.luxoft.falcon.model.Pon;

import java.util.LinkedList;


/* Class forms HTML string with checklist steps*/
public class ChecklistMonitor {



    private static Integer i = 0;


    public static String getDataFromChecklist(ChecklistTI checklist) {



        StringBuilder checklistMonitor = new StringBuilder();

        checklistMonitor.append("<div>");


        checklistMonitor.append("<table border=1 width = 95%>");
        checklistMonitor.append("<tr>");
        checklistMonitor.append("<th>##</th>");
        checklistMonitor.append("<th>Full name</th>");
        checklistMonitor.append("<th>Step</th>");
        checklistMonitor.append("<th>Checked</th>");
        checklistMonitor.append("<th>Result</th>");
        checklistMonitor.append("<th>Query</th>");
        checklistMonitor.append("<th>Regression</th>");
        checklistMonitor.append("</tr>");


        checklistMonitor.append(getRowsFromChecklist("SPIDER", checklist.getSpiderSteps(), checklist.getName()));
        checklistMonitor.append(getRowsFromChecklist("BIRT", checklist.getBirtSteps(), checklist.getName()));

        checklistMonitor.append("</table>");
        checklistMonitor.append("</div>");





        if (checklist.getLogOfErrors().size() != 0) {
            checklistMonitor.append("<div align = left>");
            checklistMonitor.append("<details><summary><u><b>See log of errors </b></u></summary>\n");
            checklistMonitor.append(
                    String.format(
                            "<div><p><font color=red>%s</font></p></div>\n", checklist.getLogOfErrors().toString()));
            checklistMonitor.append("</b></details>\n");
            checklistMonitor.append("</div>");
        }
        return checklistMonitor.toString();


    }




    private static String getRowsFromChecklist(String serviceName, LinkedList<ChecklistEntry> steps, String name) {

        StringBuilder checklistMonitor = new StringBuilder();

        checklistMonitor.append("<tr>");
        checklistMonitor.append("<td colspan = 7 align=center>");
        checklistMonitor.append(serviceName);
        checklistMonitor.append("</td>");
        checklistMonitor.append("</tr>");


        for (ChecklistEntry entry : steps) {
            checklistMonitor.append("<tr>");
            i++;
            checklistMonitor.append(
                    String.format("<td align=center>%2d</td>",
                            i));


            if (entry.getFullNameOfPon() != null) {
                String addThreeDots = "";
                if (entry.getFullNameOfPon() != name) {
                    addThreeDots = "...";
                }
                checklistMonitor.append(
                        String.format("<td align=center>" +
                                        "<div class=\"tooltip_for_name\">%s" +
                                        "<span class=\"tooltiptext\">%s</span>" +
                                        "</div></td>",
                                name + addThreeDots,
                                entry.getFullNameOfPon()));//FullName
            } else checklistMonitor.append("<td></td>");


            checklistMonitor.append(
                    String.format("<td>%s</td>",
                            entry.getNameOfErrorToCheckFor()));

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
                    String.format("<td align=center><div class=\"tooltip_for_query\"><img src=\"lib/img/glass.jpg\" \n" +
                                    " alt=\"Query\"><span class=\"tooltiptext\">%s</span></div></td>",
                            entry.getFullQuery()));//query

            checklistMonitor.append(
                    String.format("<td>%s</td>",
                            entry.getIsRegression()));

            checklistMonitor.append("</tr>");
        }
        return checklistMonitor.toString();
    }
}
