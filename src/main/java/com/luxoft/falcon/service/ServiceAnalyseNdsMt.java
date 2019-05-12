package com.luxoft.falcon.service;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.config.NdsConfigAndQuery;
import com.luxoft.falcon.config.SpiderConfigAndQuery;
import com.luxoft.falcon.dao.DbConnectorNds;
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


/* Is used to process checklist for SPIDER */
@Slf4j
public class ServiceAnalyseNdsMt extends Thread{
    private Checklist checklist;
    private Report report;
    private Boolean analyseRegression;

    private static NdsConfigAndQuery ndsConfigAndQuery = new NdsConfigAndQuery();
    private static Connection con = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet resultSet = null;
    private static int requestsCount;

    public ServiceAnalyseNdsMt(Checklist checklist, Report report, Boolean analyseRegression){
        this.checklist = checklist;
        this.report = report;
        this.analyseRegression = analyseRegression;
    }

    public void run(){
        processNdsChecklist(checklist, report, analyseRegression);
    }
    public List<ChecklistEntry> getSteps (){
        return report.getNdsSteps();
    }
    public int getRequestsCount(){
        return requestsCount;
    }

    public int processNdsChecklist(Checklist checklist, Report report, Boolean analyseRegression) {
        requestsCount = 0;
        log.info("**** in ServiceAnalyseNds.processNdsChecklist() ****");

        try {
            con = DbConnectorNds.connectDatabase(
                    ndsConfigAndQuery.getJdbcUrl(),
                    ndsConfigAndQuery.getJdbcLogin(),
                    ndsConfigAndQuery.getJdbcPassword());


            List<ChecklistEntry> fillNdsErrors;// = new LinkedList<>();

            /* Iterate over NDS*/
            fillNdsErrors = analyseActual(checklist.getNdsSteps(), report);

            /* Store items in report*/
            report.setNdsSteps(fillNdsErrors);

            /* CHECK FOR REGRESSION */
            if (analyseRegression) {
                analyseRegression(report);
            }

            resultSet.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            report.addLogOfErrors(e.getMessage());
        } finally {
            if (resultSet != null) {
                resultSet = null;
            }
            if (pstmt != null) {
                pstmt = null;
            }
            if (con != null) {
                con = null;
            }
        }


        return requestsCount;
    }


    private static List<ChecklistEntry> analyseActual(List<String> steps, Report report) throws SQLException {
        List<ChecklistEntry> fillSpiderErrors = new LinkedList<>();

//FIXME - DB is compressed and enciphered - NDS lib must be used to open
// - Now we have error here!!!
        pstmt = con.prepareStatement(ndsConfigAndQuery.getQuery());
//        pstmt.setString(1, "%" + report.getName() + "%");
//        pstmt.setInt(2, report.getIteration());
//        pstmt.setInt(4, report.getLimit());

        //TODO NOT IMPLEMENTED
        for (String errorToCheck : steps) {
//            try {
//            pstmt.setString(3, errorToCheck);

            String fullQuery = pstmt.toString();

            resultSet = pstmt.executeQuery();
            requestsCount++;

            log.info(String.format("************************* NDS query has been executed (%s)", fullQuery));


            /*If ResultSet is empty create one new item in List of NDS Errors with step and checked Flag*/
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





            /* Ir ResultSet is not empty - check all rows and create new items in List of Spider Errors*/
            while (resultSet.next()) {
                String fullName = resultSet.getString(MainConfig.getSPIDER_TASK_COL_NAME());
                String error = resultSet.getString(MainConfig.getSPIDER_JAVA_CLASS_ERROR_COL_NAME());


                fillSpiderErrors.add(
                        new ChecklistEntry(
                                error,
                                true,
                                true,
                                fullQuery,
                                fullName,
                                "NOK"));

                log.debug(String.format("************************* NDS resultSet item (%s) processed !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", fullName));
            }


//            } catch (Exception e) {
//                log.error(e.getMessage());
//                report.addLogOfErrors(e.getMessage());
//            }

        }
        return fillSpiderErrors;
    }


    /* Make regression analysis*/
    private static void analyseRegression(Report report) throws SQLException {

//        try {

//TODO - NOT IMPLEMENTED
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
                                    e.getMessage()));
                }


                pstmt = con.prepareStatement(ndsConfigAndQuery.getQuery());
//                pstmt.setString(1, "%" + nameToCheck.trim() + "%");
//                pstmt.setInt(2, report.getPrevIteration());
//                pstmt.setInt(4, report.getLimit());
//                pstmt.setString(3, entry.getNameOfErrorToCheckFor());
                String fullQuery = pstmt.toString();

                resultSet = pstmt.executeQuery();
                requestsCount++;

                log.debug(
                        String.format(
                                "************************* NDS query for regression check has been executed (%s)",
                                fullQuery));


                if (!resultSet.isBeforeFirst()) {
                    entry.setIsRegression("Yes");
                    entry.setFullNameOfRegressionPon(nameToCheck);
                }


                while (resultSet.next()) {
                    String fullName = resultSet.getString(MainConfig.getSPIDER_TASK_COL_NAME());
                    String restOfPrevName = fullName.replace(report.getPrevName(), "");
                    String restOfOriginalName = entry.getFullNameOfPon().replace(report.getName(), "");

                    if (restOfOriginalName.equals(restOfPrevName)) {
                        entry.setIsRegression("No");
                    }

                }


            }
        }
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            report.addLogOfErrors(e.getMessage());
//        }

    }
}