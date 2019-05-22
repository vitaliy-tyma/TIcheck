package com.luxoft.falcon.controller;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.Checklist;
import com.luxoft.falcon.model.ChecklistEntry;
import com.luxoft.falcon.model.Report;
import com.luxoft.falcon.service.*;
import com.luxoft.falcon.util.ReadXML;
import com.luxoft.falcon.util.ReportToHtml;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Servlet is launched from web-browser's form and starts processes of checklists analysis
 */

//FIXME Refactor methods to return only objects with data
// code to show objects it must be separated
// refactor project with MVC
// check input with case of letters


//TODO - Testing section is not used!!!
// Z:\Test_DB\release-7.2_MAN\TBAA\001\iDb
@Slf4j
public class ServletAnalyse extends HttpServlet {
    private static MainConfig mainConfig = MainConfig.getInstance();
    private static int requestsCount = 0;
    private static boolean analyseRegression = false;


    @Override
    public void init() throws ServletException {
        super.init();
    }


    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {


        log.info("*********************** NEW REQUEST STARTED ** ServletAnalyse.doGet() ****************************************");
        StringBuilder result = new StringBuilder();
        Checklist checklist;
        Report report = new Report();

        final String ponName =
                httpServletRequest.getParameter(mainConfig.getPON_NAME_REQUEST());
        httpServletRequest.setAttribute(mainConfig.getPON_NAME_REQUEST(), ponName);

        final String ponIteration =
                httpServletRequest.getParameter(mainConfig.getPON_ITERATION_REQUEST());
        httpServletRequest.setAttribute(mainConfig.getPON_ITERATION_REQUEST(), ponIteration);

        final String useQueryLike =
                httpServletRequest.getParameter(mainConfig.getUSE_QUERY_LIKE_REQUEST());
        httpServletRequest.setAttribute(mainConfig.getUSE_QUERY_LIKE_REQUEST(), useQueryLike);

        final String checklistRequestName =
                httpServletRequest.getParameter(mainConfig.getCHECKLISTS_REQUEST());
        httpServletRequest.setAttribute(mainConfig.getCHECKLISTS_REQUEST(), checklistRequestName);

        final String limitValue =
                httpServletRequest.getParameter(mainConfig.getQUERY_LIMIT());
        httpServletRequest.setAttribute(mainConfig.getQUERY_LIMIT(), limitValue);


        final String prevPonName =
                httpServletRequest.getParameter(mainConfig.getPON_NAME_PREV_REQUEST());
        httpServletRequest.setAttribute(mainConfig.getPON_NAME_PREV_REQUEST(), prevPonName);

        final String prevPonIteration =
                httpServletRequest.getParameter(mainConfig.getPON_ITERATION_PREV_REQUEST());
        httpServletRequest.setAttribute(mainConfig.getPON_ITERATION_PREV_REQUEST(), prevPonIteration);

        final String useQueryLikeForPrev =
                httpServletRequest.getParameter(mainConfig.getUSE_QUERY_LIKE_PREV_REQUEST());
        httpServletRequest.setAttribute(mainConfig.getUSE_QUERY_LIKE_PREV_REQUEST(), useQueryLikeForPrev);

        final String regressionCheck =
                httpServletRequest.getParameter(mainConfig.getCHECKLISTS_REGRESSION());
        httpServletRequest.setAttribute(mainConfig.getCHECKLISTS_REGRESSION(), regressionCheck);


        long start = System.currentTimeMillis();


        log.info("****************************************** CHECKLIST IS BEING PROCESSED *****************************************");
        try {

//TODO - Load all checklist at the start (or on special request) and
// only choose the appropriate from the memory at this point
// to be developed later
            checklist = ReadXML.readChecklistFromFile(checklistRequestName);


            log.debug(String.format(
                    "******* Processing checklist %s with the request: name = %s;" +
                            " iteration = %s; useQueryLike = %s",
                    checklistRequestName,
                    ponName,
                    ponIteration,
                    useQueryLike));

            /* Get header and start of the body section*/
            result.append(getHeader());
            result.append(getBodyStartPart());


            report.setName(ponName);
            report.setIteration(ponIteration);
            report.setLimit(limitValue);


            if (useQueryLike == null) {
                report.setUseQueryLike(Boolean.FALSE);
            } else {
                if (useQueryLike.equals("on")) {
                    report.setUseQueryLike(Boolean.TRUE);
                } else {
                    report.setUseQueryLike(Boolean.FALSE);
                }
            }

            if (regressionCheck != null) {
                if (regressionCheck.equals("on") && ((prevPonName != "") && (prevPonIteration != ""))) {
                    analyseRegression = true;
                }
            }


            if (analyseRegression) {
                report.setPrevName(prevPonName);
                report.setPrevIteration(prevPonIteration);
                if (useQueryLikeForPrev == null) {
                    report.setUseQueryLikeForPrev(Boolean.FALSE);
                } else {
                    if (useQueryLikeForPrev.equals("on")) {
                        report.setUseQueryLikeForPrev(Boolean.TRUE);
                    } else {
                        report.setUseQueryLikeForPrev(Boolean.FALSE);
                    }
                }
            }

            /* Choose the mode;
             - SingleThread is for debug!!!
             - MultithreadedMode - is for Multithreading (but there is no acceleration detected)*/
            boolean debugMode = true;
            if (debugMode) {
                startSingleThreadedMode(checklist, report, analyseRegression);
            } else {
                //Made just as an example - may be useful for very big requests!!
                startMultithreadedMode(checklist, report, analyseRegression);
            }


            log.info("****************************************** CHECKLIST PROCESSING HAS FINISHED *****************************************");

            /*calculate statistics - time and requests*/
            long end = System.currentTimeMillis();
            float sec = (end - start) / 1000F;
            log.info(String.format(
                    "Elapsed: %s seconds; Requests Count: %s.",
                    sec,
                    requestsCount));
            report.setElapsedTime(sec);
            report.setRequestsCount(requestsCount);







            /* OUTPUT of checklist report*/
            result.append(getBodyFirstPart(checklistRequestName, report, analyseRegression));
            result.append(ReportToHtml.getDataFromReport(report));
            result.append(getBodyLastPart());





            /* Error handling in case of input parameters problems*/
        } catch (Exception e) {
            report.addLogOfErrors(e.getMessage());
            result.append("Error while processing<br/>\n");
            result.append(report.getLogOfErrors());
        }

        /* Reset all variables to be ready for the next cycle */
        analyseRegression = false;

        /* Send reply after analysis */
        httpServletResponse.getWriter().print(result.toString());
    }


    // Use one thread to process different data sources - useful for debug!!!
    private void startSingleThreadedMode(Checklist checklist, Report report, boolean analyseRegression) {

        /* PROCESS SPIDER ERRORS*/
        if (checklist.getSpiderSteps().size() != 0) {
            ServiceAnalyseSpiderMt analyseSpiderMt = new ServiceAnalyseSpiderMt(checklist, report, analyseRegression);
            requestsCount += analyseSpiderMt.processSpiderChecklist(checklist, report, analyseRegression);
        }
        /* PROCESS BIRT ERRORS*/
        if (checklist.getBirtSteps().size() != 0) {
            ServiceAnalyseBirtMt analyseBirtMt = new ServiceAnalyseBirtMt(checklist, report, analyseRegression);
            requestsCount += analyseBirtMt.processBirtChecklist(checklist, report, analyseRegression);
        }
        /* PROCESS NDS ERRORS*/
        if (checklist.getNdsSteps().size() != 0) {
            ServiceAnalyseNdsMt analyseNdsMt = new ServiceAnalyseNdsMt(checklist, report, analyseRegression);
            requestsCount += analyseNdsMt.processNdsChecklist(checklist, report, analyseRegression);
        }
    }


    // Use several threads to process different data sources
    private static void startMultithreadedMode(Checklist checklist, Report report, boolean analyseRegression) {

        try {

            Thread analyseSpiderMt = new ServiceAnalyseSpiderMt(checklist, report, analyseRegression);
            Thread analyseBirtMt = new ServiceAnalyseBirtMt(checklist, report, analyseRegression);
            Thread analyseNdsMt = new ServiceAnalyseNdsMt(checklist, report, analyseRegression);

            /* PROCESS SPIDER ERRORS*/
            if (checklist.getSpiderSteps().size() != 0) {
                analyseSpiderMt.start();
            }

            /* PROCESS BIRT ERRORS*/
            if (checklist.getBirtSteps().size() != 0) {
                analyseBirtMt.start();
            }

            /* PROCESS NDS ERRORS*/
            if (checklist.getNdsSteps().size() != 0) {
                analyseNdsMt.start();
            }

            analyseSpiderMt.join();
            analyseBirtMt.join();
            analyseNdsMt.join();

            report.setSpiderSteps(((ServiceAnalyseSpiderMt) analyseSpiderMt).getSteps());
            requestsCount += ((ServiceAnalyseSpiderMt) analyseSpiderMt).getRequestsCount();

            report.setBirtSteps(((ServiceAnalyseBirtMt) analyseBirtMt).getSteps());
            requestsCount += ((ServiceAnalyseBirtMt) analyseBirtMt).getRequestsCount();

            report.setNdsSteps(((ServiceAnalyseNdsMt) analyseNdsMt).getSteps());
            requestsCount += ((ServiceAnalyseNdsMt) analyseNdsMt).getRequestsCount();
        } catch (Exception e) {
            report.addLogOfErrors(e.getMessage());
        }
    }


    /* Display the first part of the body*/
    private String getBodyFirstPart(String checklistRequestName, Report report, boolean analyseRegression) {

        StringBuilder result = new StringBuilder();

        result.append(
                String.format("<p><h3>Automated <u>%s</u> checklist analysis results for the request:</h3>\n",
                        checklistRequestName));
        result.append("<table border=1>\n");
        result.append("<tr><th>Request</th><th>Name</th><th>Iteration</th><th>Query LIKE%...%</th><th>Limit</th></tr>\n");
        result.append("<tr><td>Actual</td>\n");

        result.append(
                String.format(
                        "<td><b>%s</b></td><td><b>%s</b></td><td><b>%s</b></td><td><b>%s</b></td></p>\n",
                        report.getName(),
                        report.getIteration(),
                        report.getUseQueryLike(),
                        report.getLimit()));
        result.append("</tr>\n");


        if (analyseRegression) {
            result.append("<tr><td>Previous</td>\n");
            result.append(
                    String.format(
                            "<td><b>%s</b></td><td><b>%s</b></td><td><b>%s</b></td><td><b>%s</b></td></p>\n",
                            report.getPrevName(),
                            report.getPrevIteration(),
                            report.getUseQueryLikeForPrev(),
                            report.getLimit()));
            result.append("</tr>\n");
        }

//        result.append("<object type=\"text/html\" data=\"index.jsp\"></object>\n"); //works but in reduced window
//        result.append("<object type=\"text/html\" data=\"index.jsp\" style=\"width:100%; height: 50%\"></object>\n"); //works well but calls new requests inside of the new frame
// And not possible to set parameters!
//The form must be implemented inside of the page!!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

//        result.append("\n\n<div id=\"includedContent\"></div>\n\n"); //not work with jQuery - need to be corrected
//        result.append("<link rel=\"import\" href=\"index.jsp\">\n"); //doesn't work
//        result.append("\n\n<iframe src=\"index.jsp\" frameborder=\"0\"></iframe>\n\n");//works, nut iFrame is outdated


//doesn't work
//        result.append("\n\n<div id =\"content\" ></div>");
//        result.append("\n<script>");
//        result.append("\nfunction load_home(){");
//        result.append("\ndocument.getElementById(\"content\").innerHTML='<object type=\"text/html\" data=\"index.jsp\" style=\"width:100%; height: 100%\";></object>';");
//        result.append("\n}");
//        result.append("\n</script>");


        /* Show summary result */
        result.append("<tr><td colspan = 5 align = center>");
        if (checkReportForErrors(report.getSpiderSteps()) ||
                checkReportForErrors(report.getBirtSteps()) ||
                checkReportForErrors(report.getNdsSteps())) {
            result.append("<b><font color = red>Summary: Checklist has failed (see the list of failed tests below)</font></b>");
        } else {
            result.append("<b><font color = green>Summary: Checklist has passed (no failed tests have been detected)</font></b>");
        }
        result.append("<br/>");

        /* Show time, requests count and date time */
        result.append("<br/><div align = left>");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        result.append(
                String.format(
                        "Elapsed: %s seconds; Requests Count: %s; Request Time is: %s",
                        report.getElapsedTime(),
                        report.getRequestsCount(),
                        dateFormat.format(date)));
        result.append("</div>");


        /*Show error log if it exists*/
        if (report.getLogOfErrors().size() != 0) {
            result.append("<br/><div align = left>");
            result.append("<details><summary><u><b>See log of errors while checklist analysis</b></u></summary>\n");
            result.append(
                    String.format(
                            "<div><p><font color=red>%s</font></p></div>\n",
                            report.getLogOfErrors().toString()));
            result.append("</b></details>\n");
            result.append("</div>");
        }


        result.append("</td></tr>");
        result.append("</table>\n");
        result.append("<br/>\n");


        return result.toString();
    }


    private boolean checkReportForErrors(List<ChecklistEntry> steps) {
        for (ChecklistEntry entry : steps) {
            if (entry.getResultOfCheckIsNOK()) {
                return true;
            }
        }
        return false;
    }

    /* Display the first part of the body*/
    private String getBodyStartPart() {
        return "\n\n<body >\n<div align=center>\n";
        //onload="load_home()"
    }


    /* Display the last part of the body*/
    private String getBodyLastPart() {
        return "</div>\n</body>\n";
    }


    /* Make header with CSS to display tooltip container*/
    private static String getHeader() {

        String header = "<head>\n" +

//                "\n" +
//                "<script src = \"jquery.js\"></script >\n" +
//                "<script >\n" +
//                "$(function() {\n" +
//                "$(\"#includedContent\").load(\"index.jsp\");\n" +
//                "});\n" +
//                "</script >\n" +


                "\n" +
                "<style>\n" +
//TODO Locate CSS in separate file - it is not defined yet
// Cannot locate CSS within IDE (must be checked in TOMCAT)
//                       "<link href=\"lib/css/tooltip.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
//                       "<link href=\"/css/tooltip.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
//                       "<link href=\"css/tableRowsColor.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
//                "<%@include file=\"lib/css/tooltip.css\"%>\n" +
//                "<%@include file=\"lib/css/tableRowsColor.css\"%>\n" +


                "/* Tooltip container */\n" +

                ".tooltip_for_query {\n" +
                "  position: relative;\n" +
                "  display: inline-block;\n" +
                "  border-bottom: 1px dotted black; /* If you want dots under the hoverable text */\n" +
                "}\n" +
                ".tooltip_for_name {\n" +
                "  position: relative;\n" +
                "  display: inline-block;\n" +
                "  border-bottom: 1px dotted black; /* If you want dots under the hoverable text */\n" +
                "}\n" +

                "\n" +
                "/* Tooltip text */\n" +
                ".tooltip_for_query .tooltiptext {\n" +
                "  visibility: hidden;\n" +
                "  width: 500px;\n" +
                "  bottom: 100%;\n" +
                "  left: 50%; \n" +
                "  margin-left: -400px; /* Use shift, to center the tooltip */" +
                "" +
                "  background-color: black;\n" +
                "  color: #fff;\n" +
                "  text-align: center;\n" +
                "  padding: 5px 0;\n" +
                "  border-radius: 6px;\n" +
                " \n" +
                "  /* Position the tooltip text - see examples below! */\n" +
                "  position: absolute;\n" +
                "  z-index: 1;\n" +
                "}\n" +


                "\n" +
                "/* Tooltip text */\n" +
                ".tooltip_for_name .tooltiptext {\n" +
                "  visibility: hidden;\n" +
                "  width: 200px;\n" +
                "  bottom: 100%;\n" +
                "  left: 50%; \n" +
                "  margin-left: -100px; /* Use shift, to center the tooltip */" +
                "  background-color: black;\n" +
                "  color: #fff;\n" +
                "  text-align: center;\n" +
                "  padding: 5px 0;\n" +
                "  border-radius: 6px;\n" +
                " \n" +
                "  /* Position the tooltip text - see examples below! */\n" +
                "  position: absolute;\n" +
                "  z-index: 1;\n" +
                "}\n" +
                "" +

                "\n" +
                "/* Show the tooltip text when you mouse over the tooltip container */\n" +
                ".tooltip_for_query:hover .tooltiptext {\n" +
                "  visibility: visible;\n" +
                "}" +

                "\n" +
                "/* Show the tooltip text when you mouse over the tooltip container */\n" +
                ".tooltip_for_name:hover .tooltiptext {\n" +
                "  visibility: visible;\n" +
                "}" +

                "\n" +
                "table tr#ROW_0  {background-color:; color:;}\n" +
                "table tr#ROW_WHITE  {background-color:white;}\n" +
                "table tr#ROW_WHITE2  {background-color:white; color:black;}\n" +
                "table tr#ROW_GRAY  {background-color:#e6e6e6; color:black;}\n" +

                "</style>\n" +
                "</head>\n";
        return header;
    }


}