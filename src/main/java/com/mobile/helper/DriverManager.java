package com.mobile.helper;

import com.web.execution.AppDriver;
import com.web.helper.driver.DriverInstance;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

/**
 * Created by Navya on 23-08-2020.
 */

public class DriverManager {

    public static AppDriver app = new AppDriver();
    private static DriverManager instance = null;
    private static DriverInstance drvIns = null;
    private static WebDriver Driver = null;
    public static Map<String, String> tempMap = new HashMap<>();

    public void Environments() throws IOException {
        tempMap = app.ExecutionPlatform();
        // AppDriver.setTempMap();
    }

    public DriverManager() {

        Driver = getDriver();
    }

    public static WebDriver getWebDriver() {
        return Driver;
    }


    public WebDriver getDriver() {
        try {
            Environments();

            if (tempMap.get("ExeLoc").equalsIgnoreCase("local")) {
                if (tempMap.get("ExeDev").equalsIgnoreCase("desktop")) {
                    switch (tempMap.get("DevBrowser")) {
                        case "firefox":
                            app.createFirefoxDriver();
                            break;
                        case "chrome":
                            app.createChromeWebdriver();
                            break;
                        case "safari":
                            app.createSafariDriver();
                            break;
                        case "IE11":
                            app.createIEWebdriver();
                            break;
                        case "Edge":
                            app.createEdgeWebdriver();
                            break;
                        case "firefoxGrid":
                            app.createChromeGridDriver();
                            break;
                        default:
                            fail(String.format("%s is an unknown browser type", tempMap.get("DevBrowser")));
                            break;
                    }
                } else if (tempMap.get("ExeDev").equalsIgnoreCase("mobile")) {
                    if (tempMap.get("DevOS").equalsIgnoreCase("android")) {
                        switch (tempMap.get("DevBrowser").toUpperCase()) {
                            case "BROWSER":
                                app.createAppiumAndroidBrowserDriver();
                                break;
                            case "CHROME":
                                app.createAppiumAndroidChromeDriver();
                                break;

                            case "APPIUM":
                                app.createAppiumAndroidDriver();
                                break;
                        }
                    }
                }
                else if (tempMap.get("ExeDev").equalsIgnoreCase("perfecto")) {
                    switch (tempMap.get("DevOS").toUpperCase()){

                        case "ANDROID": app.createPerfectoDriver();
                            break;
                        case "IOS": app.createPerfectoIOSDriver();
                            break;
                    }

                }
            }

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return AppDriver.driverInstance.getDriver();

    }

    public static DriverManager getInstance() throws IOException {
        if (instance == null) {
            instance = new DriverManager();
        }
        return instance;
    }

}
