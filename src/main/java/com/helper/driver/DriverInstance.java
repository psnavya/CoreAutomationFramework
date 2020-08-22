package com.helper.driver;

import org.openqa.selenium.WebDriver;

/**
 * Created by Navya on 23-08-2020.
 */
public enum DriverInstance {

    INSTANCE;

    public WebDriver getDriver() {
        return driver;
    }
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
    private WebDriver driver;

}