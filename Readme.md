/**
* @author Vitaliy-tyma
*
* Spider connector - ETL application
* For FAST2.0 - FOX - FALCON app
*
*************************************************************
* Works with spider db to (E)xtract errors and
* corresponding Jira tickets for the requested PON_region.
*
* On the (T)ransform step acquired data are analysed and
* put into the special class, that to be stored on the (L)oad step by an another app.
*
* (L)oader will be implemented by others.
*************************************************************
*
* Configuraion is located in the spiderconnector.xml file
* 1) IDE - root folder
* 2) tomcat - bin folder
* 
*************************************************************
* To be done list:
* 1) Logging to the file, ready to be displayed in browser
* 2) test must be automatically run at the compilation stage (as a part of Maven activities).
* 3) after the compilation and deployment stage the "test.bat" must be run to open browser window.
* 4) continuous integration with Jenkins must be set.
*/