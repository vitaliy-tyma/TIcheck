package com.luxoft.falcon.controller;

import com.luxoft.falcon.configuration.MainConfig;
import com.luxoft.falcon.service.ServletDbService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Servlet is launched from web-browser and calls SpiderDbConnector to get data */
public class ServletSpider extends HttpServlet {
    private final String SOURCE_NAME = MainConfig.getSOURCE_NAME_SPIDER();

    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {

        String result = ServletDbService.servletDbService(SOURCE_NAME);

        /**Maven's webapp code*/
        httpServletResponse.getWriter().print(result);
        /*********************************************/
    }
}