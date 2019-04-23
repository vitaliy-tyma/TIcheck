package com.luxoft.falcon;


//@Slf4j
public class AppRunner {
    /** To demonstration only - @Slf4j creates log entity as the following */
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AppRunner.class);

    public static String main() {
        log.debug("********** Run AppRunner.main() NOT USED HERE **********");
        return "result";
    }
}
