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
    private static boolean bgColorSwap = true;
    private static String tempErrorToCheck = "";


    public static String getDataFromReport(Report report) {
        i = 0;

        StringBuilder result = new StringBuilder();

        result.append("<div>");


        result.append("<table border=1 width = 95%>\n");
        result.append("<tr>\n");
        result.append("<th>#<br/>#</th>\n");
        result.append("<th>Full name (iteration)</th>\n");
        result.append("<th>Step</th>\n");
        result.append("<th>Checked</th>\n");
        result.append("<th>Result</th>\n");
        result.append("<th>Query</th>\n");
        result.append("<th>Regression</th>\n");
        result.append("</tr>\n");


        result.append("<tr><td colspan = 7 align=center>\n");
        result.append("SPIDER</td></tr>\n");
        result.append(getRowsFromReport(report, report.getSpiderSteps()));

        result.append("<tr><td colspan = 7 align=center>\n");
        result.append("BIRT</td></tr>\n");
        result.append(getRowsFromReport(report, report.getBirtSteps()));


        result.append("</table>\n");
        result.append("</div>\n");


        return result.toString();


    }


    private static String getRowsFromReport(Report report, List<ChecklistEntry> steps) {

        StringBuilder result = new StringBuilder();



        for (ChecklistEntry entry : steps) {
            i++;

            if (!tempErrorToCheck.equals(entry.getNameOfErrorToCheckFor())) {
                bgColorSwap = !bgColorSwap;
            }
            tempErrorToCheck = entry.getNameOfErrorToCheckFor();

            //Show index
            result.append("\n <tr id = " + (bgColorSwap ? "ROW_WHITE" : "ROW_GRAY") + ">");
            result.append(
                    String.format("\n<td align=center>%2d</td>\n",
                            i));


            //Show the full name of actual PON
            result.append(
                    String.format("\n<td>%s (%s)</td>\n",
                            entry.getFullNameOfPon(),
                            report.getIteration()));


            //Show the name of error to check
            result.append(
                    String.format("<td>%s</td>\n",
                            entry.getNameOfErrorToCheckFor()));


            //Show weather the step has been checked or not
            if (entry.getStepIsChecked()) {
                result.append(
                        String.format("<td align=center><font color = green>%s</font></td>\n",
                                entry.getStepIsChecked()));
            } else {
                result.append(
                        String.format("<td align=center><font color = red>%s</font></td>\n",
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
                color = entry.getResultOfCheckText().equals("OK") ? "green" : "blue";

            }

//            result.append(
//                    String.format("<td align=center><div class=\"tooltip_for_name\">" +
//                                    "<font color = %s>%s</font>" +
//                                    "<span class=\"tooltiptext\">%s</span>" +
//                                    "</div></td>",
//                                    color,
//                                    entry.getResultOfCheckText(),
//                                    stringNOK_OK));
            result.append(
                    String.format("<td align=center>" +
                                    "<font color = %s>%s</font>" +
                                    "</td>\n",
                            color,
                            entry.getResultOfCheckText()));

            //Show query in the tooltip
            result.append(
                    String.format("<td align=center><div class=\"tooltip_for_query\"><img src=\"lib/img/glass.jpg\" \n" +
                                    " alt=\"Query\"><span class=\"tooltiptext\">%s</span></div></td>\n",
                            entry.getFullQuery()));


            //Show regression analysis result and the name of PON in the tooltip
            if (entry.getIsRegression().equals("Yes")) {
                result.append(
                        String.format("<td align=center><font color = red>" +
                                        "<div class=\"tooltip_for_name\">%s" +
                                        "<span class=\"tooltiptext\">%s</span>" +
                                        "</div></font></td>\n",
                                entry.getIsRegression(),
                                "To " +
                                        entry.getFullNameOfRegressionPon() +
                                        " (iteration " + report.getPrevIteration() + ")"));
            } else {
                result.append(
                        String.format("<td align = center>%s</td>\n",
                                entry.getIsRegression()));
            }


            result.append("</tr>\n");
        }
        return result.toString();
    }
}
