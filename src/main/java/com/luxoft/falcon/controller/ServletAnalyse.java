package com.luxoft.falcon.controller;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.Checklist;
import com.luxoft.falcon.model.Report;
import com.luxoft.falcon.service.ServiceAnalyseBirt;
import com.luxoft.falcon.service.ServiceAnalyseSpider;
import com.luxoft.falcon.util.ChecklistMonitor;
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
        log.info("***********************************************************************************************************");
        log.info("**************************************************************************************************************");
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


        if ((prevPonName != "") && (prevPonIteration != "")) {
            analyseRegression = Boolean.TRUE;
        }




        /* Check the name of checklist. If = TI proceed*/
        log.info("****************************************** CHECKLIST IS BEING PROCESSED *****************************************");
//            result.append("\nCHECKLIST IS BEING PROCESSED\n");
        checklist = new Checklist(checklistRequestName);// Load checklist by it's name - to be developed later


        report = new Report();

        log.debug(String.format(
                "******* Processing checklist %s with the request: name = %s;" +
                        " iteration = %s; autocomplete = %s",
                checklistRequestName,
                ponName,
                ponIteration,
                autocompletePon));


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









        /* Read all sections to define check steps - fill in Keys*/
        result.append(getHeader());
        result.append(getBodyFirstPart(checklistRequestName, checklist, report));




        /* PROCESS SPIDER ERRORS*/
        ServiceAnalyseSpider.processSpiderChecklist(checklist, report, analyseRegression);
        log.info(String.format(
                "********************************* PROCESSING SPIDER of PON {} HAS FINISHED ******************"),
                report.getName());




        /* PROCESS BIRT ERRORS*/
        ServiceAnalyseBirt.processBirtChecklist(checklist, report, analyseRegression);
        log.info(String.format(
                "****************************** PROCESSING BIRT of PON {} HAS FINISHED ******************"),
                report.getName());









        /* OUTPUT of checklist report*/
        result.append(ChecklistMonitor.getDataFromreport(report));

        result.append(getBodyLastPart());





        /* To be sent as reply after analysis only*/
        httpServletResponse.getWriter().print(result.toString());
    }


    /* Display the first part of the body*/
    private String getBodyFirstPart(String checklistRequestName, Checklist checklist, Report report) {

        StringBuilder result = new StringBuilder();

        result.append("<body>\n");
        result.append("<div align=center>\n");
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


    /* Make header with CSS to display tooltip container*/
    private static String getHeader() {

        String header = "<head>" +
                "<style>\n" +
//                "<link href=\"/lib/css/tooltip.css?v=8\" rel=\"stylesheet\" type=\"text/css\">" +

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