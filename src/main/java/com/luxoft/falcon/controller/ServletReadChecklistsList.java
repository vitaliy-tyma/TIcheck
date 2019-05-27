package com.luxoft.falcon.controller;

import com.luxoft.falcon.config.MainConfig;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.luxoft.falcon.util.ReadChecklistsFiles.getChecklistsList;

@Slf4j
public class ServletReadChecklistsList extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            getChecklistsList(MainConfig.getCHECKLISTS_PATH());
        } catch (Exception e) {
            log.error("Error in getChecklistsList: " + e.getMessage());
        }
        log.info("*********************** ServletReadChecklistsList.doGet() ****************************************");
        //httpServletResponse.getWriter().print(result.toString()); //May be used to return flag - successful!
    }
}
