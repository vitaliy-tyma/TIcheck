package com.luxoft.falcon.controller;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.Checklist;
import com.luxoft.falcon.model.Pon;
import com.luxoft.falcon.model.SpiderError;
import com.luxoft.falcon.service.ServletAnalyseService;
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
    private Pon pon = new Pon();


    @Override
    public void init() throws ServletException {
        super.init();
        Loader loader = new Loader();
        loader.init();
    }



    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {


        log.info("******** ServletAnalyse.doGet() started");

        final String ponName = httpServletRequest.getParameter(MainConfig.getPON_NAME_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getPON_NAME_REQUEST_PARAMETER_VALUE(), ponName);

        final String ponIteration = httpServletRequest.getParameter(MainConfig.getPON_ITERATION_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getPON_ITERATION_REQUEST_PARAMETER_KEY(), ponName);

        final String autocompletePon = httpServletRequest.getParameter(MainConfig.getAUTOCOMPLETE_PON_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getAUTOCOMPLETE_PON_REQUEST_PARAMETER_KEY(), ponName);

        log.info(
                String.format(
                        "******* Processing with the request: name = <b>%s</b>; iteration = <b>%s</b>; autocomplete = <b>%s</b>",
                        ponName,
                        ponIteration,
                        autocompletePon));



        pon.setName(ponName);
        pon.setIteration(Integer.parseInt(ponIteration));

        if (autocompletePon == null){
            pon.setAutocomplete(false);
        } else {
            //.equals("on")
            pon.setAutocomplete(true);
        }


        /* Read all sections to define check steps - fill in Keys*/
        /* !!!!!!!!!!!!!!!!!!!!!*/


        Checklist checklist = new Checklist();
        StringBuilder result = new StringBuilder();


        LinkedList<SpiderError> spiderErrors;
        spiderErrors = ServletAnalyseService.processSpider(checklist, pon);
        pon.setSpiderErrors(spiderErrors);

        result.append(
                String.format(
                        "<p>Spider TI check for request <b>%s</b>, iteration <b>%s</b>, autocomplete <b>%s</b></p>\n",
                        pon.getName(),
                        pon.getIteration(),
                        pon.getAutocomplete()));

        for (SpiderError spiderError: pon.getSpiderErrors()){
            result.append(
                    String.format(
                            "<p>Full Name = %s, Error = %s</p>\n",
                            spiderError.getFullName(),
                            spiderError.getJavaClassError()));
        }

        /* SQL request in Spoiler*/
        result.append("<div><details><summary><u>Open to see the Query</u></summary>\n" +
                        "<i><b><font color = green>");
        result.append(pon.getQueryFull());
        result.append("</font></b></i></details></div>\n");






        /* To be filled after analysis only*/


        pon.setOutput("Test result is ready to be printed out");
        log.info(String.format("*************** PROCESSING SPIDER of PON {} HAS FINISHED ******************"), pon.getName());










        httpServletResponse.getWriter().print(result.toString());

    }


}