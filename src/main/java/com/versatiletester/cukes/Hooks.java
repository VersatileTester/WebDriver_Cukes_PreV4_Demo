package com.versatiletester.cukes;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.versatiletester.util.DriverUtils.getScreenshotBytes;
import static com.versatiletester.util.DriverUtils.takeScreenshot;

public class Hooks extends TestBase {
    public static Logger log = LoggerFactory.getLogger(TestBase.class);

    @Before(order = 1)
    public void before(Scenario scenario) {
        log.info("Starting - " + scenario.getName());
        setupTestContext();
    }

    @After
    public void after(Scenario scenario) {
        log.info(scenario.getName() + " Status - " + scenario.getStatus());

        if (scenario.isFailed()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            scenario.write("Scenario failed: " + scenario.getName() + " at " + dateFormat.format(date));
            log.info("Scenario failed: " + scenario.getName() + " at " + dateFormat.format(date));
            scenario.write("Failing URL: " + driver.getCurrentUrl());
            log.info("Failing URL: " + driver.getCurrentUrl());
            scenario.write("Failing Title: " + driver.getTitle());
            log.info("Failing Title: " + driver.getTitle());

            try {
                scenario.embed(getScreenshotBytes(), "image/png");
                scenario.embed(driver.getPageSource().getBytes(Charsets.UTF_8), "text/html");
            } catch (Exception e) {
                log.info("Failed to embed screenshot, attempting to save externally");
                takeScreenshot("failed_state");
            }
        } else {
            log.info("Scenario passed: " + scenario.getName());
        }
    }
}