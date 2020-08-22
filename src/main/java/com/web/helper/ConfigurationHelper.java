package com.web.helper;

import org.apache.commons.lang3.StringUtils;

public class ConfigurationHelper {

	private static final String DEFAULT_BROWSER = "chrome";// "ie";//"firefox";
	private static final String DEFAULT_TEST_ENV = "";// TEST URL
	private static final String DEFAULT_DIR = System.getProperty("user.dir");
	private static final String DEFAULT_DRIVER_PATH = DEFAULT_DIR + "\\Drivers";
	private static final String TEST_DATA_PATH = DEFAULT_DIR + "\\TestCase";
	private static final String OR_PATH = DEFAULT_DIR + "/OR.xlsx";// Object
																	// Repo path
	private static final String Screenshot_Path = DEFAULT_DIR + "\\Screenshots";

	// FOR MOBILE
	private static final String DEVICE_NAME = "ANDROID_DEVICE";// MOBILE DEVICE, Android or iOS
	private static final String BROWSER_NAME = "";// MOBILE BROWSER NAME
	private static final String PLATFORM_NAME = "";// ANDROID OR IOS
	private static final String DEFAULT_ANDROID_APP_PATH = DEFAULT_DIR + "\\MobileApps";
	private static final String APPIUM_HOME = "C:\\Program Files (x86)\\Appium\\node_modules\\appium\\bin\\appium.js";// APPIUM HOME PATH
	private static final String NODE_HOME = "C:\\Program Files (x86)\\Appium\\node.exe";// NODE HOME PATH
	private static final String APP_PATH = DEFAULT_DIR + "\\Mobile_Apps\\linkedin-sit6-release-7.5.3.apk";


	public static String getBrowser() {
		return getSystemProperty("Browser", DEFAULT_BROWSER);
	}

	public static String getTestUrl() {
		return getSystemProperty("EnvUrl", DEFAULT_TEST_ENV);
	}

	public static String getDriver() {
		return getSystemProperty("Driver", DEFAULT_DRIVER_PATH);
	}

	public static String getORPath() {
		return getSystemProperty("ORPATH", OR_PATH);
	}

	public static String getTestDataPath() {return getSystemProperty("TestDataPath", TEST_DATA_PATH);}

	public static String getScreenshotPath() {return getSystemProperty("Screenshot_Path", Screenshot_Path);}

	public static String getDEVICE_NAME() {return getSystemProperty("DEVICE_NAME", DEVICE_NAME);}

	public static String getBROWSER_NAME() {
		return getSystemProperty("BROWSER_NAME", BROWSER_NAME);
	}

	public static String getPLATFORM_NAME() {
		return getSystemProperty("PLATFORM_NAME", PLATFORM_NAME);
	}

	public static String getDefaultAndriodAppPath() {return getSystemProperty("DEFAULT_ANDROID_APP_PATH", DEFAULT_ANDROID_APP_PATH);}

	public static String getAppiumHome() {
		return getSystemProperty("APPIUM_HOME", APPIUM_HOME);
	}

	public static String getNodeHome() {
		return getSystemProperty("NODE_HOME", NODE_HOME);
	}

	public static String getAPP_PATH() {
		return getSystemProperty("APP_PATH", APP_PATH);
	}

	public static String getSystemProperty(String property, String defaultValue) {
		final String value = System.getProperties().getProperty(property);
		System.out.println(value);
		return StringUtils.isBlank(value) ? defaultValue : value;
	}

}
