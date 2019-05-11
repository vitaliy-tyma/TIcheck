package com.luxoft.falcon.util;

import com.luxoft.falcon.model.ChecklistEntry;
import com.luxoft.falcon.model.Report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/* Class forms HTML string with checklist steps*/
public class ReportToHtml {


    private static Integer i;

    public static String getDataFromReport(Report report) {
        i = 0;

        StringBuilder result = new StringBuilder();

        result.append("<div>");


        result.append("<table border=1 width = 95%>");
        result.append("<tr>");
        result.append("<th>##</th>");
        result.append("<th>Full name (iteration)</th>");
        result.append("<th>Step</th>");
        result.append("<th>Checked</th>");
        result.append("<th>Result</th>");
        result.append("<th>Query</th>");
        result.append("<th>Regression</th>");
        result.append("</tr>");



        result.append("<tr><td colspan = 7 align=center>\n");
        result.append("SPIDER</td></tr>\n");
        result.append(getRowsFromReport(report, report.getSpiderSteps()));

        result.append("<tr><td colspan = 7 align=center>\n");
        result.append("BIRT</td></tr>\n");
        result.append(getRowsFromReport(report, report.getBirtSteps()));




        result.append("</table>");
        result.append("</div>");

        /*Show time, requests count and date time*/
        result.append("<br/><div align = left>");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();


        result.append(
                    String.format(
                        "Elapsed: %s seconds; Requests Count: %s; Request Time is: %s.",
                        report.getElapsedTime(),
                        report.getRequestsCount(),
                        dateFormat.format(date)));
        result.append("</div>");

        /*Show error log if it exists*/
        if (report.getLogOfErrors().size() != 0) {
            result.append("<br/><div align = left>");
            result.append("<details><summary><u><b>See log of errors </b></u></summary>\n");
            result.append(
                    String.format(
                            "<div><p><font color=red>%s</font></p></div>\n",
                            report.getLogOfErrors().toString()));
            result.append("</b></details>\n");
            result.append("</div>");
        }
        return result.toString();


    }


    private static String getRowsFromReport(Report report, List<ChecklistEntry> steps) {

        StringBuilder result = new StringBuilder();





        for (ChecklistEntry entry : steps) {
            i++;
            //Show index
            result.append("<tr>");
            result.append(
                    String.format("<td align=center>%2d</td>",
                            i));


            //Show the full name of actual PON
            result.append(
                    String.format("<td>%s (%s)</td>",
                            entry.getFullNameOfPon(),
                            report.getIteration()));


            //Show the name of error to check
            result.append(
                    String.format("<td>%s</td>",
                            entry.getNameOfErrorToCheckFor()));

            //Show weather the step has been checked or not
            if (entry.getStepIsChecked()) {
                result.append(
                        String.format("<td align=center><font color = green>%s</font></td>",
                                entry.getStepIsChecked()));
            } else {
                result.append(
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

            result.append(
                    String.format("<td align=center><div class=\"tooltip_for_name\">" +
                                    "<font color = %s>%s</font>" +
                                    "<span class=\"tooltiptext\">%s</span>" +
                                    "</div></td>",
                                    color,
                                    entry.getResultOfCheckText(),
                                    stringNOK_OK));


            //Show query in the tooltip
            result.append(
                    String.format("<td align=center><div class=\"tooltip_for_query\"><img src=\"lib/img/glass.jpg\" \n" +
                                    " alt=\"Query\"><span class=\"tooltiptext\">%s</span></div></td>",
                            entry.getFullQuery()));



            //Show regression analysis result and the name of PON in the tooltip
            if (entry.getIsRegression().equals("Yes")) {
                result.append(
                        String.format("<td align=center><font color = red>" +
                                        "<div class=\"tooltip_for_name\">%s" +
                                        "<span class=\"tooltiptext\">%s</span>" +
                                        "</div></font></td>",
                                entry.getIsRegression(),
                                "To " +
                                        entry.getFullNameOfRegressionPon() +
                                        " (iteration " + report.getPrevIteration() + ")"));
            } else {
                result.append(
                        String.format("<td align = center>%s</td>",
                                entry.getIsRegression()));
            }


            result.append("</tr>");
        }
        return result.toString();
    }
}
