package com.luxoft.falcon.util;

import com.luxoft.falcon.model.ChecklistEntry;
import com.luxoft.falcon.model.Report;
import java.util.List;


/* Class forms HTML string with checklist steps*/
public class ChecklistMonitor {


    private static Integer i;

    public static String getDataFromReport(Report report) {
        i = 0;

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



        checklistMonitor.append("<tr><td colspan = 7 align=center>\n");
        checklistMonitor.append("SPIDER</td></tr>\n");
        checklistMonitor.append(getRowsFromChecklist(report, report.getSpiderSteps()));

        checklistMonitor.append("<tr><td colspan = 7 align=center>\n");
        checklistMonitor.append("BIRT</td></tr>\n");
        checklistMonitor.append(getRowsFromChecklist(report, report.getBirtSteps()));




        checklistMonitor.append("</table>");
        checklistMonitor.append("</div>");

        /*Show time and requests count*/
        checklistMonitor.append("<br/><div align = left>");

        checklistMonitor.append(
                    String.format(
                        "Elapsed %s seconds; Requests Count %s",
                        report.getElapsedTime(),
                        report.getRequestsCount()));
        checklistMonitor.append("</div>");

        /*Show error log if it exists*/
        if (report.getLogOfErrors().size() != 0) {
            checklistMonitor.append("<br/><div align = left>");
            checklistMonitor.append("<details><summary><u><b>See log of errors </b></u></summary>\n");
            checklistMonitor.append(
                    String.format(
                            "<div><p><font color=red>%s</font></p></div>\n",
                            report.getLogOfErrors().toString()));
            checklistMonitor.append("</b></details>\n");
            checklistMonitor.append("</div>");
        }
        return checklistMonitor.toString();


    }


    private static String getRowsFromChecklist(Report report, List<ChecklistEntry> steps) {

        StringBuilder checklistMonitor = new StringBuilder();





        for (ChecklistEntry entry : steps) {
            i++;
            //Show index
            checklistMonitor.append("<tr>");
            checklistMonitor.append(
                    String.format("<td align=center>%2d</td>",
                            i));


            //Show the full name of actual PON
            checklistMonitor.append(
                    String.format("<td>%s (%s)</td>",
                            entry.getFullNameOfPon(),
                            report.getIteration()));


            //Show the name of error to check
            checklistMonitor.append(
                    String.format("<td>%s</td>",
                            entry.getNameOfErrorToCheckFor()));

            //Show weather the step has been checked or not
            if (entry.getStepIsChecked()) {
                checklistMonitor.append(
                        String.format("<td align=center><font color = green>%s</font></td>",
                                entry.getStepIsChecked()));
            } else {
                checklistMonitor.append(
                        String.format("<td align=center><font color = red>%s</font></td>",
                                entry.getStepIsChecked()));
            }



            //Show result and TEST if it is not OK or NOK in the tooltip
            String stringNOK_OK;
            String color;
            if (entry.getResultOfCheckIsNOK()) {
                stringNOK_OK = "NOK";
                color = "red";
            } else {
                stringNOK_OK = "OK";
                color = entry.getResultOfCheckText().equals("OK")?"green":"blue";

            }
            checklistMonitor.append(
                    String.format("<td align=center><div class=\"tooltip_for_name\">" +
                                    "<font color = %s>%s</font>" +
                                    "<span class=\"tooltiptext\">%s</span>" +
                                    "</div></td>",
                                    color,
                                    stringNOK_OK,
                                    entry.getResultOfCheckText()));


            //Show query in the tooltip
            checklistMonitor.append(
                    String.format("<td align=center><div class=\"tooltip_for_query\"><img src=\"lib/img/glass.jpg\" \n" +
                                    " alt=\"Query\"><span class=\"tooltiptext\">%s</span></div></td>",
                            entry.getFullQuery()));



            //Show regression analysis result and the name of PON in the tooltip
            if (entry.getIsRegression().equals("Yes")) {
                checklistMonitor.append(
                        String.format("<td align=center><font color = red>" +
                                        "<div class=\"tooltip_for_name\">%s" +
                                        "<span class=\"tooltiptext\">%s</span>" +
                                        "</div></font></td>",
                                entry.getIsRegression(),
                                "To " +
                                        entry.getFullNameOfRegressionPon() +
                                        " (iteration " + report.getPrevIteration() + ")"));
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
