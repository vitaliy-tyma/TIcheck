package com.luxoft.falcon.controller;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.ChecklistTI;
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
    //    private Pon pon = new Pon();
    private Boolean analyseRegression = Boolean.FALSE;
    private Boolean nokOnlyBool = Boolean.FALSE;



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
        httpServletRequest.setAttribute(MainConfig.getPON_ITERATION_REQUEST_PARAMETER_KEY(), ponIteration);

        final String autocompletePon =
                httpServletRequest.getParameter(MainConfig.getAUTOCOMPLETE_PON_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getAUTOCOMPLETE_PON_REQUEST_PARAMETER_KEY(), autocompletePon);

        final String checklistRequestName =
                httpServletRequest.getParameter(MainConfig.getCHECKLISTS_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getCHECKLISTS_REQUEST_PARAMETER_KEY(), checklistRequestName);

        final String nokOnly =
                httpServletRequest.getParameter(MainConfig.getGET_NOK_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getGET_NOK_REQUEST_PARAMETER_VALUE(), nokOnly);

        final String prevPonName =
                httpServletRequest.getParameter(MainConfig.getPON_NAME_PREV_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getPON_NAME_PREV_REQUEST_PARAMETER_VALUE(), prevPonName);

        final String prevPonIteration =
                httpServletRequest.getParameter(MainConfig.getPON_ITERATION_PREV_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getPON_ITERATION_PREV_REQUEST_PARAMETER_KEY(), prevPonIteration);

        final String autocompletePrevPon =
                httpServletRequest.getParameter(MainConfig.getAUTOCOMPLETE_PON_PREV_REQUEST_PARAMETER_KEY());
        httpServletRequest.setAttribute(MainConfig.getAUTOCOMPLETE_PON_PREV_REQUEST_PARAMETER_KEY(), autocompletePrevPon);


        if ((nokOnly != null) && (nokOnly.equals("on"))) {
            nokOnlyBool = Boolean.TRUE;
        }

        if ((prevPonName != null) && (prevPonIteration != null)) {
            analyseRegression = Boolean.TRUE;
        }

        /* Check the name of checklist. If = TI proceed*/
        if (checklistRequestName.equals(MainConfig.getCHECKLISTS_NAME_TI())) {
            log.info("****************************************** TI CHECKLIST IS BEING PROCESSED *****************************************");
            ChecklistTI checklist = new ChecklistTI();

            log.info(
                    String.format(
                            "******* Processing checklist %s with the request: name = %s;" +
                                    " iteration = %s; autocomplete = %s, NOK only = %s",
                            checklistRequestName,
                            ponName,
                            ponIteration,
                            autocompletePon,
                            nokOnlyBool));



            checklist.setName(ponName);
            checklist.setIteration(Integer.parseInt(ponIteration));//validate!!!
            checklist.setChecklistName(checklistRequestName);
            checklist.setNokOnly(nokOnlyBool);
            if (autocompletePon.equals("on")) {
                checklist.setAutocomplete(Boolean.TRUE);
            } else {
                checklist.setAutocomplete(Boolean.FALSE);
            }



            if (analyseRegression) {
                log.info(
                        String.format(
                                "******* Processing regression check with the request: name = %s;" +
                                        " iteration = %s; autocomplete = %s",
                                prevPonName,
                                prevPonIteration,
                                autocompletePrevPon));


                //+
                checklist.setPrevName(prevPonName);
                checklist.setPrevIteration(Integer.parseInt(prevPonIteration));//validate!!!
                if (autocompletePrevPon.equals("on")) {
                    checklist.setPrevAutocomplete(Boolean.TRUE);
                } else {
                    checklist.setPrevAutocomplete(Boolean.FALSE);
                }
            }









            /* Read all sections to define check steps - fill in Keys*/
            /* !!!!!!!!!!!!!!!!!!!!!*/



            result.append(getHeader());
            result.append("<body>");
            result.append("<div align=center>");
            result.append(
                    String.format("<p><h3>Automated %s checklist analysis results for the request:</h3>",
                            checklistRequestName));
            result.append(
                    String.format(
                            "Name = <b>%s</b>, iteration = <b>%s</b>, autocomplete = <b>%s</b>, limit = <b>%s</b>.</p>\n",
                            checklist.getName(),
                            checklist.getIteration(),
                            checklist.getAutocomplete(),
                            MainConfig.getQUERY_LIMIT()));



            if (analyseRegression) {
                result.append(
                        String.format(
                                "PrevPonName = <b>%s</b>, prevIteration = <b>%s</b>, prevAutocomplete = <b>%s</b>, limit = <b>%s</b>.</p>\n",
                                checklist.getPrevName(),
                                checklist.getPrevIteration(),
                                checklist.getPrevAutocomplete(),
                                MainConfig.getQUERY_LIMIT()));
            }




            /* PROCESS SPIDER ERRORS*/
            ServiceAnalyseSpider.processSpiderChecklist(checklist, analyseRegression, nokOnlyBool);
            log.info(String.format("********************************* PROCESSING SPIDER of PON {} HAS FINISHED ******************"), checklist.getName());




            /* PROCESS BIRT ERRORS*/
            ServiceAnalyseBirt.processBirtChecklist(checklist, analyseRegression, nokOnlyBool);
            log.info(String.format("****************************** PROCESSING BIRT of PON {} HAS FINISHED ******************"), checklist.getName());









            /* OUTPUT */
            /* Checklist report*/
            //-
//            result.append(ChecklistMonitor.getData(checklist, pon));
            //+
            result.append(ChecklistMonitor.getDataFromChecklist(checklist));




        result.append("</div>");
        result.append("</body>");


        } else {
            result.append("Checklist name is not correct. Not possible to proceed.");
        }
        /* To be sent as reply after analysis only*/
        httpServletResponse.getWriter().print(result.toString());
    }













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