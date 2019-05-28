package com.luxoft.falcon.util;

import com.luxoft.falcon.model.ChecklistEntry;
import com.luxoft.falcon.model.Report;
import java.util.List;


/* Class forms HTML string with checklist steps*/
public class ReportToHtml {


    private static Integer i;
    private static boolean bgColorSwap = true;
    private static String tempErrorToCheck = "";


    @SuppressWarnings("StringBufferReplaceableByString")
    public static String getDataFromReport(Report report) {
        i = 0;
        StringBuilder result = new StringBuilder();

        result.append("<div>\n");

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


        result.append(getRowsFromReport("SPIDER", report, report.getSpiderSteps()));
        result.append(getRowsFromReport("BIRT", report, report.getBirtSteps()));
        result.append(getRowsFromReport("NDS", report, report.getNdsSteps()));
        result.append("</table>\n");
        result.append("</div>\n");

        return result.toString();
    }


    private static String getRowsFromReport(String stepName, Report report, List<ChecklistEntry> steps) {

        StringBuilder result = new StringBuilder();

        if (steps.size() != 0) {
            result.append("<tr>\n<td colspan = 7 align=center>\n");
            result.append(stepName + "\n</td>\n</tr>\n");
        }

        for (ChecklistEntry entry : steps) {
            i++;

            if (!tempErrorToCheck.equals(entry.getNameOfErrorToCheckFor())) {
                bgColorSwap = !bgColorSwap;
            }
            tempErrorToCheck = entry.getNameOfErrorToCheckFor();


            //Show index
            result.append("\n <tr id = ");
            result.append(bgColorSwap ? "ROW_WHITE" : "ROW_GRAY");
            result.append(">");
            result.append(
                    String.format("\n<td align=center>%2d</td>\n",
                            i));


            //Show the full name of actual PON
            if (entry.getAggregatedNames() == null) {
                result.append(
                        String.format("\n<td>%s (%s)</td>\n",
                                entry.getFullNameOfPon(),
                                report.getIteration()));

            } else {
                result.append(
                        String.format("<td align=center><font color = blue>" +
                                        "<div class=\"tooltip_for_name\">%s (%s)" +
                                        "<span class=\"tooltiptext\">%s</span>" +
                                        "</div></font></td>\n",
                                entry.getFullNameOfPon(),
                                report.getIteration(),
                                entry.getAggregatedNames()));
            }







            //Show the name of error to check
            result.append(
                    String.format("<td>%s</td>\n",
                            entry.getNameOfErrorToCheckFor()));


            //Show whether the step has been checked or not
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
            String color;
            if (entry.getResultOfCheckIsNOK()) {
                color = "red";
            } else {
                color = entry.getResultOfCheckText().equals("OK") ? "green" : "blue";

            }

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
                                entry.getFullNameOfRegressionPon() +
                                " (iteration " + report.getPrevIteration() + ")"));
            } else if (entry.getFullNameOfRegressionPon() != null &&
                    !entry.getFullNameOfRegressionPon().equals("")) {
                result.append(
                        String.format("<td align=center><font color = blue>" +
                                        "<div class=\"tooltip_for_name\">%s" +
                                        "<span class=\"tooltiptext\">%s</span>" +
                                        "</div></font></td>\n",
                                entry.getIsRegression(),
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
