package com.mobile.helper;

import com.web.helper.ConfigurationHelper;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Capabilities {

	public static void main(String[] args) {

		System.out.println("Started");

		try {
			AndroidDriver driver;
			System.out.println("asd");
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(CapabilityType.BROWSER_NAME, ConfigurationHelper.getBROWSER_NAME());
			capabilities.setCapability("deviceName", "00fd2d9c5fb4cf04");
			capabilities.setCapability("app", "D:\\Softwares\\linkedin-release-7.4.2.apk");


			String host = "http://127.0.0.1";
			int port = 4723;
			System.out.println(port);
			System.out.println(host);

			String appiumHome = "C:\\Program Files (x86)\\Appium\\node_modules\\appium\\bin\\appium.js";
			String nodeHome = "C:\\Program Files (x86)\\Appium\\node.exe";
			System.out.println(appiumHome);
			System.out.println(nodeHome);

			AppiumDriverLocalService service;

			service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().withIPAddress("127.0.0.1").usingPort(port).usingDriverExecutable(new File(nodeHome)).withAppiumJS(new File(appiumHome)));

			service.start();
			driver = new AndroidDriver(new URL(host + ":" + port + "/wd/hub"), capabilities);
			Thread.sleep(10000);
			driver.quit();
			service.stop();

			System.out.println("Appum Server Stopped");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
