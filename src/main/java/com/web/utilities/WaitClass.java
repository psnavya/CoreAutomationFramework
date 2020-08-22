package com.web.utilities;

import com.web.base.MobileBaseUtil;
import com.web.helper.testDataHandling.ObjectRepositoryReader;
import com.web.helper.driver.DriverInstance;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class WaitClass extends MobileBaseUtil {

	private static long defaultTime = 60;
	public static long DEFAULT_SLEEP_BETWEEN_POLLS_MILLIS = 1;
	public static long DEFAULT_MAX_WAIT_TIME_SECONDS = 50;
	protected long sleepBetweenPollsMillis = DEFAULT_SLEEP_BETWEEN_POLLS_MILLIS;
	public static long maxWaitTimeSeconds = DEFAULT_MAX_WAIT_TIME_SECONDS;
	public static WebDriverWait wait ; //= new WebDriverWait(DriverInstance.INSTANCE.getDriver(), maxWaitTimeSeconds);
	
	
	
	public static void setWait(WebDriverWait wait) {
		WaitClass.wait = wait;
	}

	public boolean waitUntilLocated(final String objName) {

		final String maxWaitTimeSeconds = "20";
		return waitUntilLocated(objName, maxWaitTimeSeconds);
	}
	

	public void waitFluent(String objName) {
		
		try {
			FluentWait<WebDriver> waitFlu = new WebDriverWait(DriverInstance.INSTANCE.getDriver(), WaitClass.maxWaitTimeSeconds);
			waitFlu = new FluentWait<WebDriver>(DriverInstance.INSTANCE.getDriver()).withTimeout(DEFAULT_MAX_WAIT_TIME_SECONDS, TimeUnit.SECONDS).pollingEvery((long) 0.5, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			waitFlu.until(ExpectedConditions.visibilityOfElementLocated(ObjectRepositoryReader.getLocator(objName)));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}


	public void validateLocated(String objName) {
		
		boolean ans = false;
		try {
			waitUntilLocated(objName, "" + maxWaitTimeSeconds);
			loggerChild.log(LogStatus.PASS, "Element " + objName + " is located");
			ans = true;
			Assert.assertTrue(ans);
		} catch (final Exception e) {
			loggerChild.log(LogStatus.FAIL, "Unable to locate element");
			e.printStackTrace();
			Assert.assertTrue(ans);
		}
	}

	public boolean waitUntilLocated(final String objName, final String maxWaitTimeSeconds, final String... skip) {

		boolean ans = true;
		try {
			if (maxWaitTimeSeconds != null && !maxWaitTimeSeconds.trim().equals("")) {
				//wait.until(ExpectedConditions.presenceOfElementLocated(ORreader.getLocator(objName)));
				//wait.until(ExpectedConditions.visibilityOfElementLocated(ORreader.getLocator(objName)))){
				if(driverInstance().findElements(ObjectRepositoryReader.getLocator(objName)).size() != 0)
					loggerChild.log(LogStatus.PASS, "Waited until " + objName + " element is located");
				else
					loggerChild.log(LogStatus.FAIL, "Unable to find element " + objName);

				//}
			}
		} catch (final Exception e) {
			ans = false;
		}
		return ans;
	}
	
	public void sleep(final double seconds) {

		sleep("" + seconds);
	}
	
	public void sleep(final String seconds) {

		if(seconds == "default"){
			sleep("1", "");
		}else
			sleep(seconds, "");
	}
	
	public void sleep(final String seconds, final String skip) {

		try {
			Thread.sleep((long) (Double.parseDouble(seconds) * 1000));
		} catch (final Exception e) {
		}
	}
}