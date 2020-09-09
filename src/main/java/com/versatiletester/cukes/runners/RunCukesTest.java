package com.versatiletester.cukes.runners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import com.versatiletester.util.DriverFactory;

/**
 * <p>Cucumber run class</p>
 *
 * <p>Sets the tags to be used within the integration (build) tests,
 * where the classname must contain the phrase 'test' to be picked
 * up in the build Junit runner.</p>
 *
 * <p>Also configures the logging/output cucumber settings, specifies
 * the location of the feature files and implements functionality
 * which requires the @BeforeSuite and @AfterSuite annotations defined in
 * other classes (e.g. Closing the RemoteWebDriver instance after
 * ALL tests have run).</p>
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(
		monochrome = true,
		plugin = {	"html:target/cukes/cucumber-html-report",
				"json:target/cukes/cucumber.json",
				"pretty:target/cukes/cucumber-pretty.txt",
				"usage:target/cukes/cucumber-usage.json",
				"pretty"
		},
		features = "src/main/resources/features",
		glue={"com.versatiletester.cukes"},
		tags={"@All","~@Ignore"}
)
public class RunCukesTest {
	/**
	 * close the RemoteWebDriver after ALL tests are run -
	 * closing after each test causes numerous problems and would lead to
	 * a massive runtime overhead (destroying and recreating the browser
	 * instance on every test).
	 *
	 * Only works on LOCAL TEST RUNS due to Cucumber Parallel running
	 **/
	@AfterClass
	public static void afterClass(){
		DriverFactory.closeDriver();
	}
}
