package com.luxoft.falcon.controller;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.Checklist;
import com.luxoft.falcon.model.Pon;
import com.luxoft.falcon.model.MapError;
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


        log.info("******** ServletAnalyse.doGet() started *********");
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
                        "******* Processing with the request: name = <b>%s</b>;" +
                        " iteration = <b>%s</b>; autocomplete = <b>%s</b>",
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


        LinkedList<MapError> spiderErrors;
        spiderErrors = ServletAnalyseService.processSpider(checklist, pon);
        pon.setSpiderErrors(spiderErrors);

        result.append("<p><h3>Automated TI checklist analysis results:</h3></p>");
        result.append(
                String.format(
                        "Request is <b>%s</b>, iteration is <b>%s</b>, autocomplete is <b>%s</b></p>\n",
                        pon.getName(),
                        pon.getIteration(),
                        pon.getAutocomplete()));

        result.append("<p>1) Spider check</p>");

        StringBuilder queryFull = new StringBuilder();
        queryFull.append("<details><summary><u>See query</u></summary>\n");
        queryFull.append("<i><b><font color = green>");
        queryFull.append(pon.getQueryFull());
        queryFull.append("</font></b></i></details>\n");

        if (pon.getNoSpiderErrorsPresent()){
            result.append(String.format("<p>No spider errors are present. %s</p>", queryFull.toString()));
        }

        for (MapError spiderError: pon.getSpiderErrors()){
            /* Put SQL request in Spoiler*/

            result.append(
                    String.format(
                            "<div><p>Full Name = %s, Error = %s. %s</p></div>\n",
                            spiderError.getFullName(),
                            spiderError.getJavaClassError(),
                            queryFull.toString()));
        }
        pon.setOutput("Spider errors checklist result - XX");
        log.info(String.format("*************** PROCESSING SPIDER of PON {} HAS FINISHED ******************"), pon.getName());

















        /* To be sent after analysis only*/
        httpServletResponse.getWriter().print(result.toString());

    }


}