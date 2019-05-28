package com.luxoft.falcon.util;

import com.luxoft.falcon.config.MainConfig;
import lombok.extern.slf4j.Slf4j;

import static com.luxoft.falcon.util.ReadChecklistsFiles.getChecklistsList;
import static com.luxoft.falcon.util.ReadXML.readMainConfigFromFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/* Class is loaded at start of the app (during webapp is started)
 * and is used to load mainConfig and checklists list.
 * It is used with <listener-class>com.luxoft.falcon.util.Listener</listener-class> in web.xml */

//FIXME - main task!!!
// Make tests
// Make eager initialization
// Make AJAX for PON name detection and result displaying
// Make Api for other apps
// Make NDS part - open DB?
// MAKE AWB part - visual?

@Slf4j
public class Listener implements javax.servlet.ServletContextListener {
    ServletContext context;



    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        log.info("+++++++++++++++++++++ Context Created +++++++++++++++++++++");
        context = servletContextEvent.getServletContext();
        // set variable to servlet context
        context.setAttribute("TIcheck", "TIcheck_VALUE");


        readMainConfigFromFile(MainConfig.getCONFIG_PATH_AND_NAME());
        log.info("MainConfig.getCONFIG_PATH_AND_NAME(): " + MainConfig.getCONFIG_PATH_AND_NAME());

        try {
            //FixME - error if is called with empty request - null.xml is impossible to proceed!
            getChecklistsList(MainConfig.getCHECKLISTS_PATH());
            log.info("MainConfig.getCHECKLISTS_PATH(): " + MainConfig.getCHECKLISTS_PATH());
        } catch (Exception e) {
            log.error("Error in getChecklistsList: " + e.getMessage());
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        context = servletContextEvent.getServletContext();
        log.info("+++++++++++++++++++++ Context Destroyed +++++++++++++++++++++");
    }
}
