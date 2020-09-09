package com.versatiletester.page;

import com.versatiletester.util.DriverFactory;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * TODO: Explain why create an ABSTRACT page base, with an ABSTRACT isVisible() method
 */
public abstract class PageBase{
    protected RemoteWebDriver driver;

    public PageBase() {
        this.driver = DriverFactory.getDriver();
        PageFactory.initElements(driver, this);
    }

    @SuppressWarnings("unused")
    public abstract boolean isVisible();
}
