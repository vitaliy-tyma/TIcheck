package com.luxoft.falcon.controller;

import com.luxoft.falcon.config.MainConfig;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.luxoft.falcon.util.ReadXML.readMainConfigFromFile;

@Slf4j
public class ServletReadMainConfig extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        readMainConfigFromFile(MainConfig.getCONFIG_PATH_AND_NAME());
        log.info("*********************** ServletReadMainConfig.doGet() ****************************************");
        //httpServletResponse.getWriter().print(result.toString()); //May be used to return flag - successful!
    }
}
