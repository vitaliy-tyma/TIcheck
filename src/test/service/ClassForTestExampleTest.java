package service;

import com.luxoft.falcon.service.ClassForTestExample;
import org.junit.Assert;
import org.junit.Test;


public class ClassForTestExampleTest {
    /** Example of test*/

    @Test
    public void testMock() {
        Assert.assertTrue(Boolean.TRUE);
    }

    @Test
    public void testTestMethod() {
        int test_value = 1;
        String test_string = "Test 1.";

        ClassForTestExample gd = new ClassForTestExample();

        Assert.assertNotNull(test_string);
        Assert.assertEquals(test_string, gd.testMethod(test_value));

        test_value = 0;
        test_string = "Test 0.";
        Assert.assertNotNull(test_string);
        Assert.assertEquals(test_string, gd.testMethod(test_value));

        test_value = 2;
        //test_string = "Test 2.";
        test_string = "Test xxx.";
        Assert.assertNotNull(test_string);
        Assert.assertEquals(test_string, gd.testMethod(test_value));

    }

}
