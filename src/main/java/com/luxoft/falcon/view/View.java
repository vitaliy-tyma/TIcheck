package com.luxoft.falcon.view;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.ChecklistEntry;
import com.luxoft.falcon.model.ChecklistsList;
import com.luxoft.falcon.model.ChecklistsListEntry;
import com.luxoft.falcon.model.Report;
import com.luxoft.falcon.util.ReportToHtml;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class View {
    private static MainConfig mainConfig = MainConfig.getInstance();

    /* MAIN METHOD*/
    public static String getHTML(
            String nameOfChecklist,
            Report report,
            boolean analyseRegression,
            boolean showEmptyForm) {
        StringBuilder result = new StringBuilder();

        /* Get header and start of the body section*/
        result.append(getHeader(nameOfChecklist, report));
        result.append(getBodyStartPart());
        result.append(getBodyForm(nameOfChecklist, report, analyseRegression));

        if (showEmptyForm) {
            result.append(getFinalPartOfTheForm());
        } else {
            result.append(getSummaryDataUnderForm(report));
            result.append(getFinalPartOfTheForm());
            result.append(showErrorLog(report));
            result.append(ReportToHtml.getDataFromReport(report));
        }

        /* Footer with service links - Refresh MainConfig, etc. */
        result.append(getServletFooter());
        result.append(getBodyLastPart());

        return result.toString();
    }


    /* Make header with CSS to display tooltip container*/
    private static String getHeader(String nameOfChecklist, Report report) {

        StringBuilder result = new StringBuilder();
        result.append("<!DOCTYPE html>\n");
        result.append("<html>\n");
        result.append("<head>\n");
        result.append("<meta charset=\"UTF-8\">\n");
        result.append("<title>");
        result.append(getTitle(nameOfChecklist, report));
        result.append("</title>\n");
        result.append("<link href=\"lib/css/TIcheck.css\" rel=\"stylesheet\" type=\"text/css\">\n");
        result.append("<link href=\"lib/css/tooltip.css\" rel=\"stylesheet\" type=\"text/css\">\n");
        result.append("<link href=\"lib/css/tableRowsColor.css\" rel=\"stylesheet\" type=\"text/css\">\n");
        result.append("<link rel=\"shortcut icon\" href=\"lib/img/favicon.ico\" type=\"image/x-icon\">");
        result.append("<style>\n");
        result.append("</style>\n");
        result.append("</head>\n");

        return result.toString();
    }

    private static String getTitle(String nameOfChecklist, Report report) {
        StringBuilder result = new StringBuilder();

        result.append((nameOfChecklist != null && nameOfChecklist != "") ? "[" + nameOfChecklist : "");

        result.append(
                (report.getName() != null && report.getName() != "")
                        ? " for " + report.getName() + ":" + report.getIteration() : "");

        result.append(
                (report.getPrevName() != null && report.getPrevName() != "")
                        ? " / " + report.getPrevName() + ":" + report.getPrevIteration() : "");

        result.append((nameOfChecklist != null && nameOfChecklist != "") ? "] - " : "");


        result.append("Automated checklist analyser\n");

        return result.toString();
    }


    /* Display the first part of the body*/
    private static String getBodyStartPart() {
        return "\n\n<body>\n<div align=center>\n";
    }

    /* Display the first part of the body*/
    private static String getBodyForm(
            String checklistRequestName, Report report, boolean analyseRegression) {

        StringBuilder result = new StringBuilder();

        if (!checklistRequestName.equals("")) {
            result.append(
                    String.format("\n<p><h3>Automated <u>%s</u> checklist analysis results for the request:</h3>\n",
                            checklistRequestName));
        } else {
            result.append("\n<p><h3>Automated checklist analyser</h3>\n");
        }


        result.append("\n<div class=\"PON_selector\">\n");

        result.append("\n<form action=\"analyse?action=submit\" method=\"get\" id=\"checklist\">\n");

        result.append("<table border=1>\n");
        result.append("<tr>\n<th width=100px>Request</th><th>Name</th><th>Iteration</th><th>Use query<br/> LIKE%...%</th><th>Regression<br/> check</th></tr>\n");


        /* FORM */
        result.append("\n<tr><td>Actual</td>\n");
        result.append(
                String.format(
                        "\n<td class = \"center\">\n" +
                                "<input\n" +
                                "type=\"text\"\n" +
                                "name=\"pon_name\"\n" +
                                "value = \"%s\"\n" +
                                "placeholder=\"Enter PON or it's part\"\n" +
                                "title=\"Query with &quot;LIKE&quot; or &quot;=&quot; statement will be used depending on &quot;Use LIKE...&quot; checkbox\"\n" +
                                "tabindex=\"1\"\n" +
                                "maxlength=\"30\"\n" +
                                "size=\"20\"\n" +
                                "autofocus/>\n" +
                                "</td>\n",
                        report.getName()));

        result.append(
                String.format(
                        "<td class = \"center\">\n" +
                                "<input\n" +
                                "type=\"text\"\n" +
                                "name=\"pon_iteration\" value=\"%s\"\n" +
                                "placeholder=\"Enter PON's iteration\"\n" +
                                "title=\"Replace with the actual value (_R# will be added for query with =)\"\n" +
                                "maxlength=\"3\"\n" +
                                "tabindex=\"2\"/>\n" +
                                "</td>\n",
                        report.getIteration()));


        result.append(
                String.format(
                        "<td class = \"center\">\n" +
                                "<input\n" +
                                "type=\"checkbox\"\n" +
                                "name=\"use_query_like\"\n" +
                                "%s\n" +
                                "title=\"Query for requested name will be used with LIKE + heading and tailing &#37;\"\n" +
                                "tabindex=\"3\">\n" +
                                "</td>\n",
                        (report.getUseQueryLike() ? "checked" : "")));


        result.append(
                String.format(
                        "<td rowspan = 2 class = \"center\">\n" +
                                "<input\n" +
                                "type=\"checkbox\"\n" +
                                "name=\"regression_check\"\n" +
                                "%s\n" +
                                "title=\"Unset to disable regression check despite of entered data\"\n" +
                                "tabindex=\"20\">\n" +
                                "\n",
                        (analyseRegression ? "checked" : "")));

        result.append("</td>\n");


        result.append("</tr>\n");


        result.append("<tr><td>Previous</td>\n");
        result.append(
                String.format(
                        "<td class = \"center\">\n" +
                                "<input\n" +
                                "type=\"text\"\n" +
                                "name=\"prev_pon_name\"\n" +
                                "value = \"%s\"\n" +
                                "placeholder=\"Enter previous PON/part\"\n" +
                                "title=\"Query with &quot;LIKE&quot; or &quot;=&quot; statement will be used depending on &quot;Use LIKE...&quot; checkbox\"\n" +
                                "tabindex=\"10\"\n" +
                                "maxlength=\"30\"\n" +
                                "size=\"20\"\n" +
                                "</td>\n",
                        report.getPrevName()));

        result.append(
                String.format(
                        "<td class = \"center\">\n" +
                                "<input\n" +
                                "type=\"text\"\n" +
                                "name=\"prev_pon_iteration\" value=\"%s\"\n" +
                                "placeholder=\"Enter previous PON's iteration\"\n" +
                                "title=\"Replace with the actual value (_R# will be added for query with =)\"\n" +
                                "maxlength=\"3\"\n" +
                                "tabindex=\"11\"/>\n" +
                                "</td>\n",
                        report.getPrevIteration()));


        result.append(
                String.format(
                        "<td class = \"center\">\n" +
                                "<input\n" +
                                "type=\"checkbox\"\n" +
                                "name=\"use_query_like_for_prev\"\n" +
                                "%s\n" +
                                "title=\"Query for requested name will be used with LIKE + heading and tailing &#37;\"\n" +
                                "tabindex=\"12\">\n" +
                                "</td>\n",
                        (report.getUseQueryLikeForPrev() ? "checked" : "")));


        result.append("</tr>\n");


        /* Show Checklist's select and Submit button */
        result.append("<tr>\n");
        result.append("<td colspan = 5 align = center>\n");

        //TODO Load Checklists at start of the APPLICATION!!!
        // Validate files

        result.append(
                "Select Checklist " +
                        "<select\n" +
                        "name = \"checklists\"\n" +
                        "form=\"checklist\"\n" +
                        "style=\"width: 120px\"\n" +
                        "tabindex=\"30\">\n");

        for (ChecklistsListEntry entry : ChecklistsList.getInstance().getChecklistsList()) {
            result.append(
                    String.format(
                            "            <option value = \"%s\" %s>%s %s</option>\n",
                            entry.getSimpleFileNameOfChecklist(),
                            (entry.getNameOfChecklist().equals(checklistRequestName) ||

                                    //FIXME - find out why is it false always?!?
                                    (entry.getDefaultFlag() && checklistRequestName.equals("")))
                                    ? "selected" : "",
                            entry.getNameOfChecklist(),
                            entry.getDefaultFlag() ? " (Default)" : ""));
        }
        result.append("</select>\n");


        result.append(
                "<button\n" +
                        "class = \"PON_filter\"\n" +
                        "type=\"submit\"\n" +
                        "style=\"width: 120px\"\n" +
                        "title=\"Enter PON (or it's part) and press button to start TI analysis\"\n" +
                        "tabindex=\"31\">\n" +
                        "Analyse\n" +
                        "</button>\n");

        result.append(
                String.format(
                        "<input %s\n" +
                                "type=\"checkbox\"\n" +
                                "name=\"limit_on_off\"\n" +
                                "title=\"Check if limit setting is affecting results\"\n" +
                                "tabindex=\"32\">\n",
                        report.getLimitControl()?"checked":""));

        boolean limit10 = false;
        boolean limit100 = false;
        boolean limit1000 = false;
        boolean limit10000 = false;

        switch (report.getLimit()) {
            case 10:
                limit10 = true;
                break;
            case 100:
                limit100 = true;
                break;
            case 1000:
                limit1000 = true;
                break;
            case 10000:
                limit10000 = true;
                break;
            default:
                limit100 = true;
        }
        result.append(
                String.format(
                        " \n" +
                                "<select\n" +
                                "name = \"limit\"\n" +
                                "form=\"checklist\"\n" +
                                "style=\"width: 120px\"\n" +
                                "tabindex=\"33\">\n" +
                                "            <option value = \"10\" %s>10</option>\n" +
                                "            <option value = \"100\" %s>100 (Default)</option>\n" +
                                "            <option value = \"1000\" %s>1000</option>\n" +
                                "            <option value = \"10000\" %s>10000</option>\n" +
                                "         </select> Limit request &nbsp;\n",

                        (limit10 ? "selected" : ""),
                        (limit100 ? "selected" : ""),
                        (limit1000 ? "selected" : ""),
                        (limit10000 ? "selected" : "")));




        result.append("</td>\n");
        result.append("</tr>\n");

        return result.toString();
    }


    /* Close form*/
    private static String getFinalPartOfTheForm() {
        String result;
        result = "</table>\n" +
                "</form>\n" +
                "</div>\n" +
                "<br/>\n";
        return result;
    }

    /* Show summary result */
    private static String getSummaryDataUnderForm(Report report) {
        StringBuilder result = new StringBuilder();


        result.append("\n<tr>\n<td colspan = 5 align = center>\n");
        if (checkReportForErrors(report.getSpiderSteps()) ||
                checkReportForErrors(report.getBirtSteps()) ||
                checkReportForErrors(report.getNdsSteps())) {
            result.append("\n<b><font color = red>Summary: Checklist has failed (see the list of failed tests below)</font></b>\n");
        } else {
            result.append("\n<b><font color = green>Summary: Checklist has passed (no failed tests have been detected)</font></b>\n");
        }
        result.append("</tr>\n");
        result.append("\n<tr>\n<td colspan = 5 align = center>\n");

        /* Show time, requests count and date time */
        result.append("\n<div align = left>\n");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        result.append(
                String.format(
                        "Elapsed: %s seconds; Requests Count: %s; Limit control: %s; Request time: %s",
                        report.getElapsedTime(),
                        report.getRequestsCount(),
                        report.getLimitControl()?"ON":"OFF",
                        dateFormat.format(date)));
        result.append("</div>\n");


        result.append("</td>\n");
        result.append("</tr>\n");


        return result.toString();
    }


    private static String showErrorLog(Report report) {

        StringBuilder result = new StringBuilder();
        /*Show error log if it exists*/
        if (report.getLogOfErrors().size() != 0) {
            result.append("<br/>\n<div align = left>\n");
            result.append("<details>\n<summary>\n<u><b>\n<font color = red>SEE THE LOG OF ERRORS WHILE CHECKLIST ANALYSIS</font></b></u>\n</summary>\n");
            result.append(
                    String.format(
                            "\n<div><p>\n<font color=red>, %s</font></p>\n</div>\n",
                            report.getLogOfErrors()
                                    .toString()
                                    .replace("[", "")
                                    .replace("]", "")));
            result.append("</details>\n");
            result.append("</div>\n<br/>\n");
        }
        return result.toString();
    }

    private static boolean checkReportForErrors(List<ChecklistEntry> steps) {
        for (ChecklistEntry entry : steps) {
            if (entry.getResultOfCheckIsNOK()) {
                return true;
            }
        }
        return false;
    }


    /* Display the last part of the body*/
    private static String getServletFooter() {
        StringBuilder result = new StringBuilder();
        result.append("<br/>\n<div>\n");
        result.append("<table border = 0 width=\"95%\">\n");


        result.append("<tr>\n");
        result.append("<td colspan = 6><hr align=\"center\" width=\"100%\" size=\"2\" color=\"#ff0000\" /></td>\n");
        result.append("</tr>\n");

        result.append("<tr>\n");
        result.append("<td width=\"20%\"> ADMIN AREA </td>\n");

        result.append("<td width=\"20%\">\n");
        result.append("<a href=\"help.html\" target=\"_blank\">HELP</a>\n");
        result.append("</td>\n");

        //Todo Use AJAX to process servlet in silent mode
        result.append("<td width=\"20%\">\n");
        result.append("<a href=\"ServletReadMainConfig\" target=\"_blank\">RELOAD MAIN CONFIG</a>\n");
        result.append("</td>\n");

        //Todo Use AJAX to process servlet in silent mode
        result.append("<td width=\"20%\">\n");
        result.append("<a href=\"ServletReadChecklistsList\" target=\"_blank\">RELOAD LIST OF CHECKLISTS</a>\n");
        result.append("</td>\n");

        result.append("<td width=\"20%\">\n");
        result.append("<a href=\"http://172.30.136.166:8889/\">IDE [http://172.30.136.166:8889/]</a>\n");
        result.append("</td>\n");

        result.append("<td width=\"20%\">\n");
        result.append("<a href=\"http://172.30.136.166:8080/TIcheck/\">[http://172.30.136.166:8080/TIcheck/]</a>\n");
        result.append("</td>\n");

        result.append("</table>\n");
        result.append("</div>\n");
        return result.toString();

    }

    /* Display the last part of the body*/
    private static String getBodyLastPart() {
        return "</div>\n</body>\n</html>";
    }
}
