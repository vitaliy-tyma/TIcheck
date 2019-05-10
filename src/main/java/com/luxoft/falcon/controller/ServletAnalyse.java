package com.luxoft.falcon.controller;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.Checklist;
import com.luxoft.falcon.model.Report;
import com.luxoft.falcon.service.ServiceAnalyseBirt;
import com.luxoft.falcon.service.ServiceAnalyseSpider;
import com.luxoft.falcon.util.ChecklistToHtml;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet is launched from web-browser's form and starts processes of checklists analysis
 */
@Slf4j
public class ServletAnalyse extends HttpServlet {

    private Checklist checklist;
    private Report report;
    private Boolean analyseRegression = Boolean.FALSE;


    @Override
    public void init() throws ServletException {
        super.init();
//        Loader loader = new Loader();
//        loader.init();
    }


    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {


        log.info("*********************** NEW REQUEST STARTED ** ServletAnalyse.doGet() ****************************************");
        StringBuilder result = new StringBuilder();

        final String ponName =
                httpServletRequest.getParameter(MainConfig.getPON_NAME_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getPON_NAME_REQUEST_PARAMETER_VALUE(), ponName);

        final String ponIteration =
                httpServletRequest.getParameter(MainConfig.getPON_ITERATION_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getPON_ITERATION_REQUEST_PARAMETER_VALUE(), ponIteration);

        final String autocompletePon =
                httpServletRequest.getParameter(MainConfig.getAUTOCOMPLETE_PON_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getAUTOCOMPLETE_PON_REQUEST_PARAMETER_VALUE(), autocompletePon);

        final String checklistRequestName =
                httpServletRequest.getParameter(MainConfig.getCHECKLISTS_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getCHECKLISTS_REQUEST_PARAMETER_VALUE(), checklistRequestName);

        final String limitValue =
                httpServletRequest.getParameter(MainConfig.getQUERY_LIMIT_KEY());
        httpServletRequest.setAttribute(MainConfig.getQUERY_LIMIT_VALUE(), limitValue);


        final String prevPonName =
                httpServletRequest.getParameter(MainConfig.getPON_NAME_PREV_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getPON_NAME_PREV_REQUEST_PARAMETER_VALUE(), prevPonName);

        final String prevPonIteration =
                httpServletRequest.getParameter(MainConfig.getPON_ITERATION_PREV_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getPON_ITERATION_PREV_REQUEST_PARAMETER_VALUE(), prevPonIteration);

        final String autocompletePrevPon =
                httpServletRequest.getParameter(MainConfig.getAUTOCOMPLETE_PON_PREV_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getAUTOCOMPLETE_PON_PREV_REQUEST_PARAMETER_VALUE(), autocompletePrevPon);

        final String regressionCheck =
                httpServletRequest.getParameter(MainConfig.getCHECKLISTS_REGRESSION_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getCHECKLISTS_REGRESSION_PARAMETER_VALUE(), regressionCheck);


        long start = System.currentTimeMillis();
        int requestsCount = 0;
        log.info("****************************************** CHECKLIST IS BEING PROCESSED *****************************************");

        // Load checklist by it's name - to be developed later
        checklist = new Checklist(checklistRequestName);


        report = new Report();

        log.debug(String.format(
                "******* Processing checklist %s with the request: name = %s;" +
                        " iteration = %s; autocomplete = %s",
                checklistRequestName,
                ponName,
                ponIteration,
                autocompletePon));

        /* Get header and start of the body section*/
        result.append(getHeader());
        result.append(getBodyStartPart());


        try {
            report.setName(ponName);
            report.setIteration(ponIteration);
            report.setLimit(limitValue);


            if (autocompletePon == null) {
                report.setAutocomplete(Boolean.FALSE);
            } else {
                if (autocompletePon.equals("on")) {
                    report.setAutocomplete(Boolean.TRUE);
                } else {
                    report.setAutocomplete(Boolean.FALSE);
                }
            }

            if (regressionCheck != null) {
                if (regressionCheck.equals("on") && ((prevPonName != "") && (prevPonIteration != ""))) {
                    analyseRegression = Boolean.TRUE;
                }
            }

            if (analyseRegression) {
                report.setPrevName(prevPonName);
                report.setPrevIteration(prevPonIteration);
                if (autocompletePrevPon == null) {
                    report.setPrevAutocomplete(Boolean.FALSE);
                } else {
                    if (autocompletePrevPon.equals("on")) {
                        report.setPrevAutocomplete(Boolean.TRUE);
                    } else {
                        report.setPrevAutocomplete(Boolean.FALSE);
                    }
                }
            }


            result.append(getBodyFirstPart(checklistRequestName, checklist, report));




            /* PROCESS SPIDER ERRORS*/
            requestsCount += ServiceAnalyseSpider.processSpiderChecklist(checklist, report, analyseRegression);
            log.debug(String.format(
                    "********************************* PROCESSING SPIDER of PON {} HAS FINISHED ******************"),
                    report.getName());




            /* PROCESS BIRT ERRORS*/
            requestsCount += ServiceAnalyseBirt.processBirtChecklist(checklist, report, analyseRegression);
            log.debug(String.format(
                    "****************************** PROCESSING BIRT of PON {} HAS FINISHED ******************"),
                    report.getName());


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
            result.append(ChecklistToHtml.getDataFromReport(report));


            log.info("****************************************** CHECKLIST PROCESSING IS FINISHED *****************************************");

            /* Error handling in case of input parameters problems*/
        } catch (Exception e) {
            result.append("Error while input processing<br/>");
            result.append(report.getLogOfErrors());
            report.addLogOfErrors(e.getMessage());
        }

        /* Get last part of the body section*/
        result.append(getBodyLastPart());


        /* Reply after analysis */
        analyseRegression = Boolean.FALSE;
        httpServletResponse.getWriter().print(result.toString());
    }









    /* Display the first part of the body*/
    private String getBodyFirstPart(String checklistRequestName, Checklist checklist, Report report) {

        StringBuilder result = new StringBuilder();


        result.append(
                String.format("<p><h3>Automated %s checklist analysis results for the request:</h3>\n",
                        checklistRequestName));
        result.append("<table border=1>");
        result.append("<tr><th>Request</th><th>Name</th><th>Iteration</th><th>Autocomplete</th><th>Limit</th></tr>\n");
        result.append("<tr><td>Actual</td>\n");

        result.append(
                String.format(
                        "<td><b>%s</b></td><td><b>%s</b></td><td><b>%s</b></td><td><b>%s</b></td></p>\n",
                        report.getName(),
                        report.getIteration(),
                        report.getAutocomplete(),
                        report.getLimit()));
        result.append("</tr>\n");


        if (analyseRegression) {
            result.append("<tr><td>Previous</td>");
            result.append(
                    String.format(
                            "<td><b>%s</b></td><td><b>%s</b></td><td><b>%s</b></td><td><b>%s</b></td></p>\n",
                            report.getPrevName(),
                            report.getPrevIteration(),
                            report.getPrevAutocomplete(),
                            report.getLimit()));
            result.append("</tr>\n");
        }
        result.append("</table>\n");
        result.append("<br/>");


        return result.toString();
    }


    /* Display the last part of the body*/
    private String getBodyLastPart() {
        StringBuilder result = new StringBuilder();
        result.append("</div>");
        result.append("</body>");
        return result.toString();
    }

    /* Display the last part of the body*/
    private String getBodyStartPart() {
        StringBuilder result = new StringBuilder();
        result.append("<body>\n");
        result.append("<div align=center>\n");
        return result.toString();
    }


    /* Make header with CSS to display tooltip container*/
    private static String getHeader() {

        String header = "<head>" +
                "<style>\n" +

//                "<link href=\"/lib/css/tooltip.css?v=8\" rel=\"stylesheet\" type=\"text/css\">" +
//Cannot locate CSS within TOMCAT and IDE

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