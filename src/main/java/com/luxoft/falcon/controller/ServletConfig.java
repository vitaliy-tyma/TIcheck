package com.luxoft.falcon.controller;

import com.luxoft.falcon.configuration.MainConfig;
import com.luxoft.falcon.service.ServletConfigService;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Servlet is launched from web-browser and reads config from XML file */
public class ServletConfig extends HttpServlet {
    private static final String SOURCE_NAME = MainConfig.getSOURCE_NAME_SPIDER();


    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {

        String result = ServletConfigService.service(SOURCE_NAME);

        /**Maven's webapp code*/
        httpServletResponse.getWriter().print(result);
    }
}