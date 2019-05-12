package com.luxoft.falcon.controller;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.Checklist;
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

/**
 * Servlet is launched from web-browser's form and starts processes of checklists analysis
 */

//FIXME Refactor methods to return only objects with data
// - code to objects show it must be separated

@Slf4j
public class ServletAnalyse extends HttpServlet {

    private Checklist checklist;
    private Report report;
    private int requestsCount = 0;



    @Override
    public void init() throws ServletException {
        super.init();
        log.info("Init ServletAnalyse !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        Loader loader = new Loader();
//        loader.init();
    }


    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {


        log.info("*********************** NEW REQUEST STARTED ** ServletAnalyse.doGet() ****************************************");
        StringBuilder result = new StringBuilder();

        final String ponName =
                httpServletRequest.getParameter(MainConfig.getPON_NAME_REQUEST());
        httpServletRequest.setAttribute(MainConfig.getPON_NAME_REQUEST(), ponName);

        final String ponIteration =
                httpServletRequest.getParameter(MainConfig.getPON_ITERATION_REQUEST());
        httpServletRequest.setAttribute(MainConfig.getPON_ITERATION_REQUEST(), ponIteration);

        final String useQueryLike =
                httpServletRequest.getParameter(MainConfig.getUSE_QUERY_LIKE_REQUEST());
        httpServletRequest.setAttribute(MainConfig.getUSE_QUERY_LIKE_REQUEST(), useQueryLike);

        final String checklistRequestName =
                httpServletRequest.getParameter(MainConfig.getCHECKLISTS_REQUEST());
        httpServletRequest.setAttribute(MainConfig.getCHECKLISTS_REQUEST(), checklistRequestName);

        final String limitValue =
                httpServletRequest.getParameter(MainConfig.getQUERY_LIMIT());
        httpServletRequest.setAttribute(MainConfig.getQUERY_LIMIT(), limitValue);


        final String prevPonName =
                httpServletRequest.getParameter(MainConfig.getPON_NAME_PREV_REQUEST());
        httpServletRequest.setAttribute(MainConfig.getPON_NAME_PREV_REQUEST(), prevPonName);

        final String prevPonIteration =
                httpServletRequest.getParameter(MainConfig.getPON_ITERATION_PREV_REQUEST());
        httpServletRequest.setAttribute(MainConfig.getPON_ITERATION_PREV_REQUEST(), prevPonIteration);

        final String useQueryLikeForPrev =
                httpServletRequest.getParameter(MainConfig.getUSE_QUERY_LIKE_PREV_REQUEST());
        httpServletRequest.setAttribute(MainConfig.getUSE_QUERY_LIKE_PREV_REQUEST(), useQueryLikeForPrev);

        final String regressionCheck =
                httpServletRequest.getParameter(MainConfig.getCHECKLISTS_REGRESSION());
        httpServletRequest.setAttribute(MainConfig.getCHECKLISTS_REGRESSION(), regressionCheck);


        long start = System.currentTimeMillis();

        boolean analyseRegression = false;
        log.info("****************************************** CHECKLIST IS BEING PROCESSED *****************************************");
        try {

            // Load checklist by it's name - to be developed later
            checklist = ReadXML.readChecklistFromFile(checklistRequestName);




            report = new Report();

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





            // Use several threads to process different data sources
            Thread analyseSpiderMt = new ServiceAnalyseSpiderMt(checklist, report, analyseRegression);
            Thread analyseBirtMt = new ServiceAnalyseBirtMt(checklist, report, analyseRegression);
            Thread analyseNdsMt = new ServiceAnalyseNdsMt(checklist, report, analyseRegression);

            /* PROCESS SPIDER ERRORS*/
            if (checklist.getSpiderSteps().size() != 0) {
                analyseSpiderMt.start();
//                requestsCount += ServiceAnalyseSpider.processSpiderChecklist(checklist, report, analyseRegression);

            }




            /* PROCESS BIRT ERRORS*/
            if (checklist.getBirtSteps().size() != 0) {
                analyseBirtMt.start();
//                requestsCount += ServiceAnalyseBirt.processBirtChecklist(checklist, report, analyseRegression);

            }


            /* PROCESS NDS ERRORS*/
            if (checklist.getNdsSteps().size() != 0) {
                analyseNdsMt.start();
            }


            try {
                analyseSpiderMt.join();
                log.debug(String.format(
                        "********************************* PROCESSING SPIDER of PON {} HAS FINISHED ******************"),
                        report.getName());

                analyseBirtMt.join();
                log.debug(String.format(
                        "****************************** PROCESSING BIRT of PON {} HAS FINISHED ******************"),
                        report.getName());

                analyseNdsMt.join();
                log.debug(String.format(
                        "****************************** PROCESSING NDS of PON {} HAS FINISHED ******************"),
                        report.getName());

                report.setSpiderSteps(((ServiceAnalyseSpiderMt) analyseSpiderMt).getSteps());
                requestsCount += ((ServiceAnalyseSpiderMt) analyseSpiderMt).getRequestsCount();

                report.setBirtSteps(((ServiceAnalyseBirtMt) analyseBirtMt).getSteps());
                requestsCount += ((ServiceAnalyseBirtMt) analyseBirtMt).getRequestsCount();

                report.setNdsSteps(((ServiceAnalyseNdsMt) analyseNdsMt).getSteps());
                requestsCount += ((ServiceAnalyseNdsMt) analyseNdsMt).getRequestsCount();
            }
            catch (Exception e){
                report.addLogOfErrors(e.getMessage());
            }

            log.info("****************************************** CHECKLIST PROCESSING IS FINISHED *****************************************");

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





        /* Send reply after analysis */
        httpServletResponse.getWriter().print(result.toString());
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
        result.append("</table>\n");
        result.append("<br/>\n");


        return result.toString();
    }


    /* Display the last part of the body*/
    private String getBodyLastPart() {
        return "</div>\n</body>\n";
    }

    /* Display the last part of the body*/
    private String getBodyStartPart() {
        return "<body>\n<div align=center>\n";
    }


    /* Make header with CSS to display tooltip container*/
    private static String getHeader() {

        String header = "<head>\n" +
                "<style>\n" +
//TODO Locate CSS in file - place is not defined
                //"<link href=\"/lib/css/tooltip.css\" rel=\"stylesheet\" type=\"text/css\">" +
//Cannot locate CSS within IDE (must be checked in TOMCAT)

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

                "</style>" +
                "</head>\n";
        return header;
    }


}