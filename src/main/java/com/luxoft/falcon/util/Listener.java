package com.luxoft.falcon.util;

import com.luxoft.falcon.config.MainConfig;
import lombok.extern.slf4j.Slf4j;
import static com.luxoft.falcon.util.ReadXML.readMainConfigFromFile;
import javax.servlet.ServletContextEvent;

/* Class is loaded at start of the app (while webapp is started) and is used to load mainConfig.
* It is used with <listener-class>com.luxoft.falcon.util.Listener</listener-class> in web.xml */

@Slf4j
public class Listener implements javax.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        readMainConfigFromFile(MainConfig.getCONFIG_PATH_AND_NAME());

        log.info("!!!!!!!!!!!!!! MainConfig has been read !!!!!!!!!!!!!!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
