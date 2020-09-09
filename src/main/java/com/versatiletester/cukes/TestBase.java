package com.versatiletester.cukes;

import com.google.common.base.Preconditions;
import com.versatiletester.util.CapabilityManager;
import com.versatiletester.util.DriverFactory;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public abstract class TestBase {
    public static Logger logger = LoggerFactory.getLogger(TestBase.class);

    private static ThreadLocal<Properties> propertiesWrapper = new ThreadLocal<>();
    private static Properties properties;

    public static RemoteWebDriver driver;

    public static String getProperty(String propertyName){
        return properties.getProperty(propertyName);
    }

    //Config/Property names
    public static final String GOOGLE_URL_PROPERTY_NAME = "google.base.url";
    public static final String MAVEN_PROFILE_PROPERTY_NAME = "maven.profile";
    public static final String GRID_URL_PROPERTY_NAME = "grid.url";
    public static final String LOCAL_ID_PROPERTY_NAME = "local.id";
    public static final String PROJECT_NAME_PROPERTY_NAME = "projectname";
    public static final String BUILD_NUM_PROPERTY_NAME = "build";

    protected static String BROWSER_PROPERTY_NAME = "browser";
    protected static String BSTACK_URL_PROPERTY_NAME = "browserstack.url";
    protected static String BSTACK_LOCAL_BOOL_PROPERTY_NAME = "browserstack.local";

    public enum Environments{
        LOCAL("local"),
        BROWSERSTACK_LOCAL("bstack"),
        GRID("grid");

        private String description;
        Environments(String browser){this.description = browser;}
        public String toString(){return this.description;}
        public static Environments getMatch(String text){
            for(Environments env : Environments.values()){
                if(env.toString().equalsIgnoreCase(text)){
                    return env;
                }
            }
            return null;
        }
    }

    protected static void setupTestContext(){
        if (properties == null) {

            Environments env = Environments.getMatch(System.getProperty(MAVEN_PROFILE_PROPERTY_NAME));
            properties = new Properties();

            Preconditions.checkArgument(env != null);
            try {
                properties.load(new FileReader(new File("src/main/resources/config/" + env.toString() + ".config.properties")));

                // Any command line arguments should be defined here to override the environment config
                if (System.getProperty(GOOGLE_URL_PROPERTY_NAME) != null) {
                    properties.setProperty(GOOGLE_URL_PROPERTY_NAME, System.getProperty(GOOGLE_URL_PROPERTY_NAME));
                }
                if (System.getProperty(BSTACK_URL_PROPERTY_NAME) != null) {
                    properties.setProperty(BSTACK_URL_PROPERTY_NAME, System.getProperty(BSTACK_URL_PROPERTY_NAME));
                }
                if (System.getProperty(GRID_URL_PROPERTY_NAME) != null) {
                    properties.setProperty(GRID_URL_PROPERTY_NAME, System.getProperty(GRID_URL_PROPERTY_NAME));
                }
                if (System.getProperty(LOCAL_ID_PROPERTY_NAME) != null) {
                    properties.setProperty(LOCAL_ID_PROPERTY_NAME, System.getProperty(LOCAL_ID_PROPERTY_NAME));
                }
                if (System.getProperty(PROJECT_NAME_PROPERTY_NAME) != null) {
                    properties.setProperty(PROJECT_NAME_PROPERTY_NAME, System.getProperty(PROJECT_NAME_PROPERTY_NAME));
                }
                if (System.getProperty(BROWSER_PROPERTY_NAME) != null) {
                    properties.setProperty(BROWSER_PROPERTY_NAME, System.getProperty(BROWSER_PROPERTY_NAME));
                }
                if (System.getProperty(BUILD_NUM_PROPERTY_NAME) != null) {
                    properties.setProperty(BUILD_NUM_PROPERTY_NAME, System.getProperty(BUILD_NUM_PROPERTY_NAME));
                } else {
                    properties.setProperty(BUILD_NUM_PROPERTY_NAME,
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")));
                }

                properties.setProperty(MAVEN_PROFILE_PROPERTY_NAME, env.toString());
                propertiesWrapper.set(properties);

                String os = null;
                String osVersion = null;
                String browser = null;
                String browserVersion = null;

                switch (properties.getProperty(BROWSER_PROPERTY_NAME)) {
                    case "winIE": {
                        os = "Windows";
                        osVersion = "10";
                        browser = "Internet Explorer";
                        browserVersion = "";
                        break;
                    }
                    case "winEdge": {
                        os = "Windows";
                        osVersion = "10";
                        browser = "Edge";
                        browserVersion = "";
                        break;
                    }
                    case "winChrome": {
                        os = "Windows";
                        osVersion = "10";
                        browser = "Chrome";
                        browserVersion = "";
                        break;
                    }
                    case "winFF": {
                        os = "Windows";
                        osVersion = "10";
                        browser = "Firefox";
                        browserVersion = "";
                        break;
                    }
                    case "osxSafari": {
                        os = "OS X";
                        osVersion = "Mojave";
                        browser = "Safari";
                        browserVersion = "";
                        break;
                    }
                    case "osxChrome": {
                        os = "OS X";
                        osVersion = "Mojave";
                        browser = "Chrome";
                        browserVersion = "";
                        break;
                    }
                    case "osxFF": {
                        os = "OS X";
                        osVersion = "Mojave";
                        browser = "Firefox";
                        browserVersion = "";
                        break;
                    }
                    case "ipadSafari": {
                        os = "iPad Pro";
                        osVersion = "";
                        browser = "Safari";
                        break;
                    }
                    case "samsungChrome": {
                        os = "Samsung Galaxy Note 8";
                        osVersion = "";
                        browser = "Google Chrome";
                        break;
                    }
                }

                /**
                 * Change browser name to chrome.firefox or internet explorer
                 */
                switch (env) {
                    case LOCAL: {
                        DriverFactory.setLocalDriver(DriverFactory.LocalBrowser.getMatch(
                                properties.getProperty(BROWSER_PROPERTY_NAME)));
                        driver = DriverFactory.getDriver();
                        break;
                    }
                    case GRID: {
                        Preconditions.checkArgument(properties.getProperty(GRID_URL_PROPERTY_NAME) != null,
                                "In order to use the selenium grid maven profile, pass in the grid url as " +
                                        "'-Dgrid.url=http://192.168.0.18:4444/hub/wb' or set it in the grid config file.");
                        DriverFactory.setRemoteDriver(
                                properties.getProperty(GRID_URL_PROPERTY_NAME),
                                CapabilityManager.generateDefaultChromeCapabilities(
                                        properties.getProperty(PROJECT_NAME_PROPERTY_NAME),
                                        properties.getProperty(BUILD_NUM_PROPERTY_NAME)));
                        driver = DriverFactory.getDriver();
                        break;
                    }
                    case BROWSERSTACK_LOCAL: {
                        // Null values from mobile devices handled in the 'generateBrowserStackCapabilities' method.
                        DriverFactory.setRemoteDriver(
                                properties.getProperty(BSTACK_URL_PROPERTY_NAME),
                                CapabilityManager.generateBrowserStackCapabilities(
                                        os,
                                        osVersion,
                                        browser,
                                        browserVersion,
                                        properties.getProperty(PROJECT_NAME_PROPERTY_NAME),
                                        properties.getProperty(BUILD_NUM_PROPERTY_NAME),
                                        properties.getProperty(LOCAL_ID_PROPERTY_NAME),
                                        Boolean.parseBoolean(properties.getProperty(BSTACK_LOCAL_BOOL_PROPERTY_NAME))));
                        driver = DriverFactory.getDriver();
                        break;
                    }
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}