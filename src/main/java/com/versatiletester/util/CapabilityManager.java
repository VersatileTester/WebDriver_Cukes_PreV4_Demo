package com.versatiletester.util;

import org.openqa.selenium.remote.DesiredCapabilities;

public class CapabilityManager {

    public static DesiredCapabilities generateBrowserStackCapabilities(String os,
                                                                       String osVersion,
                                                                       String browser,
                                                                       String browserVersion,
                                                                       String projectName,
                                                                       String buildNumber,
                                                                       String localID,
                                                                       boolean local) {
        DesiredCapabilities caps = new DesiredCapabilities();

        if(!(os.equalsIgnoreCase("windows")||os.equalsIgnoreCase("OS X"))){
            //Mobile Specific
            caps.setCapability("os_version", osVersion);
            caps.setCapability("device", os);
            caps.setCapability("real_mobile", "true");
        } else{
            //Desktop Specific
            caps.setCapability("os", os);
            caps.setCapability("os_version", osVersion);

            caps.setCapability("browserstack.selenium_version", "3.14.0");

            caps.setCapability("browser", browser.toString());
            caps.setCapability("browser_version", browserVersion);
        }

        //Local Browserstack capability settings
        caps.setCapability("project", projectName);
        caps.setCapability("build", buildNumber);
        caps.setCapability("browserstack.localIdentifier", localID);

        caps.setCapability("browserstack.local", local);
        caps.setCapability("browserstack.timezone", "Europe/London");

        return caps;
    }

    public static DesiredCapabilities generateDefaultChromeCapabilities(String projectName, String buildNumber){
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setBrowserName("chrome");
        if(projectName != null){
            caps.setCapability("project", projectName);
        }
        if(buildNumber != null) {
            caps.setCapability("build", buildNumber);
        }
        return caps;
    }
}