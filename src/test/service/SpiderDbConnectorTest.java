package service;

import com.luxoft.falcon.configuration.TestConfig;
import com.luxoft.falcon.dao.SpiderDbConnector;
import com.luxoft.falcon.model.ConfigData;
import com.luxoft.falcon.configuration.MainConfig;
import com.luxoft.falcon.model.SpiderDataClass;
import com.luxoft.falcon.model.SpiderErrorClass;
import com.luxoft.falcon.util.ReadXmlFile;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;


/**
 * Test is under construction!
 * <p>
 * Test is used to connect to DB with data from XML file, substitute entities to known data,
 * and compare two created classes.
 * <p>
 * Now comparison is not complete.
 */
@Slf4j
public class SpiderDbConnectorTest {
    private static final String CONFIG_FILE_NAME = MainConfig.getCONFIG_FILE_NAME();
    private static final String SOURCE_NAME = MainConfig.getSOURCE_NAME_SPIDER();


    private static final String RUN_TEST = TestConfig.getRUN_TEST();
    private static final String ITERATION_TEST = TestConfig.getITERATION_TEST();
    private static final String ERROR_TEXT = TestConfig.getERROR_TEXT();
    private static final String JIRA_TICKET_TEXT = TestConfig.getJIRA_TICKET_TEXT();
    private static final String COUNT_TEXT = TestConfig.getCOUNT_TEXT();
    private static final String NAME_TEXT = TestConfig.getTASK_TEST();


    /**
     * Test method testConnectDatabase -
     * 1) Reference data must be in Db in the correct form!
     * 2) Config data must exist and be valid (are not tested here)!
     */
    @Test
    public void testConnectDatabase() {


        /** Create the reference entity with predefined errors */
        SpiderDataClass spiderDataClassRef = new SpiderDataClass(RUN_TEST, "", "", ITERATION_TEST);
        SpiderErrorClass spiderErrorRef = new SpiderErrorClass();

        ArrayList<SpiderErrorClass> errorListRef = new ArrayList<>();
        { /** The following entities must be in Spider for task, which is defined in configuration */
            spiderErrorRef.setError(ERROR_TEXT);
            spiderErrorRef.setJiraTicket(JIRA_TICKET_TEXT);
            spiderErrorRef.setCount(COUNT_TEXT);
            spiderErrorRef.setTask(NAME_TEXT);
            errorListRef.add(spiderErrorRef);
        }
        spiderDataClassRef.setErrorsList(errorListRef);


        /** Create and request test entity */
        SpiderDbConnector spiderDbConnectorTest = new SpiderDbConnector();
        SpiderDataClass spiderDataClassTest = null;


        try {
            ReadXmlFile rXml = new ReadXmlFile();
            ConfigData configData = rXml.readXmlFile(CONFIG_FILE_NAME, SOURCE_NAME);
            /** Substitute Run name for testing (not depend on XML data)*/
            configData.setRun(RUN_TEST);
            configData.setIteration(ITERATION_TEST);

            spiderDataClassTest = spiderDbConnectorTest.connectDatabase(configData);
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }


        /* Check for null output - in such case next command will not crash - test will fail on this command */
        Assert.assertNotNull(spiderDataClassTest);
        /* Comparison works not on the full set of fields */
        Assert.assertEquals(spiderDataClassTest, spiderDataClassRef);
    }

    /**
     * Test method - example!
     */
    @Test
    public void testMock() {
        Assert.assertTrue(Boolean.TRUE);
    }

}
