/**
* @author Vitaliy-tyma
* @version 0.2.1
* @since   2019-05-03
*
* Automated checklistTI analyser
* Related to FAST2.0 / FOX / FALCON project
* TI for the start
*
*************************************************************
* Uses user's PON name to search .
* May detect regression if previous PON has been entered.
*
* Works with SPIDER db to extract 2 errors
* Works with BIRT db to extract results of a number of FACT tests
* Works with NDS db to extract content of related tables (0Mxxxx - 1 file, NHxxxx - many files)
* - NO!!!! - Works with SPIDER db to define versions of TI - NO!!!!
*
* ISSUES
* Different generations of projects and different schemas in DBs
*
* Different naming conversions for SPIDER and BIRT
* SPIDER = PON928_EA
* BIRT = PONA30_E7_BMD_MID_R1
*
*************************************************************
* Deployment *
* Configuration is located in the <project_name>.xml file ?????
* 1) IDE - ./src/main/resources folder
* 2) tomcat - ./bin/src/main/resources folder ?????
* 
*************************************************************
* To be done list:
* 1) Logging to the file, ready to be displayed in browser, Admin console to watch log
* 2) Tests must be automatically run at the compilation stage (as a part of Maven activities)
* 3) AJAX must be added to automatically search for PON name (including mask or wildcard symbols)
* 4) AJAX must be added to display results in live mode (-//-)
* 5) Configuration must be loaded after start in singleton class and reloaded on request form the browser
* loader must use SAX
* 6) Continuous integration with Jenkins and Docker must be set
*/