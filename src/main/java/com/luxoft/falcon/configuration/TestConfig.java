package com.luxoft.falcon.configuration;

import lombok.Getter;

/** Now for test is used one of the existing records in Spider DB
 * Must be updated to use special record - only for testing purposes!
 * */
public class TestConfig {


    @Getter
    private static final String RUN_TEST = "PONADE_Full";
    @Getter
    private static final String ITERATION_TEST = "1";

    @Getter
    private static final String TASK_TEST = "PONADE_Full                                       ";
    @Getter
    /** Be aware of 2 spaces before "Main..." and \n at the end of the string!*/
    private static final String ERROR_TEXT = "2019-03-23 14:36:54,252  Main Thread ERROR AbstractValidator:86 - Product [/mnt/navdata04/DB_Prod/Full-NDS_DB/NBTEvo/ECE/PONB3A/EZ/001/iDb/ME_/0MEZB3A1.NDS] doesn't contain a table poiAttrNameStringRelationTable\n";
    @Getter
    private static final String JIRA_TICKET_TEXT = "NAV2010ANA-24449";
    @Getter
    private static final String COUNT_TEXT = "77";

}
