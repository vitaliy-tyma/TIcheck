/**
* @author Vitaliy-tyma
*
* TI checklist automation -
* Close to FAST2.0 / FOX / FALCON project
*
*************************************************************
* Uses user's PON name to search .
* May detect regression if previous PON has been entered.
*
* Works with SPIDER db to extract 2 errors
* Works with BIRT db to extract results of a number of FACT tests
* Works with NDS db to extract content of related tables
* Works with SPIDER db to define versions of TI
*
*************************************************************
*
* Configuration is located in the <project_name>.xml file
* 1) IDE - root/src/main/resources folder
* 2) tomcat - bin folder?????
* 
*************************************************************
* To be done list:
* 1) Logging to the file, ready to be displayed in browser
* 2) test must be automatically run at the compilation stage (as a part of Maven activities)
* 3) AJAX must be added to automatically search for PON name (including mask or wildcard symbols)
* 4) AJAX must be added to display results in online mode (-//-)
* 5) configuration must be loaded after start in singleton class and reloaded on request form the browser
* loader must use SAX
* 6) continuous integration with Jenkins must be set
*/