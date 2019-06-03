package com.luxoft.falcon.controller;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.*;
import com.luxoft.falcon.service.*;
import com.luxoft.falcon.util.ReadXML;
import com.luxoft.falcon.view.View;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet is launched from web-browser's form and starts to processes the checklists analysis
 */

//FIXME - Refactor with SPRING MVC

//TODO - USE testing DBs!!!
// Z:\Test_DB\release-7.2_MAN\TBAA\001\iDb

//TODO Check if PON and iteration is valid!
@Slf4j
public class ServletAnalyse extends HttpServlet {
    private static MainConfig mainConfig = MainConfig.getInstance();
    //    private static ChecklistsList checklistsList = ChecklistsList.getInstance();
    private static int requestsCount = 0;
    private static boolean analyseRegression = false;


//    @Override
//    public void init() throws ServletException {
//        super.init();
//    }


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

            if (!mainConfig.getConfigIsLoaded()) {
                throw new Exception("MainConfig is not loaded yet!!!");
            }

//TODO - Load all checklist at the start (or on special request) and
// only choose the appropriate from the memory at this point
// to be developed later
            checklist = ReadXML.readChecklistFromFile(checklistRequestName);


            log.info(String.format(
                    "******* Processing checklist %s with the request: name = %s;" +
                            " iteration = %s; useQueryLike = %s",
                    checklistRequestName,
                    ponName,
                    ponIteration,
                    useQueryLike));


            if (ponName != null || ponIteration != null || limitValue != null) {
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


                if (regressionCheck != null) {
                    if (regressionCheck.equals("on") &&
                            ((!prevPonName.equals("")) && (!prevPonIteration.equals("")))) {
                        analyseRegression = true;
                    }
                }





                /* MAIN ACTION IS BELOW!*/
                if (mainConfig.getMULTITHREAD()) {
                    //May be faster for very big requests!!
                    log.info("****************************************** startMultithreadedMode *****************************************");
                    startMultithreadedMode(checklist, report, analyseRegression);
                } else {
                    // Use SingleThread mode for debug!!!
                    log.info("****************************************** startSingleThreadedMode *****************************************");
                    startSingleThreadedMode(checklist, report, analyseRegression);
                }


                log.info("****************************************** CHECKLIST PROCESSING HAS FINISHED *****************************************");
            }
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
            boolean showEmptyForm;
            showEmptyForm = (ponName == null && ponIteration == null && limitValue == null);
            result.append(
                    View.getHTML(
                            checklist.getNameOfChecklist(),
                            report,
                            analyseRegression,
                            showEmptyForm));


            /* The block is not necessary - works well without it*/
            {
                checklist.clear();
                report.clear();
                //checklist = null;//never used more!
                report = null;
            }


            /* Error handling in case of input parameters problems*/
        } catch (Exception e) {
//            log.error(e.getMessage());
            log.error(e.toString());
            log.error(String.valueOf(e));
//            report.addLogOfErrors(e.getMessage());
            report.addLogOfErrors(this.getClass().getName() + e.toString());
            result.append("<html>\nError while processing ServiceAnalyse<br/>\n");
            result.append(report.getLogOfErrors());
            result.append("\n<html>");
        }

        /* Reset all variables to be ready for the next cycle */
        analyseRegression = false;
        requestsCount = 0;

        /* Send reply to the client */
        httpServletResponse.getWriter().print(result.toString());
    }


    // Use one thread to process different data sources - useful for debug!!!
    private void startSingleThreadedMode(Checklist checklist, Report report, boolean analyseRegression) {

        log.info("************************ startSingleThreadedMode **************");
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

        log.info("************************ startMultithreadedMode **************");
        try {

            ServiceAnalyseSpiderMt analyseSpiderMt = new ServiceAnalyseSpiderMt(checklist, report, analyseRegression);
            ServiceAnalyseBirtMt analyseBirtMt = new ServiceAnalyseBirtMt(checklist, report, analyseRegression);
            ServiceAnalyseNdsMt analyseNdsMt = new ServiceAnalyseNdsMt(checklist, report, analyseRegression);

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
            /* Wait for all threads to finish*/
            analyseSpiderMt.join();
            analyseBirtMt.join();
            analyseNdsMt.join();

            report.setSpiderSteps(analyseSpiderMt.getSteps());
            requestsCount += analyseSpiderMt.getRequestsCount();

            report.setBirtSteps(analyseBirtMt.getSteps());
            requestsCount += analyseBirtMt.getRequestsCount();

            report.setNdsSteps(analyseNdsMt.getSteps());
            requestsCount += analyseNdsMt.getRequestsCount();
        } catch (Exception e) {
            report.addLogOfErrors(e.toString());
        }
    }

}