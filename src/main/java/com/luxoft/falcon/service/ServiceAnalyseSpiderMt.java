package com.luxoft.falcon.service;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.config.SpiderConfigAndQuery;
import com.luxoft.falcon.dao.DbConnectorSpider;
import com.luxoft.falcon.model.Checklist;
import com.luxoft.falcon.model.ChecklistEntry;
import com.luxoft.falcon.model.Report;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;



/**
 * Contains method to get data from SPIDER database
 *
 * Data source: Spider PostgreSQL DB on requested Server:port and with credentials
 *
 * Input data: the name of the PON and iteration #
 * (comes from XML-file or from outer application/class via API)
 * Note: Market and region are not processed!
 *
 * Transformation: toString and getHTML by template - NOT IMPLEMENTED
 *
 * Output data: returns Requests Count and updates local Report object passed in arguments
 *
 * In case of exception returns text of error in report.logOfErrors
 */
@Slf4j
public class ServiceAnalyseSpiderMt extends Thread {


    private Checklist checklist;
    private Report report;
    private Boolean analyseRegression;

    private static MainConfig mainConfig = MainConfig.getInstance();
    private static SpiderConfigAndQuery spiderConfigAndQuery = SpiderConfigAndQuery.getInstance();
    private Connection con = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private PreparedStatement pstmtCount = null;
    private ResultSet resultSetCount = null;
    private int requestsCount;


    public ServiceAnalyseSpiderMt(Checklist checklist, Report report, Boolean analyseRegression){
        this.checklist = checklist;
        this.report = report;
        this.analyseRegression = analyseRegression;
    }

    public void run(){
        processSpiderChecklist(checklist, report, analyseRegression);
    }
    public List<ChecklistEntry> getSteps (){
        return report.getSpiderSteps();
    }
    public int getRequestsCount(){
        return requestsCount;
    }



    public int processSpiderChecklist(Checklist checklist, Report report, Boolean analyseRegression) {
        requestsCount = 0;
        log.info("**** in ServiceAnalyseSpiderMt.processSpiderChecklist() ****");

        try {
            con = DbConnectorSpider.connectDatabase(spiderConfigAndQuery);

            List<ChecklistEntry> fillSpiderErrors;

            /* Iterate over SPIDER*/
            fillSpiderErrors = analyseActual(checklist.getSpiderSteps(), report);

            /* Store items in report*/
            report.setSpiderSteps(fillSpiderErrors);

            /* CHECK FOR REGRESSION */
            if (analyseRegression) {
                analyseRegression(report);
            }

            resultSet.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            log.error(e.toString());
            report.addLogOfErrors(this.getClass().getName() + e.toString());
        } finally {
            if (resultSet != null) {
                resultSet = null;
            }
            if (resultSetCount != null) {
                resultSetCount = null;
            }

            if (pstmt != null) {
                pstmt = null;
            }

            if (pstmtCount != null) {
                pstmtCount = null;
            }
            if (con != null) {
                con = null;
            }
        }

        log.info("********************************* PROCESSING SPIDER of PON {} HAS BEEN FINISHED ******************",
                report.getName());

        return requestsCount;
    }




    private List<ChecklistEntry> analyseActual(List<String> steps, Report report) throws SQLException {
        List<ChecklistEntry> fillSpiderErrors = new LinkedList<>();

        if (report.getUseQueryLike()) {
            pstmt = con.prepareStatement(spiderConfigAndQuery.getQueryLike());
            pstmt.setString(1, "%" + report.getName() + "%");
        } else {
            pstmt = con.prepareStatement(spiderConfigAndQuery.getQueryIs());
            pstmt.setString(1, report.getName());
        }
        pstmt.setInt(2, report.getIteration());
        pstmt.setInt(4, report.getLimit());

        for (String errorToCheck : steps) {

                pstmt.setString(3, errorToCheck);

                String fullQuery = pstmt.toString();

                resultSet = pstmt.executeQuery();
                requestsCount++;

                if (report.getLimitControl()) {
                    pstmtCount =
                            con.prepareStatement(
                                    " SELECT COUNT(*) AS COUNTER FROM " +
                                            fullQuery
                                                    .split("ORDER BY", 2)[0]
                                                    .split("FROM", 2)[1]
                            );
                    resultSetCount = pstmtCount.executeQuery();
                    requestsCount++;
                }
                log.debug(String.format("************************* Spider query has been executed (%s)", fullQuery));


                /*If ResultSet is empty create one new item in List of Spider Errors with step and checked Flag*/
                if (!resultSet.isBeforeFirst()) {
                    fillSpiderErrors.add(
                            new ChecklistEntry(
                                    errorToCheck,
                                    Boolean.TRUE,
                                    Boolean.FALSE,
                                    fullQuery,
                                    report.getName(),
                                    "Not found"));
                }





                /* If ResultSet is not empty - check all rows and
                 * create new items in List of Spider Errors*/
                while (resultSet.next()) {
                    /* Check that LIMIT is not exceeded! */
                    if (report.getLimitControl()) {
                        if (resultSet.isLast()) {
                            while (resultSetCount.next()) {
                                if (Integer.valueOf(resultSetCount.getString("COUNTER"))
                                        > report.getLimit()) {

                                    report.addLogOfErrors("QUERY LIMIT " +
                                            report.getLimit().toString() +
                                            " has been exceeded - results count is " +
                                            resultSetCount.getString("COUNTER") +
                                            " rows " +
                                            "for SPIDER error \"" +
                                            errorToCheck +
                                            "\"!"
                                    );
                                }
                            }
                        }
                    }
                    String fullName = resultSet
                            .getString(mainConfig.getSPIDER_TASK_COL_NAME());
                    String error = resultSet
                            .getString(mainConfig.getSPIDER_JAVA_CLASS_ERROR_COL_NAME());


                    fillSpiderErrors.add(
                            new ChecklistEntry(
                                    error,
                                    true,
                                    true,
                                    fullQuery,
                                    fullName,
                                    "NOK"));



                    log.debug(String.format("************************* Spider resultSet item (%s) processed !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", fullName));
                }
        }
        return fillSpiderErrors;
    }



    /* Make regression analysis*/
    private void analyseRegression(Report report) throws SQLException {

            for (ChecklistEntry entry : report.getSpiderSteps()) {

                if (entry.getResultOfCheckIsNOK()) {

                    String[] restOfOriginalNameArray = entry.getFullNameOfPon().split(report.getName(), 2);
                    String nameToCheck;
                    try {
                        nameToCheck = restOfOriginalNameArray[0] + report.getPrevName() + restOfOriginalNameArray[1];
                    } catch (Exception e) {
                        nameToCheck = report.getPrevName();
                        log.error(String.format("Regression check - error while converting names - work with %s", nameToCheck));
                        report.addLogOfErrors(
                                String.format("Regression check - error while converting names - work with %s. Error is [%s]",
                                        nameToCheck,
                                        e.toString()));
                    }


                    if (report.getUseQueryLikeForPrev()) {
                        pstmt = con.prepareStatement(spiderConfigAndQuery.getQueryLike());
                        pstmt.setString(1, "%" + nameToCheck.trim() + "%");
                    } else {
                        pstmt = con.prepareStatement(spiderConfigAndQuery.getQueryIs());
                        pstmt.setString(1, nameToCheck.trim());
                    }

                    pstmt.setInt(2, report.getPrevIteration());
                    pstmt.setInt(4, report.getLimit());
                    pstmt.setString(3, entry.getNameOfErrorToCheckFor());
                    String fullQuery = pstmt.toString();

                    entry.setFullQuery(entry.getFullQuery() +
                            "<br/><br/>\nRegression query:<br/>\n" +
                            fullQuery);

                    resultSet = pstmt.executeQuery();
                    requestsCount++;

                    log.debug(
                            String.format(
                                    "************************* Spider query for regression check has been executed (%s)",
                                    fullQuery));


                    if (!resultSet.isBeforeFirst()) {
                        entry.setIsRegression("Yes");
                        entry.setFullNameOfRegressionPon("No records have been found for<br/>" + nameToCheck);

                    }


                    while (resultSet.next()) {
                        String fullName = resultSet.getString(mainConfig.getSPIDER_TASK_COL_NAME());
                        String restOfPrevName = fullName.replace(report.getPrevName(), "");
                        String restOfOriginalName = entry.getFullNameOfPon().replace(report.getName(), "");

                        if (restOfOriginalName.equals(restOfPrevName)) {
                            entry.setIsRegression("No");
                            entry.setFullNameOfRegressionPon("No records for<br/>" + nameToCheck);
                        }

                    }


                }
            }
    }
}