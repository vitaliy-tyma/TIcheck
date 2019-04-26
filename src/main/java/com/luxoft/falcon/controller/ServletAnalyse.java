package com.luxoft.falcon.controller;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.Checklist;
import com.luxoft.falcon.model.Pon;
import com.luxoft.falcon.model.MapError;
import com.luxoft.falcon.service.ServiceAnalyseBirt;
import com.luxoft.falcon.service.ServiceAnalyseSpider;
import com.luxoft.falcon.util.Loader;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Servlet is launched from web-browser and starts all processes
 */
@Slf4j
public class ServletAnalyse extends HttpServlet {
//    private Pon pon = new Pon();


    @Override
    public void init() throws ServletException {
        super.init();
        Loader loader = new Loader();
        loader.init();
    }


    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {


        log.info("******** NEW REQUEST STARTED ***************** ServletAnalyse.doGet() **********");
        log.info("***********************************************************************************************************");
        log.info("********************************************************************************");
        Pon pon = new Pon();

        final String ponName =
                httpServletRequest.getParameter(MainConfig.getPON_NAME_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getPON_NAME_REQUEST_PARAMETER_VALUE(), ponName);

        final String ponIteration =
                httpServletRequest.getParameter(MainConfig.getPON_ITERATION_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getPON_ITERATION_REQUEST_PARAMETER_KEY(), ponName);

        final String autocompletePon =
                httpServletRequest.getParameter(MainConfig.getAUTOCOMPLETE_PON_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getAUTOCOMPLETE_PON_REQUEST_PARAMETER_KEY(), ponName);


        log.info(
                String.format(
                        "******* Processing with the request: name = %s;" +
                                " iteration = %s; autocomplete = %s",
                        ponName,
                        ponIteration,
                        autocompletePon));


        pon.setName(ponName);
        pon.setIteration(Integer.parseInt(ponIteration));


        if (autocompletePon == null) {
            pon.setAutocomplete(false);
        } else {
            //.equals("on")
            pon.setAutocomplete(true);
        }


        /* Read all sections to define check steps - fill in Keys*/
        /* !!!!!!!!!!!!!!!!!!!!!*/


        Checklist checklist = new Checklist();
        StringBuilder result = new StringBuilder();
        result.append("<p><h3>Automated TI checklist analysis results:</h3></p>");
        result.append(
                String.format(
                        "Request is <b>%s</b>, iteration is <b>%s</b>, autocomplete is <b>%s</b></p>\n",
                        pon.getName(),
                        pon.getIteration(),
                        pon.getAutocomplete()));





        /* PROCESS SPIDER ERRORS*/
        LinkedList<MapError> spiderErrors;
        spiderErrors = ServiceAnalyseSpider.processSpider(checklist, pon);
        pon.setSpiderErrors(spiderErrors);


        result.append("<p>1) Spider check</p>");



/*        if (pon.getNoSpiderErrorsPresent()) {
            result.append(String.format("<p>No spider errors are present. %s</p>", queryFull.toString()));
        }*/

        for (MapError spiderError : pon.getSpiderErrors()) {
            /* Put SQL request in Spoiler*/
            StringBuilder queryFull = new StringBuilder();
            queryFull.append("<details><summary><u>See query</u></summary>\n");
            queryFull.append("<i><b><font color = green>");
            queryFull.append(spiderError.getQueryFull());
            queryFull.append("</font></b></i></details>\n");

            result.append(
                    String.format(
                            "<div><p>Full Name = %s, Error = %s. %s</p></div>\n",
                            spiderError.getFullName(),
                            spiderError.getError(),
                            queryFull.toString()));
        }
//        pon.setOutput("Spider errors checklist result - XX");
        if (pon.getOutput() != null) {
            result.append(
                    String.format(
                            "<div><p><font color=red>%s<font></p></div>\n", pon.getOutput()));
        }


        log.info(String.format("********************************* PROCESSING SPIDER of PON {} HAS FINISHED ******************"), pon.getName());










        /* PROCESS BIRT ERRORS*/
        LinkedList<MapError> birtErrors;
        birtErrors = ServiceAnalyseBirt.processBirt(checklist, pon);
        pon.setBirtErrors(birtErrors);


        result.append("<p>2) Birt check</p>");


/*
        if (pon.getNoBirtErrorsPresent()) {
            result.append(String.format("<p>No BIRT errors are present. %s</p>", queryFull.toString()));
        }
*/

        for (MapError birtError : pon.getBirtErrors()) {
            /* Put SQL request in Spoiler*/
            StringBuilder queryFull = new StringBuilder();
            queryFull.append("<details><summary><u>See query</u></summary>\n");
            queryFull.append("<i><b><font color = green>");
            queryFull.append(birtError.getQueryFull());
            queryFull.append("</font></b></i></details>\n");

            result.append(
                    String.format(
                            "<div><p>Full Name = %s, Error = %s. %s</p></div>\n",
                            birtError.getFullName(),
                            birtError.getError(),
                            queryFull.toString()));
        }
//        pon.setOutput("Spider errors checklist result - XX");
        if (pon.getOutput() != null) {
            result.append(
                    String.format(
                            "<div><p><font color=red>%s<font></p></div>\n", pon.getOutput()));
        }

        log.info(String.format("****************************** PROCESSING BIRT of PON {} HAS FINISHED ******************"), pon.getName());








        /* To be sent after analysis only*/
        httpServletResponse.getWriter().print(result.toString());

    }


}