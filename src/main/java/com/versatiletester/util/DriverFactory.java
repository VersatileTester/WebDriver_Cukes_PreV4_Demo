package com.versatiletester.util;

import com.versatiletester.cukes.TestBase;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverFactory {
    public static Logger log = LoggerFactory.getLogger(DriverFactory.class);

    private static ThreadLocal<RemoteWebDriver> threadSafeDriver = new ThreadLocal<>();

    private static String LOCAL_DRIVER_BINARY_PATH = "src/main/resources/binaries/";
    private static String CHROME_DRIVER_VERSION = "2.43";
    private static String GECKO_DRIVER_VERSION = "0.23.0";
    private static String IE_DRIVER_VERSION = "3.150.1";
    private static String EDGE_DRIVER_VERSION = "85.0.557.0";


    public enum LocalBrowser {
        FIREFOX("firefox"),
        CHROME("chrome"),
        IE("ie"),
        EDGE("edge");

        private String description;

        LocalBrowser(String browser) {
            this.description = browser;
        }

        public String toString() {
            return this.description;
        }

        public static LocalBrowser getMatch(String text) {
            for (LocalBrowser browser : LocalBrowser.values()) {
                if (browser.toString().equalsIgnoreCase(text)) {
                    return browser;
                }
            }
            throw new UnsupportedOperationException("Browser type " + text + " unsupported.");
        }
    }

    public synchronized static void setLocalDriver(LocalBrowser browser) {
        switch (browser) {
            case CHROME: {
                threadSafeDriver = ThreadLocal.withInitial(() -> getChromeDriver());
                break;
            }
            case FIREFOX: {
                threadSafeDriver = ThreadLocal.withInitial(() -> getFirefoxDriver());
                break;
            }
            case IE: {
                threadSafeDriver = ThreadLocal.withInitial(() -> getIEDriver());
                break;
            }
            case EDGE: {
                threadSafeDriver = ThreadLocal.withInitial(() -> getEdgeDriver());
                break;
            }
            default:
                throw new UnsupportedOperationException("Local Browser '" + browser + "' is not configured.");
        }
    }

    public synchronized static void setRemoteDriver(String url, Capabilities caps) {
        try {
            threadSafeDriver.set(new RemoteWebDriver(new URL(url), caps));
            initDriverConfig(threadSafeDriver.get(), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public synchronized static RemoteWebDriver getDriver() {
        return threadSafeDriver.get();
    }


    public synchronized static void closeDriver() {
        threadSafeDriver.get().quit();
    }

    private static FirefoxDriver getFirefoxDriver() {
        System.clearProperty("webdriver.gecko.driver");

        if (SystemUtils.IS_OS_MAC) {
            LOCAL_DRIVER_BINARY_PATH += "geckodriver-v" + GECKO_DRIVER_VERSION + "-macos/geckodriver";
        } else if (SystemUtils.IS_OS_LINUX) {
            LOCAL_DRIVER_BINARY_PATH += "geckodriver-v" + GECKO_DRIVER_VERSION + "-linux64/geckodriver";
        } else if (SystemUtils.IS_OS_WINDOWS) {
            LOCAL_DRIVER_BINARY_PATH += "geckodriver-v" + GECKO_DRIVER_VERSION + "-win64/geckodriver.exe";
        }

        File f = new File(LOCAL_DRIVER_BINARY_PATH);
        System.setProperty("webdriver.gecko.driver", f.getAbsolutePath());

        FirefoxDriver driver = new FirefoxDriver(new FirefoxOptions());
        initDriverConfig(driver, null);
        return driver;
    }

    private static ChromeDriver getChromeDriver() {
        System.clearProperty("webdriver.chrome.driver");

        if (SystemUtils.IS_OS_MAC) {
            LOCAL_DRIVER_BINARY_PATH += "chromedriver-v" + CHROME_DRIVER_VERSION + "-mac64/chromedriver";
        } else if (SystemUtils.IS_OS_LINUX) {
            LOCAL_DRIVER_BINARY_PATH += "chromedriver-v" + CHROME_DRIVER_VERSION + "-linux64/chromedriver";
        } else if (SystemUtils.IS_OS_WINDOWS) {
            LOCAL_DRIVER_BINARY_PATH += "chromedriver-v" + CHROME_DRIVER_VERSION + "-win32/chromedriver.exe";
        }

        File f = new File(LOCAL_DRIVER_BINARY_PATH);
        System.setProperty("webdriver.chrome.driver", f.getAbsolutePath());

        ChromeDriver driver = new ChromeDriver();
        initDriverConfig(driver, null);
        return driver;
    }

    private static InternetExplorerDriver getIEDriver() {
        if (SystemUtils.IS_OS_WINDOWS) {
            LOCAL_DRIVER_BINARY_PATH += "iedriverserver-v" + IE_DRIVER_VERSION + "-win32/IEDriverServer.exe";
        }

        File f = new File(LOCAL_DRIVER_BINARY_PATH);
        DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
//        caps.setCapability(CapabilityType.PLATFORM_NAME,"windows");
        caps.setCapability(CapabilityType.VERSION, "10");
        caps.setCapability(CapabilityType.BROWSER_VERSION, "11");
        caps.setCapability("ignoreProtectedModeSettings",1);
        caps.setCapability(InternetExplorerDriver.NATIVE_EVENTS,false);
        caps.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        caps.setCapability(InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR, 1);
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
        InternetExplorerOptions options = new InternetExplorerOptions();
        options.merge(caps);
        options.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        options.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
        options.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);

        System.setProperty("webdriver.ie.driver", f.getAbsolutePath());
        InternetExplorerDriver driver = new InternetExplorerDriver(caps);
        initDriverConfig(driver, null);
        return driver;
    }


    private static EdgeDriver getEdgeDriver() {
        if (SystemUtils.IS_OS_WINDOWS) {
            LOCAL_DRIVER_BINARY_PATH += "edgedriver-v" + EDGE_DRIVER_VERSION + "-win64/msedgedriver.exe";
        }
        File f = new File(LOCAL_DRIVER_BINARY_PATH);
        System.setProperty("webdriver.edge.driver", f.getAbsolutePath());

        EdgeOptions options = new EdgeOptions();
        options.setCapability("useAutomationExtension", false);
        EdgeDriver driver = new EdgeDriver(options);
        initDriverConfig(driver, null);
        return driver;
    }


    public static void resetDriverTimeouts(RemoteWebDriver driver) {
        /** The driver will wait x seconds for every page to load */
        driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
        /** The driver will wait x seconds for every element to be visible
         Increased the timeout to 10 seconds as IE needs more than 5 */
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    /**
     * Method for setting the default driver settings.
     */
    private static void initDriverConfig(RemoteWebDriver driver, Capabilities caps) {
        resetDriverTimeouts(driver);

        //If you're running on
        if (caps == null || caps.getCapability("device") == null) {
            driver.manage().window().maximize();
        }
        if (caps != null && caps.getCapability("os") != null &&
                caps.getCapability("os").toString().equalsIgnoreCase("OS X")) {
            driver.manage().window().fullscreen();
        }
    }
}
