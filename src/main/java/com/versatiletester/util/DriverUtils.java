package com.versatiletester.util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


/**
 * Test Utility Class - used to contain all RemoteWebDriver related utility methods.
 */
@SuppressWarnings("unused")
public abstract class DriverUtils {
	public static Logger log = LoggerFactory.getLogger(DriverUtils.class);
	static RemoteWebDriver driver = DriverFactory.getDriver();

	public static boolean elementExists(WebElement element){
		waitUntilPageFullyLoaded();
		// Set a small element timeout because it is a try catch.
		driver.manage().timeouts().implicitlyWait(300, MILLISECONDS);
		try{
			element.isEnabled();
			DriverFactory.resetDriverTimeouts(driver);
			return true;
		} catch(NoSuchElementException e){
			DriverFactory.resetDriverTimeouts(driver);
			return false;
		}
	}

	public static void clickPageTransitionElement(WebElement element){
		scrollToElement(element);
		element.click();
		waitUntilPageFullyLoaded();
	}
	public static void clickPageTransitionElement(By locator) {
		clickPageTransitionElement(driver.findElement(locator));
	}

	/** TODO:Refactor this method to be applyKeyPress(WebElement, KeyPress), adding an Enum for possibilities */
	public static void clearFieldViaKeyPress(By identifier){
		driver.findElement(identifier).click();
		//Wait required to encompass the time it take to perform the click action
		try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
		driver.findElement(identifier)
				.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
	}

	public static void clearFieldAndSendKeys(By identifier, String value){
		scrollToElement(driver.findElement(identifier));
		driver.findElement(identifier).clear();
		//Wait required to encompass the time it take to perform the clear action
		try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		driver.findElement(identifier).sendKeys(value);

		/** 'Internet Explorer is stupid' block.
		 * See the following for more details, however the solution of pressing the TAB key to trigger an onChange
		 * event does not work. Clicking the body element of the page does, however.
		 *
		 * https://stackoverflow.com/questions/17795354/selenium-webdriver-sendkeys-does-not-trigger-onchange-event-in-ie9-0
		 */
		if(driver.getClass() == InternetExplorerDriver.class){
			driver.findElement(By.xpath("//body")).click();

			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
			if(driver.findElement(identifier).getAttribute("value").isEmpty()){
				JavascriptExecutor jse = driver;
				jse.executeScript("arguments[0].value='"+ value +"';", driver.findElement(identifier));

				driver.findElement(By.xpath("//body")).click();
			}
		}
	}

    public static void clickFieldAndSendKeys(By identifier, String value)
	{
		scrollToElement(driver.findElement(identifier));
		driver.findElement(identifier).click();
		//Wait required to encompass the time it take to perform the click action
		try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
		driver.findElement(identifier).sendKeys(value);


		/** 'Internet Explorer is stupid' block.
		 * See the following for more details, however the solution of pressing the TAB key to trigger an onChange
		 * event does not work. Clicking the body element of the page does, however.
		 *
		 * https://stackoverflow.com/questions/17795354/selenium-webdriver-sendkeys-does-not-trigger-onchange-event-in-ie9-0
		 */
		if(driver.getClass() == InternetExplorerDriver.class){
			driver.findElement(By.xpath("//body")).click();

			try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
			if(driver.findElement(identifier).getAttribute("value").isEmpty()){
				JavascriptExecutor jse = driver;
				jse.executeScript("arguments[0].value='"+ value +"';", driver.findElement(identifier));

				driver.findElement(By.xpath("//body")).click();
			}
		}
	}

	/**
	 * Generic utility method which clicks an element based on it's text.
	 *
	 * @param xpathElement - Used to specify an element's type more uniquely
	 * @param text - The search text of the element
	 */
	public static void clickByText(String xpathElement, String text){
		ArrayList<WebElement> elements = (ArrayList<WebElement>) findElementsByText(xpathElement,text);
		scrollToElement(elements.get(0));
		elements.get(0).click();
	}

    public static List<WebElement> findElementsByText(String xpathElement, String text) {
        return driver.findElements(By.xpath("//" + xpathElement +
				"[text()[contains(normalize-space(.),'" + text + "')]]"));
    }

	public static ArrayList<WebElement> getWebElementsByText() {
		return (ArrayList<WebElement>) findElementsByText("button", "Edit");
	}


	/** Will scroll to the element provided, meeting the prerequisite of visibility before interaction. */
	public static void scrollToElement(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	/** Overload method for clickByText which clicks any(the first) matching element */
	public static void clickByText(String text){ clickByText("*",text); }

	/**
	 * Generic utility method which finds any element with the specified search text, returning the element found.
	 *
	 * @param searchString - The text used to search for within the current page (ignoring irregular formatting).
	 * @return WebElement - The element found on the current page, or null if the element is not found.
	 */
	public static boolean isStringVisible(String xpathElement, String searchString){
		try {
			driver.findElement(By.xpath(xpathElement + "[text()[contains(normalize-space(.),'" + searchString + "')]]"));
			return true;
		} catch(NoSuchElementException e){
			log.error(getErrorString(driver, searchString));
			return false;
		}
	}
	public static boolean isStringVisible(String searchString){
		return isStringVisible("//*", searchString);
	}

	/**
	 * Generic utility method which determines whether an alert matching the alert text is visible.
	 *
	 * @param alert - text used to determine if there is an expected alert visible.
	 * @return true if an alert with the specified text is visible, false if not.
	 */
	public static boolean isAlertVisible(String alert){
		Alert alertWindow = new WebDriverWait(driver, 2).until(ExpectedConditions.alertIsPresent());

		driver.switchTo().alert();
		if(normaliseString(alertWindow.getText()).equals(alert)){
			alertWindow.accept();
			return true;
		} else {
			alertWindow.accept();
			return false;
		}
	}

	/** Internal utility method which formats all input strings correctly by removing erroneous formatting */
	private static String normaliseString(String input){
		return input.trim().replace("\n", " ").replace("  ", " ");
	}

	/**
	 * DriverUtils method which creates a custom error used within the DriverUtils class.
	 *
	 * @param driver
	 * @param searchCriteria
	 * @return Custom error including extra information on the issue.
	 */
	private static String getErrorString(RemoteWebDriver driver, String searchCriteria){
		StringBuffer error = new StringBuffer();
		error.append("\nNoSuchElement Exception: Unable to locate element with text of: ");
		error.append(searchCriteria);
		error.append("\nCurrent Page Title: \"");
		error.append(driver.getTitle());
		error.append("\"");
		error.append("\nCurrent URL: ");
		error.append(driver.getCurrentUrl());

		return error.toString();
	}

    public static void doubleClick(WebElement element) {
		Actions actions = new Actions(driver);
		actions.doubleClick(element).perform();
	}

	public static void takeScreenshot(String filePrefix){
		try {
			File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshotFile, new File("./target/reports/" + filePrefix + "." +
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("'T'yyyyMMddHHmmssS")) + "." + driver.getSessionId() + ".png"));
		} catch (IOException ex) {
			log.error("Attempt to take screenshot failed, cause was: " + ex.getCause());
		}
	}
	public static byte[] getScreenshotBytes(){ return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES); }

	/**
	 * TODO: Add Javadoc
	 */
	public static void waitUntilPageFullyLoaded() {
		//Wait required to ensure DOM cache location is reset (page has begun transitioning to the next page + DOM is null)
		try { Thread.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
		new WebDriverWait(driver, 60).until(
				webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
		if((Boolean)((JavascriptExecutor) driver).executeScript("return window.jQuery != undefined")){
			new WebDriverWait(driver, 10).until(
					webDriver -> (Boolean)((JavascriptExecutor) webDriver).executeScript("return jQuery.active === 0"));
		}
		//Wait required to ensure full loading of UI elements as it is not encompassed in the above DOM/JS waits
		try { Thread.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
	}
}
