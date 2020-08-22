package com.web.base;

import com.web.execution.AppDriver;
import com.web.helper.testDataHandling.DataHelper;
import com.web.helper.Log;
import com.web.helper.testDataHandling.ObjectRepositoryReader;
import com.web.helper.driver.DriverInstance;
import com.web.helper.driver.DriverManager;
import com.web.helper.FileOperations;
import com.web.execution.MasterXMLSuite;
import com.web.helper.driver.InstanceValidation;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
/*
Appium Related
import io.appium.java_client.AppiumDriver;*/

import com.web.utilities.WaitClass;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.screentaker.ViewportPastingStrategy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.web.helper.testDataHandling.DataHelper.readTestDataSet;

public class MobileBaseUtil {

    public static Map<String, String> testData = new HashMap<String, String>();
    public static String TestName;
    public static DriverManager drv = null;

    public static ExtentReports extent = null;

    public static void setExtent(ExtentReports extent) {
        MobileBaseUtil.extent = extent;
    }

    public static ExtentTest logger = null;
    public static ExtentTest loggerChild = null;
    public static ExtentTest loggerKid = null;

    public static final String TASKLIST = "tasklist";
    public static final String KILL = "taskkill /F /IM ";


    public static String EXTENTREPORTPATH = System.getProperty("user.dir") + "\\test-output\\extentReport\\ExtentReport" + System.currentTimeMillis() + ".html";
    //public static String EXTENTREPORTPATH = System.getProperty("user.dir") + "\\test-output\\extentReport\\ExtentReport";
    public static String currentTestMethod = null;


    public static String methodDescription = "";
    /*public static WebElement verificationElement = null;*/

    public static String SS = "SS";
    public static String before_method_screenshot = "";
    public static String after_method_screenshot = "";

    public static WebDriver driverInstance() {
        return driverInstance();
    }

    //Appium Related
    public static AppiumDriver driverInstanceMobile(){
          return (AppiumDriver) DriverInstance.INSTANCE.getDriver();
      }
    public static void setVerificationElement(WebElement verificationElement) {
        MobileBaseUtil.verificationElement = verificationElement;
    }

    public static WebElement verificationElement = null;
    public static String currentExcelPath = "";
  /*  public ClickActions clickActions = new ClickClass();
    public ClickActions javascriptActions = new Javascript();
  */

    @BeforeSuite
    public static void beforeSuite(ITestContext context) {

  /*AppDriver appdriver = new AppDriver();
        String Devicelist = null;
        try {
            Devicelist = appdriver.readConfigFile("DceviceModelNumber");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] Devicename = Devicelist.split(",");
*/

        // for (int i =0; i<Devicename.length; i++){

        if (MasterXMLSuite.tempMap.get("ExeDev").equalsIgnoreCase("desktop")) {
            if (DriverInstance.INSTANCE.getDriver() == null) {
                drv = new DriverManager();
                DriverInstance.INSTANCE.getDriver().manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
                DriverInstance.INSTANCE.getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                DriverInstance.INSTANCE.getDriver().manage().window().maximize();
                WebDriverWait wait = new WebDriverWait(DriverInstance.INSTANCE.getDriver(), WaitClass.maxWaitTimeSeconds);
                WaitClass.setWait(wait);
            }
            if (InstanceValidation.INSTANCE.getInstanceCount() == 999)
                InstanceValidation.INSTANCE.setInstanceCount(MasterXMLSuite.generatePaths().size());
        } else if (MasterXMLSuite.tempMap.get("ExeDev").equalsIgnoreCase("mobile")) {
            drv = new DriverManager();
            DriverInstance.INSTANCE.getDriver().manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
            if (InstanceValidation.INSTANCE.getInstanceCount() == 999)
                InstanceValidation.INSTANCE.setInstanceCount(MasterXMLSuite.generatePaths().size());
        } else if (MasterXMLSuite.tempMap.get("ExeDev").equalsIgnoreCase("Perfecto")) {
            drv = new DriverManager();

            if (InstanceValidation.INSTANCE.getInstanceCount() == 999)
                InstanceValidation.INSTANCE.setInstanceCount(MasterXMLSuite.generatePaths().size());
        }
        //  }

        //extent = new ExtentReports(EXTENTREPORTPATH + workBookName + ".html", true, NetworkMode.ONLINE);


        String workBookName = context.getSuite().getName();
        currentExcelPath = "\\src\\test\\resources\\DataSheet\\" + workBookName + ".xls";
        setCurrentExcelPath(currentExcelPath);
        DataHelper.TestSet.clear();
        testData.clear();
        readTestDataSet(System.getProperty("user.dir") + currentExcelPath);
        DataHelper.setOutputExcel(currentExcelPath);

        try {
            //VerificationPointsReader.loadVerficationPoint(System.getProperty("user.dir") + currentExcelPath, "Verification");
            ObjectRepositoryReader.getKnownGoodMap(System.getProperty("user.dir") + currentExcelPath);
            String propsPath = System.getProperty("user.dir") + "\\src\\test\\resources\\OR.properties";
            ObjectRepositoryReader.writeToPropertiesFile(propsPath);
            before_method_screenshot = AppDriver.readConfigFile("BEFORE_METHOD");
            after_method_screenshot = AppDriver.readConfigFile("AFTER_METHOD");
        } catch (IOException e) {
            e.printStackTrace();
        }

        extent = new ExtentReports(EXTENTREPORTPATH, false, NetworkMode.OFFLINE);
        extent.addSystemInfo("Release# ", AppDriver.readConfigFile("BUILD_NUMBER"));
        extent.addSystemInfo("Environment: ", AppDriver.readConfigFile("ENVIRONMENT"));
        extent.addSystemInfo("DeviceName: ", AppDriver.readConfigFile("DeviceModelNumber"));
        logger = extent.startTest(workBookName);
    }

    @BeforeTest
    public static void startExtent(ITestContext testContext) {
        TestName = testContext.getName();
        setTestName(TestName);
        Log.startTestCase(testContext.getName());
        //logger = extent.startTest(TestName);
        loggerChild = extent.startTest(testContext.getName());
        //loggerKid = extent.startTest("some");
        logger.appendChild(loggerChild);
        //loggerChild.appendChild(loggerKid);
        for (final Map<String, String> s : DataHelper.TestSet) {
            if (s.containsValue(testContext.getName())) {
                testData.putAll(s);
                break;
            }
        }
    }

    @BeforeMethod
    public static void getMethodName(Method method) {

        currentTestMethod = method.getName();
        Test testValues = method.getAnnotation(Test.class);
        methodDescription = testValues.description();
        if (methodDescription.contains("ss=yes") && before_method_screenshot == "YES")
            attachFullScreenShotToExtentReport("Screen before " + "<b>" + currentTestMethod + "</b>" + " executed");
        /*loggerChild = extent.startTest(method.getName());
        logger.appendChild(loggerChild);*/
    }

    ///////////////////////////CODE TO ATTACH SCREENSHOT TO EXENTREPORT//////////////////////////////////////

    @AfterMethod
    public void afterTestMethod(ITestResult testResult, Method method) {

        currentTestMethod = method.getName();
        System.out.println("Execution completed for " + currentTestMethod);
        Test testValues = method.getAnnotation(Test.class);
        methodDescription = testValues.description();
        if (methodDescription.contains("ss=yes") && after_method_screenshot.contains("YES"))
            attachFullScreenShotToExtentReport("Screen before " + "<b>" + currentTestMethod + "</b>" + " executed");


        if (testResult.getStatus() == ITestResult.FAILURE) {
            try {
                String screenshot_path = captureScreenShot(testResult.getName());
                String encodedPath = FileOperations.encodeFileToBase64Binary(screenshot_path);
                loggerChild.log(LogStatus.FAIL, "Test method " + testResult.getName() + " is <b>FAILED</b>" + loggerChild.addScreenCapture(encodedPath));
                //extent.endTest(loggerChild);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @AfterTest
    public static void endTestScenario(ITestContext context) {
        extent.endTest(loggerChild);
        //extent.endTest(logger);
    }

    @AfterSuite
    public static void afterSuite() {
        extent.endTest(logger);
/*Appium Related*/
        driverInstanceMobile().quit();
        /*extent.flush();
        extent.close();

        InstanceValidation.INSTANCE.setInstanceCount(InstanceValidation.INSTANCE.getInstanceCount() - 1);
        if (InstanceValidation.INSTANCE.getInstanceCount() == 0) {
            DriverManager.getWebDriver().quit();
            FileOperations.afterSuite();
        }
        openGeneratedReport();*/
    }

    public static void clearXMLFiles() {
        //extent.endTest(logger);
        driverInstanceMobile().quit();
        extent.flush();
        extent.close();

        InstanceValidation.INSTANCE.setInstanceCount(InstanceValidation.INSTANCE.getInstanceCount() - 1);
        if (InstanceValidation.INSTANCE.getInstanceCount() == 0) {
            DriverManager.getWebDriver().quit();
            FileOperations.afterSuite();
        }
        openGeneratedReport();
    }

    public static String getTestName() {
        return TestName;
    }

    public static void setTestName(String testName) {
        TestName = testName;
    }

    public static String getCurrentExcelPath() {
        return currentExcelPath;
    }

    public static void setCurrentExcelPath(String currentExcelPath) {
        MobileBaseUtil.currentExcelPath = currentExcelPath;
    }

    public static String takeFullScreenShot(String testMethodName) throws IOException {

        Date date = new Date();
        String DestPath = System.getProperty("user.dir") + "/ScreenShots/" + testMethodName + System.currentTimeMillis() + ".png";
        Screenshot screenshot = new AShot().shootingStrategy(new ViewportPastingStrategy(500)).takeScreenshot(driverInstance());
        ImageIO.write(screenshot.getImage(), "PNG", new File(DestPath));
        return DestPath;
    }

    public static void attachFullScreenShotToExtentReport(String toDisplay) {

        try {
            String screenshot_path = takeFullScreenShot(currentTestMethod);
            String encodedPath = FileOperations.encodeFileToBase64Binary(screenshot_path);
            loggerChild.log(LogStatus.PASS, toDisplay + loggerChild.addScreenCapture(encodedPath));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String captureScreenShot(String testCaseName) {

        String DestPath = System.getProperty("user.dir") + "/ScreenShots/" + testCaseName + System.currentTimeMillis() + ".png";
        try {
            // Take screenshot and store as a file format
            File src = ((TakesScreenshot) driverInstance()).getScreenshotAs(OutputType.FILE);
            // now copy the  screenshot to desired location using copyFile //method
            FileUtils.copyFile(src, new File(DestPath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return DestPath;
    }

    public static void attachScreenShotToExtentReport(String toDisplay) {

        try {
            String screenshot_path = captureScreenShot(currentTestMethod);
            String encodedPath = FileOperations.encodeFileToBase64Binary(screenshot_path);
            loggerChild.log(LogStatus.PASS, toDisplay + loggerChild.addScreenCapture(encodedPath));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void validProcessStatus() {

        try {
            if (isProcessRunning("iexplore.exe") == true) {
                do {
                    killProcess("iexplore.exe");
                } while (isProcessRunning("iexplore.exe") == true);
            }
            if (isProcessRunning("chromedriver.exe") == true) {
                do {
                    killProcess("chromedriver.exe");
                } while (isProcessRunning("chromedriver.exe") == true);
            }
            if (isProcessRunning("firefox.exe") == true) {
                do {
                    killProcess("firefox.exe");
                } while (isProcessRunning("firefox.exe") == true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isProcessRunning(String serviceName) throws Exception {


        try {
            Process p = Runtime.getRuntime().exec(TASKLIST);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(serviceName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void killProcess(String serviceName) throws Exception {

        Runtime.getRuntime().exec(KILL + serviceName);
    }

    public static void openGeneratedReport() {
        try {
            File htmlFile = new File(EXTENTREPORTPATH);
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
