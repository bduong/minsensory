/******************************************************************************/
/*          Copyright MINSensory - Boston University 2011-2012                */
/*                                                                            */
/******************************************************************************/

package testng;

import org.apache.log4j.Logger;
import org.testng.*;

import java.util.ArrayList;
import java.util.List;

public class LoggingListener implements ITestListener, ISuiteListener{

    private Logger logger = Logger.getLogger(LoggingListener.class);
    private int passCount = 0;
    private int failedCount = 0;
    private int skippedCount = 0;
    
    private List<String> failedTests = new ArrayList<String>();
    private List<String> skippedTests = new ArrayList<String>();
    

    private void info(String message) {
        logger.info(message);
    }
    
    @Override
    public void onTestStart(ITestResult iTestResult) {
        printLineBreak();
        info("Starting Test: " + buildTestName(iTestResult));
    }

    private void printLineBreak() {
        info("------------------------------------------------------------------------------------------");
    }

    private String buildTestName(ITestResult iTestResult) {
        return iTestResult.getTestClass().getName() + "." + iTestResult.getMethod().getMethodName();
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {        
        reportTestResult(iTestResult, "SUCCESS");
        passCount++;
    }

    private void reportTestResult(ITestResult iTestResult, String status) {
        info("Completed Test: " + buildTestName(iTestResult));
        info("Status: " + status);
        printLineBreak();
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        reportTestResult(iTestResult, "FAILED");
        failedCount++;
        failedTests.add(buildTestName(iTestResult));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        reportTestResult(iTestResult, "SKIPPED");
        skippedCount++;
        skippedTests.add(buildTestName(iTestResult));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        reportTestResult(iTestResult, "FAILED BUT WITHIN SUCCESS PERCENTAGE");
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        printLineBreak();
        info("Starting Test Class: " + iTestContext.getName());
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        info("Finished Test Class: " + iTestContext.getName());
        printLineBreak();
    }

    @Override
    public void onStart(ISuite iSuite) {
        info("Starting Suite: " + iSuite.getName());
    }

    @Override
    public void onFinish(ISuite iSuite) {
        info("Finished Suite: " + iSuite.getName());
        printStats();
    }

    private void printStats() {
        info("Passed : " + passCount);
        info("Failed : " + failedCount);
        info("Skipped : " + skippedCount);
        info("");
        info("Failed Tests:");
        for (String name : failedTests) {
            info("\t" + name);
        }
        info("Skipped Tests:");
        for (String name : skippedTests) {
            info("\t" + name);
        }

    }
}
