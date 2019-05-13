package com.luxoft.falcon;

import com.luxoft.falcon.config.MainConfig;
import lombok.extern.slf4j.Slf4j;
import static com.luxoft.falcon.util.ReadXML.readMainConfigFromFile;

@Slf4j
public class AppRunner {
    public static void main(String[] args) {
        log.info("!!!!!!!!!!!!!! MAIN METHOD HAS BEEN STARTED !!!!!!!!!!!!!!");
        readMainConfigFromFile(MainConfig.getCONFIG_PATH_AND_NAME());

    }
}
