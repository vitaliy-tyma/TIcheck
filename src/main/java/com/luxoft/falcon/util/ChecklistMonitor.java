package com.luxoft.falcon.util;

import com.luxoft.falcon.model.Checklist;
import com.luxoft.falcon.model.ChecklistEntry;
import com.luxoft.falcon.model.Report;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/* Class forms HTML string with checklist steps*/
public class ChecklistMonitor {


    private static Integer i;


    public static String getDataFromreport(Report report) {


        StringBuilder checklistMonitor = new StringBuilder();

        checklistMonitor.append("<div>");


        checklistMonitor.append("<table border=1 width = 95%>");
        checklistMonitor.append("<tr>");
        checklistMonitor.append("<th>##</th>");
        checklistMonitor.append("<th>Full name (iteration)</th>");
        checklistMonitor.append("<th>Step</th>");
        checklistMonitor.append("<th>Checked</th>");
        checklistMonitor.append("<th>Result</th>");
        checklistMonitor.append("<th>Query</th>");
        checklistMonitor.append("<th>Regression</th>");
        checklistMonitor.append("</tr>");

        i = 0;
        checklistMonitor.append(getRowsFromChecklist("SPIDER", report));
        checklistMonitor.append(getRowsFromChecklist("BIRT", report));

        checklistMonitor.append("</table>");
        checklistMonitor.append("</div>");


        if (report.getLogOfErrors().size() != 0) {
            checklistMonitor.append("<div align = left>");
            checklistMonitor.append("<details><summary><u><b>See log of errors </b></u></summary>\n");
            checklistMonitor.append(
                    String.format(
                            "<div><p><font color=red>%s</font></p></div>\n",
//                            checklist.getLogOfErrors().toString()));
                            Arrays.toString(report.getLogOfErrors().toArray())));
            checklistMonitor.append("</b></details>\n");
            checklistMonitor.append("</div>");
        }
        return checklistMonitor.toString();


    }


    private static String getRowsFromChecklist(String serviceName, Report report) {

        StringBuilder checklistMonitor = new StringBuilder();

        checklistMonitor.append("<tr>");
        checklistMonitor.append("<td colspan = 7 align=center>");
        checklistMonitor.append(serviceName);
        checklistMonitor.append("</td>");
        checklistMonitor.append("</tr>");

        List<ChecklistEntry> steps = new LinkedList<>();

        switch (serviceName){
            case "SPIDER": steps = report.getSpiderSteps();
            break;
            case "BIRT": steps = report.getBirtSteps();
            break;
        }

        for (ChecklistEntry entry : steps) {
            checklistMonitor.append("<tr>");
            i++;
            checklistMonitor.append(
                    String.format("<td align=center>%2d</td>",
                            i));



            checklistMonitor.append(
                    String.format("<td>%s (%s)</td>",
                            entry.getFullNameOfPon(),
                            report.getIteration()));


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
                                "NOK " +
                                entry.getResultOfCheckText()));
            } else {
                checklistMonitor.append(
                        String.format("<td align=center><font color = green>%s</font></td>",
                                "OK " +
                                entry.getResultOfCheckText()));
            }


            checklistMonitor.append(
                    String.format("<td align=center><div class=\"tooltip_for_query\"><img src=\"lib/img/glass.jpg\" \n" +
                                    " alt=\"Query\"><span class=\"tooltiptext\">%s</span></div></td>",
                            entry.getFullQuery()));//query



//            if (entry.getFullNameOfPon() != null) {
//                String addThreeDots = "";
//                if (entry.getFullNameOfPon() != name) {
//                    addThreeDots = "...";
//                }
//                checklistMonitor.append(
//                        String.format("<td align=center>" +
//                                        "<div class=\"tooltip_for_name\">%s" +
//                                        "<span class=\"tooltiptext\">%s</span>" +
//                                        "</div></td>",
//                                name + addThreeDots,
//                                entry.getFullNameOfPon()));//FullName
//            } else checklistMonitor.append("<td></td>");


            if (entry.getIsRegression().equals("Yes")) {
                checklistMonitor.append(
                        String.format("<td align=center><font color = red>" +
                                        "<div class=\"tooltip_for_name\">%s" +
                                        "<span class=\"tooltiptext\">%s</span>" +
                                        "</div></font></td>",
                                        entry.getIsRegression(),
                                        "To " +
                                                report.getPrevName()+
                                        " (" + report.getPrevIteration() + ")"));

//                        String.format("<td align = center><font color = red>%s</font></td>",
//                                entry.getIsRegression()+
//                                        " " +
//                                        checklist.getPrevName()+
//                                        " (" +
//                                        checklist.getPrevIteration() +
//                                        ")"
//                        ));
            } else {
                checklistMonitor.append(
                        String.format("<td align = center>%s</td>",
                                entry.getIsRegression()));
            }




            checklistMonitor.append("</tr>");
        }
        return checklistMonitor.toString();
    }
}
