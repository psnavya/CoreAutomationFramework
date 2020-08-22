package com.execution;

import com.base.BaseUtil;
import com.helper.ConfigurationHelper;
import com.helper.driver.DriverInstance;
import com.helper.driver.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.testng.Assert.fail;

/**
 * Created by Navya on 23-08-2020.
 */
public class AppDriver extends BaseUtil {
    public static WebDriver Driver = null;
    public static AppiumDriver Driver1 =null;
    public static String projectPath = System.getProperty("user.dir")+"//src//main//resources//";
    public static String driverPath = null;
    public static DriverInstance driverInstance = DriverInstance.INSTANCE;
    //BaseUtil BaseRef = new BaseUtil();

    public void createFirefoxDriver() {
        Driver = new FirefoxDriver();
    }

    public Map<String, String> ExecutionPlatform() throws IOException {
        Map<String,String> configMap = new HashMap<>();
        configMap.put("ExeLoc",readConfigFile("execLocation"));
        configMap.put("ExeDev",readConfigFile("deviceType"));
        configMap.put("DevOS",readConfigFile("deviceOsType"));
        configMap.put("DevBrowser",readConfigFile("browserType"));

        return configMap ;
    }

    public static String readConfigFile(String key){
        try {
            Properties p = new Properties();
            FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "\\Config.properties");
            p.load(fs);
            return (String) p.get(key);
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentOsName() {
        String osName = System.getProperty("os.name").toLowerCase();
        System.out.println(String.format("OS Name --> %s", osName));
        return osName;
    }

    public void createChromeGridDriver() throws IOException {

        File file = null;
        ClassLoader classLoader = DriverManager.class.getClassLoader();


        String osName = getCurrentOsName();

        if (osName.contains("mac")) {
            String macCrFile = readConfigFile("macChromeLoc");
            file = new File(classLoader.getResource(macCrFile).getFile());
        } else if (osName.contains("win")){
            String msCrFile = readConfigFile("msChromeLoc");
            file = new File(classLoader.getResource(msCrFile).getFile());
        } else {
            System.out.print("Unsupported Platform Type");
        }


        DesiredCapabilities capability = DesiredCapabilities.chrome();
        System.setProperty("webdriver.chrome.driver",String.valueOf(file));

        Driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);
    }


    public void createSafariDriver() {

        if (System.getProperty("os.name").toLowerCase().equals("mac")) {
            Driver = new SafariDriver();
        } else {
            fail("Browser is not supported on the selected platform type");
        }
    }

    public void createIEWebdriver() throws IOException {
        File file = null;
        ClassLoader classLoader = DriverManager.class.getClassLoader();

        String osName = getCurrentOsName();

        if (osName.contains("win")){
            String msIEFile = readConfigFile("msIELoc");
            file = new File(classLoader.getResource(msIEFile).getFile());
        } else {
            System.out.print("Unsupported Platform Type");
        }

        System.setProperty("webdriver.ie.driver", String.valueOf(file));
        Driver = new InternetExplorerDriver();
    }

    public void createEdgeWebdriver() throws IOException {
        File file = null;
        ClassLoader classLoader = DriverManager.class.getClassLoader();

        String osName = getCurrentOsName();

        if (osName.contains("win")){
            String msEdgFile = readConfigFile("msEdgeLoc");
            file = new File(classLoader.getResource(msEdgFile).getFile());
        } else {
            System.out.print("Unsupported Platform Type");
        }

        System.setProperty("webdriver.edge.driver", String.valueOf(file));
        DesiredCapabilities capabilities = DesiredCapabilities.edge();
        Driver = new EdgeDriver(capabilities);
    }

    public void createChromeWebdriver() throws IOException {

        File file = null;
        ClassLoader classLoader = DriverManager.class.getClassLoader();

        String osName = getCurrentOsName();

        if (osName.contains("mac")) {
            String macCrmFile = readConfigFile("macChromeLoc");
            file = new File(classLoader.getResource(macCrmFile).getFile());
        } else if (osName.contains("win")){
            String msCrmFile = readConfigFile("msChromeLoc");
            //file = new File(classLoader.getResource(msCrmFile).getFile());
            driverPath = projectPath+"\\"+ msCrmFile;
        } else {
            System.out.print("Unsupported Platform Type");
        }

        DesiredCapabilities capability = DesiredCapabilities.chrome();
        capability.setJavascriptEnabled(true);
        System.setProperty("webdriver.chrome.driver",driverPath);
        driverInstance.setDriver(new ChromeDriver(capability));

        // DriverManager.setDriver(Driver);
    }

    public void createAppiumAndroidChromeDriver() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String AppiumURL = readConfigFile("LOCAL_URL");

        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,"a74e1fa1");
        capabilities.setCapability(MobileCapabilityType.PLATFORM, "ANDROID");
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");

        Driver = new AndroidDriver(new URL(AppiumURL), capabilities);
    }

    public void createAppiumAndroidDriver() throws IOException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","Android");
        capabilities.setCapability("app", System.getProperty("user.dir")+"\\Mobile_Apps\\linkedin-release-7.6.0.apk");
        capabilities.setCapability("appPackage","com.linkedin.mobile");
        capabilities.setCapability("appActivity","com.linkedin.mobile.ui.auth.PreSplashActivity");

        // capabilities.setCapability("full-reset", true);
        capabilities.setCapability("noReset", true);
        StartAppium();
        Driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        DriverInstance.INSTANCE.setDriver(Driver);
    }


    // @Parameters({ "deviceName_"})
    public void createPerfectoDriver() throws IOException {

        System.setProperty("http.proxyHost","192.168.29.200");
        System.setProperty("http.proxyPort","80");
        System.setProperty("https.proxyHost","192.168.29.200");
        System.setProperty("https.proxyPort","80");
        DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);

        String host = "linkedin.perfectomobile.com";
        String User=readConfigFile("PerfectoUserName");
        String password=readConfigFile("Password");
        String model=readConfigFile("DeviceModelNumber");
        String deviceID=readConfigFile("DeviceID");

        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("user", User);
        capabilities.setCapability("password", password);
        capabilities.setCapability("model",model);
        //capabilities.setCapability("deviceName", deviceID);
        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        //PerfectoLabUtils.setExecutionIdCapability(capabilities, host);
        // For Android:
        //capabilities.setCapability("appPackage","com.android.chrome");
        capabilities.setCapability("appPackage", "com.linkedin.mobile");

        //commenting below for android Greyfriars
        //capabilities.setCapability("appActivity", "com.linkedin.mobile.ui.auth.PreSplashActivity");
        //capabilities.setCapability("appActivity", "com.linkedin.mobile.ui.auth.SplashActivity");
        // capabilities.setCapability("appActivity", "com.linkedin.mobile.activity_splash_progress");
        // Name your script
        // capabilities.setCapability("scriptName", "AppiumTest");

        Driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        DriverInstance.INSTANCE.setDriver(Driver);

    }

    public void createPerfectoIOSDriver() throws IOException {
        System.setProperty("http.proxyHost", "192.168.29.200");
        System.setProperty("http.proxyPort", "80");
        System.setProperty("http.proxyPort", "80");
        System.setProperty("https.proxyHost", "192.168.29.200");
        System.setProperty("https.proxyPort", "80");
        DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);

        String host = "linkedin.perfectomobile.com";
        String User=readConfigFile("PerfectoUserName");
        String password=readConfigFile("Password");
        String model=readConfigFile("DeviceModelNumber");
        String deviceID=readConfigFile("DeviceID");


        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        capabilities.setCapability("automationName", "XCUITest"); // XCUITest/Appium
        capabilities.setCapability("user", User);
        capabilities.setCapability("password", password);
        capabilities.setCapability("model", model);
        //capabilities.setCapability("deviceName", deviceID);
        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        //PerfectoLabUtils.setExecutionIdCapability(capabilities, host);
        // Name your script
        // capabilities.setCapability("scriptName", "AppiumTest");
        // capabilities.setCapability("bundleId", "com.linkedin.digital.PCAApp");
        // For IOS:

        capabilities.setCapability("bundleId", "com.linkedin.digital.PCAApp");
        //capabilities.setCapability("bundleId", "com.google.chrome.ios");
        /*capabilities.setCapability("appPackage", "com.linkedin.digital.PCAApp");
        capabilities.setCapability("appActivity", "com.linkedin.mobile.ui.auth.PreSplashActivity");*/
        Driver = new IOSDriver<>(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        // IOSDriver driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        DriverInstance.INSTANCE.setDriver(Driver);
        // driverInstance().navigate().to("www.ukdev.linkedin.org");

    }

    public void StartAppium(){

        String appiumHome = "C:\\Program Files (x86)\\Appium\\node_modules\\appium\\bin\\appium.js";
        String nodeHome = "C:\\Program Files (x86)\\Appium\\node.exe";

        AppiumDriverLocalService service;
        service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().withIPAddress("127.0.0.1")
                .usingPort(4723).usingDriverExecutable(new File(nodeHome))
                .withAppiumJS(new File(appiumHome)));
        // service.AppiumServiceBuilder().usingDriverExecutable(new File(nodeHome))
        //   .withAppiumJS(new File(appiumHome)).withIPAddress(host.split("//")[1]).usingPort(port);

        service.start();

    }

    public void createAppiumAndroidBrowserDriver() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String AppiumURL = readConfigFile("LOCAL_URL");

        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,"a74e1fa1");
        capabilities.setCapability(MobileCapabilityType.PLATFORM, "ANDROID");
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Browser");

        Driver = new AndroidDriver(new URL(AppiumURL), capabilities);
    }

    public void createAppiumAndroidAppDriver() throws IOException {


        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        capabilities.setCapability("deviceName", ConfigurationHelper.getDEVICE_NAME());
        capabilities.setCapability("app", ConfigurationHelper.getAPP_PATH());

        String host = "http://127.0.0.1";
        int port = 4723;
        System.out.println(port);
        System.out.println(host);

        String appiumHome = ConfigurationHelper.getAppiumHome();
        String nodeHome = ConfigurationHelper.getNodeHome();
        System.out.println(appiumHome);
        System.out.println(nodeHome);

        AppiumDriverLocalService service;
        service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().withIPAddress("127.0.0.1").usingPort(port).usingDriverExecutable(new File(nodeHome)).withAppiumJS(new File(appiumHome)));
        service.start();
        //Thread.sleep(10000);
//      driver.quit();
//      service.stop();

        Driver = new AndroidDriver(new URL(host + ":" + port + "/wd/hub"), capabilities);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //driver.quit();
        //service.stop();
    }


    public static void createPerfectoIosBroswer() throws MalformedURLException {
        String browserName = "mobileOS";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        String host = "linkedin.perfectomobile.com";
        capabilities.setCapability("user", "darrell.woolgar@linkedin.com");
        capabilities.setCapability("password", "Password");
        capabilities.setCapability("deviceName", "3B2C550A2B8F7F66030FD0F4210287D757DF0DC9");


        Driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
    }


    public static void createPerfectoAndroidBroswer() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
        String host = "linkedin.perfectomobile.com";
        capabilities.setCapability("user", "darrell.woolgar@linkedin.com");
        capabilities.setCapability("password", "Password");
        capabilities.setCapability("deviceName", "988627454935584633");

        Driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
    }




}