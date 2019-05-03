package com.luxoft.falcon.controller;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.ChecklistTI;
import com.luxoft.falcon.model.ChecklistTiEntry;
import com.luxoft.falcon.model.Pon;
import com.luxoft.falcon.model.ErrorRecord;
import com.luxoft.falcon.service.ServiceAnalyseBirt;
import com.luxoft.falcon.service.ServiceAnalyseSpider;
import com.luxoft.falcon.util.ChecklistMonitor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

/**
 * Servlet is launched from web-browser's form and starts processes of checklists analysis
 */
@Slf4j
public class ServletAnalyse extends HttpServlet {
//    private Pon pon = new Pon();


    @Override
    public void init() throws ServletException {
        super.init();
//        Loader loader = new Loader();
//        loader.init();
    }


    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {

        Pon pon = new Pon();

        log.info("*********************** NEW REQUEST STARTED ** ServletAnalyse.doGet() ****************************************");
        log.info("***********************************************************************************************************");
        log.info("**************************************************************************************************************");


        final String ponName =
                httpServletRequest.getParameter(MainConfig.getPON_NAME_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getPON_NAME_REQUEST_PARAMETER_VALUE(), ponName);

        final String ponIteration =
                httpServletRequest.getParameter(MainConfig.getPON_ITERATION_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getPON_ITERATION_REQUEST_PARAMETER_KEY(), ponIteration);

        final String autocompletePon =
                httpServletRequest.getParameter(MainConfig.getAUTOCOMPLETE_PON_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getAUTOCOMPLETE_PON_REQUEST_PARAMETER_KEY(), autocompletePon);

        final String setChecklist =
                httpServletRequest.getParameter(MainConfig.getCHECKLISTS_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getCHECKLISTS_REQUEST_PARAMETER_KEY(), setChecklist);

        log.info(
                String.format(
                        "******* Processing checklistTI %s with the request: name = %s;" +
                                " iteration = %s; autocomplete = %s",
                        setChecklist,
                        ponName,
                        ponIteration,
                        autocompletePon));


        pon.setName(ponName);
        pon.setIteration(Integer.parseInt(ponIteration));
        pon.setChecklistName(setChecklist);

        if (autocompletePon == null) {
            pon.setAutocomplete(false);
        } else {
            //.equals("on")
            pon.setAutocomplete(true);
        }






        /* Read all sections to define check steps - fill in Keys*/
        /* !!!!!!!!!!!!!!!!!!!!!*/
        StringBuilder result = new StringBuilder();


        result.append(getHeader());
        result.append("<body>");
        result.append("<div align=center>");
        result.append(
                String.format("<p><h3>Automated %s checklist analysis results for the request:</h3>",
                        setChecklist));
        result.append(
                String.format(
                        "Name = <b>%s</b>, iteration = <b>%s</b>, autocomplete = <b>%s</b>.</p>\n",
                        pon.getName(),
                        pon.getIteration(),
                        pon.getAutocomplete()));





        /* Check the name of checklist. If = TI proceed*/
        if (setChecklist.equals(MainConfig.getCHECKLISTS_NAME_TI())) {
            log.info("****************************************** TI CHECKLIST IS BEING PROCESSED *****************************************");
            ChecklistTI checklistTI = new ChecklistTI();

            /* PROCESS SPIDER ERRORS*/
            LinkedList<ErrorRecord> spiderErrors;
            spiderErrors = ServiceAnalyseSpider.processSpider(checklistTI, pon);
            pon.setSpiderErrors(spiderErrors);
            log.info(String.format("********************************* PROCESSING SPIDER of PON {} HAS FINISHED ******************"), pon.getName());

            /* PROCESS BIRT ERRORS*/
            LinkedList<ErrorRecord> birtErrors;
            birtErrors = ServiceAnalyseBirt.processBirt(checklistTI, pon);
            pon.setBirtErrors(birtErrors);
            log.info(String.format("****************************** PROCESSING BIRT of PON {} HAS FINISHED ******************"), pon.getName());

            /* OUTPUT */
            /* ChecklistTI log*/
            result.append(ChecklistMonitor.getData(checklistTI, pon));

            if (pon.getOutputOfErrors() != null) {
                result.append(
                        String.format(
                                "<div><p><font color=red>%s</font></p></div>\n", pon.getOutputOfErrors()));
            }


            result.append("<br/>Notes:<br/>");
            /* DISPLAY SPIDER ERRORS*/
            result.append(ChecklistMonitor.getSpiderErrorsData(checklistTI, pon));


            /* DISPLAY BIRT ERRORS*/
            result.append(ChecklistMonitor.getBirtErrorsData(checklistTI, pon));

        }

        result.append("</div>");
        result.append("</body>");



        /* To be sent as reply after analysis only*/
        httpServletResponse.getWriter().print(result.toString());
    }






    private static String getHeader() {

        String header = "<head>"+
                "<style>\n" +
//                "<link href=\"/lib/css/tooltip.css?v=8\" rel=\"stylesheet\" type=\"text/css\">" +
                "/* Tooltip container */\n" +
                ".tooltip {\n" +
                "  position: relative;\n" +
                "  display: inline-block;\n" +
                "  border-bottom: 1px dotted black; /* If you want dots under the hoverable text */\n" +
                "}\n" +
                "\n" +
                "/* Tooltip text */\n" +
                ".tooltip .tooltiptext {\n" +
                "  visibility: hidden;\n" +
                "  width: 500px;\n" +
                "  bottom: 100%;\n" +
                "  left: 50%; \n" +
                "  margin-left: -400px; /* Use shift, to center the tooltip */" +
                ""+
//                "  width: 1000px;\n" +
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
                "/* Show the tooltip text when you mouse over the tooltip container */\n" +
                ".tooltip:hover .tooltiptext {\n" +
                "  visibility: visible;\n" +
                "}"+
                "</style></head>\n";
        return header;
    }


}