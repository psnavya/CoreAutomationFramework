package com.mobile.helper;
import com.mobile.thirdParty.PerfectoLabUtils;
import com.mobile.thirdParty.WindTunnelUtils;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AppiumTest {

    public static void main(String[] args) throws IOException {
        System.out.println("Run started");

        String browserName = "mobileOS";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        String host = "linkedin.perfectomobile.com";
        capabilities.setCapability("user", "newid@gmail.com");
        capabilities.setCapability("password", "Password");

        //TODO: Change your device ID
        capabilities.setCapability("deviceName", "833369D7E86C3A6965AF7AE6427D714AE2DB6843");
        capabilities.setCapability("model", "iPhone.*");
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "[89].*");
        
        System.setProperty("http.proxyHost", "192.168.29.200");
        System.setProperty("http.proxyPort", "80"); 
        
        // Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
        capabilities.setCapability("automationName", "Appium");

        // Call this method if you want the script to share the devices with the Perfecto Lab plugin.
        PerfectoLabUtils.setExecutionIdCapability(capabilities, host);

        capabilities.setCapability("bundleId", "com.linkedin.digital.PCAApp");

               // Name your script
        // capabilities.setCapability("scriptName", "AppiumTest");

        //AndroidDriver driver = new AndroidDriver(new URL("http://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        IOSDriver driver = new IOSDriver(new URL("http://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

       // switchToContext(driver, "NATIVE_APP");
    	
        //Important
        // Add a persona to your script (see https://community.perfectomobile.com/posts/1048047-available-personas)
        capabilities.setCapability(WindTunnelUtils.WIND_TUNNEL_PERSONA_CAPABILITY, WindTunnelUtils.GEORGIA);

    
        
        //Start Timer
        Map<String, Object> paramtimer = new HashMap<>();
        Object result = driver.executeScript("mobile:monitor:start", paramtimer); 
        
        driver.launchApp();


        //Accept Terms and conditions
 /*       if(driver.findElementByXPath("//*[@label=\"Accept\"]").isDisplayed())
        {
        	driver.findElementByXPath("//*[@label=\"Accept\"]").click();
        }
  */      
        
        WindTunnelUtils.pointOfInterest(driver,"Point of Interest 1: Successfully invoked the app", WindTunnelUtils.SUCCESS);
        
        driver.context("NATIVE_APP");
        driver.findElementByXPath("//AppiumAUT/UIAApplication[1]/UIAWindow[1]/UIASecureTextField[8]").sendKeys("9");
        driver.findElementByXPath("//AppiumAUT/UIAApplication[1]/UIAWindow[1]/UIASecureTextField[9]").sendKeys("1");
        driver.findElementByXPath("//AppiumAUT/UIAApplication[1]/UIAWindow[1]/UIASecureTextField[10]").sendKeys("1");
        driver.findElementByXPath("//AppiumAUT/UIAApplication[1]/UIAWindow[1]/UIASecureTextField[11]").sendKeys("1");
        driver.findElementByXPath("//AppiumAUT/UIAApplication[1]/UIAWindow[1]/UIASecureTextField[12]").sendKeys("1");
        
        
      //Dismiss Balance peek tutorial
     /*   if(driver.findElementByXPath("//*[@label=\"No Thanks Button\"]").isEnabled())
        {
        	driver.findElementByXPath("//*[@label=\"No Thanks Button\"]").click();
        }
     */   
        
        if(driver.findElementByXPath("//*[@label=\"Switch to List View\"]").isEnabled())
        {
        	driver.findElementByXPath("//*[@label=\"Switch to List View\"]").click();
        }

        WindTunnelUtils.pointOfInterest(driver,"Point of Interest 2: Successfully logged in using passcode", WindTunnelUtils.SUCCESS);
        
        //capabilities.setCapability(WindTunnelUtils.WIND_TUNNEL_PERSONA_CAPABILITY, WindTunnelUtils.ROSS);    
    	driver.findElementByXPath("//*[@name=\"ListBackgroundCurrentAccount\"]").click();
        
        
        //Navigate to Payment screen
    	driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.findElementByName("Move money").click();
        driver.findElementByName("Payments").click();

        WindTunnelUtils.pointOfInterest(driver,"Point of Interest 3: Navigate to Payment screen", WindTunnelUtils.SUCCESS);
        
        if(driver.findElementByXPath("//*[@label=\"Add a new payee\"]").isEnabled())
        {
        	driver.findElementByXPath("//*[@label=\"Add a new payee\"]").click();
        }
        
        driver.findElementByXPath("//*[@label=\"Full Name\"]").sendKeys("Perfecto5");
        driver.findElementByXPath("//*[@label=\"Nickname\"]").sendKeys("Perfectonick5");
        driver.findElementByXPath("//*[@label=\"Sort Code\"]").sendKeys("010004");
        driver.findElementByXPath("//*[@label=\"Account Number\"]").sendKeys("11645598");
        driver.findElementByXPath("//*[@label=\"Reference\"]").sendKeys("Perfectoreference4");
        driver.findElementByXPath("//*[@label=\"Next\"]").click();
        
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        
        
        
        if(driver.findElementByXPath("//*[@label=\"Add payee\"]").isEnabled())
        {
        	driver.findElementByXPath("//*[@label=\"Add payee\"]").click();
        }
        
        
        if(driver.findElementByXPath("//*[@label=\"Enter your passcode\"]").isEnabled())
        {
        	driver.findElementByXPath("//*[@label=\"Enter your passcode. 1st digit\"]").sendKeys("9");
        	driver.findElementByXPath("//*[@label=\"2nd digit\"]").sendKeys("1");
        	driver.findElementByXPath("//*[@label=\"3rd digit\"]").sendKeys("1");
        	driver.findElementByXPath("//*[@label=\"4th digit\"]").sendKeys("1");
        	driver.findElementByXPath("//*[@label=\"5th digit\"]").sendKeys("1");
        }
        
        if(driver.findElementByXPath("//*[@label=\"Make a payment now?\"]").isEnabled())
        {
        	driver.findElementByXPath("//*[@label=\"No\"]").click();
        	System.out.println("New Payee Added");
        }
        
        
        driver.findElementByXPath("//*[@label=\"Close\"]").click();
        driver.findElementByXPath("//*[@label=\"Menu\"]").click();
        driver.findElementByXPath("//*[@label=\"Log out\"]").click();
        System.out.println("Successfully added a Payee and logged out");
        
        
        
//   =================================================Delete Payee===================================================================     
        
        //Accept Terms and conditions
        /*   if(driver.findElementByXPath("//*[@label=\"Accept\"]").isDisplayed())
           {
           	driver.findElementByXPath("//*[@label=\"Accept\"]").click();
           }
        */
        

        driver.findElementByXPath("//AppiumAUT/UIAApplication[1]/UIAWindow[1]/UIASecureTextField[8]").sendKeys("9");
        driver.findElementByXPath("//AppiumAUT/UIAApplication[1]/UIAWindow[1]/UIASecureTextField[9]").sendKeys("2");
        driver.findElementByXPath("//AppiumAUT/UIAApplication[1]/UIAWindow[1]/UIASecureTextField[10]").sendKeys("0");
        driver.findElementByXPath("//AppiumAUT/UIAApplication[1]/UIAWindow[1]/UIASecureTextField[11]").sendKeys("1");
        driver.findElementByXPath("//AppiumAUT/UIAApplication[1]/UIAWindow[1]/UIASecureTextField[12]").sendKeys("3");
        
        
        WindTunnelUtils.pointOfInterest(driver,"Point of Interest D1: Login Successfully", WindTunnelUtils.SUCCESS);

        //Dismiss Balance peek tutorial
       /*   if(driver.findElementByXPath("//*[@label=\"No Thanks Button\"]").isEnabled())
          {
          	driver.findElementByXPath("//*[@label=\"No Thanks Button\"]").click();
          }
       */
        
        //capabilities.setCapability(WindTunnelUtils.WIND_TUNNEL_PERSONA_CAPABILITY, WindTunnelUtils.GEORGIA);    


        if(driver.findElementByXPath("//*[@label=\"Switch to List View\"]").isEnabled())
        {
        	driver.findElementByXPath("//*[@label=\"Switch to List View\"]").click();
        }

        WindTunnelUtils.pointOfInterest(driver,"Point of Interest D2: Select Current Account", WindTunnelUtils.SUCCESS);
        
        
    	driver.findElementByXPath("//*[@name=\"ListBackgroundCurrentAccount\"]").click();
        
        
        //Navigate to Payment screen
    	driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.findElementByName("Move money").click();
        driver.findElementByName("Payments").click();

        
        
        WindTunnelUtils.pointOfInterest(driver,"Point of Interest D3: Navigate to Payment screen", WindTunnelUtils.SUCCESS);

        
        driver.findElementByXPath("//*[@label=\"Delete\"]").click();
        
        driver.findElementByXPath("//*[@label=\"Delete\"]").click();

        WindTunnelUtils.pointOfInterest(driver,"Point of Interest D4: Delete Payee optoin displayed ", WindTunnelUtils.SUCCESS);

        driver.findElementByXPath("//*[@label=\"Confirm deletion?\"]").click();
        
        driver.findElementByXPath("//*[@label=\"Yes\"]").click();
        
        WindTunnelUtils.pointOfInterest(driver,"Point of Interest D5: Deleted Successfully", WindTunnelUtils.SUCCESS);

        driver.findElementByXPath("//*[@label=\"Done\"]").click();
        
        
        
        driver.findElementByXPath("//*[@label=\"Close\"]").click();
        driver.findElementByXPath("//*[@label=\"Menu\"]").click();
        driver.findElementByXPath("//*[@label=\"Log out\"]").click();
        System.out.println("Successfully Deleted a Payee and logged out");
        
        
        
       //Stop the timer
       driver.executeScript("mobile:monitor:stop", paramtimer);  
       driver.quit();
       
       try {
            // write your code here

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Retrieve the URL of the Single Test Report, can be saved to your execution summary and used to download the report at a later point
                String reportURL = (String)(driver.getCapabilities().getCapability(WindTunnelUtils.SINGLE_TEST_REPORT_URL_CAPABILITY));

                //driver.close();

                // In case you want to download the report or the report attachments, do it here.
                 PerfectoLabUtils.downloadReport(driver, "pdf", "D:\\test\\report");
                 PerfectoLabUtils.downloadAttachment(driver, "video", "D:\\test\\report\\video", "flv");
                 PerfectoLabUtils.downloadAttachment(driver, "image", "D:\\test\\report\\images", "jpg");

            } catch (Exception e) {
                e.printStackTrace();
            }

            //driver.quit();
        }

        System.out.println("Run ended");
        
        
    }

	private static void switchToContext(IOSDriver driver, String string) {
		// TODO Auto-generated method stub
		
	}
}
