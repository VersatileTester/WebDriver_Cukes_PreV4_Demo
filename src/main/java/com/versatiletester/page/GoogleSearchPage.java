package com.versatiletester.page;

import com.versatiletester.util.DriverUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GoogleSearchPage extends GooglePage {

    @FindBy(xpath = "//input[@title='Search']")
    private WebElement searchBox;
    @FindBy(xpath = "//span[@aria-label='Clear']")
    private WebElement clearIcon;

    @FindBy(xpath = "//input[@value='Google Search']")
    private WebElement googleSearchButton;
    @FindBy(xpath = "//input[contains(@value,'m Feeling Lucky')]")
    private WebElement feelingLuckyButton;

    /* The only IFrame on the page is the Cookie IFrame, which has no other useful identifiable attributes */
    @FindBy(xpath = "//iframe")
    private WebElement cookieIframe;
    @FindBy(xpath = "//span[normalize-space(text()) ='I agree']")
    private WebElement cookieConfirmationButton;

    public GoogleResultsPage googleSearchForTerm(String searchTerm){
        bypassPrompts();
        searchBox.sendKeys(searchTerm);
        DriverUtils.clickPageTransitionElement(googleSearchButton);
        return new GoogleResultsPage();
    }

    /**
     * Technically the resulting page from a 'Lucky Search' is not the GoogleResults page, however as the actual page
     * hit is dynamic and depends upon the search criteria at any given time, the GoogleResultsPage is returned in order
     * to assert against it's visibility.
     *
     * @param searchTerm
     * @return
     */
    public GoogleResultsPage luckySearchForTerm(String searchTerm){
        bypassPrompts();
        searchBox.sendKeys(searchTerm);
        DriverUtils.clickPageTransitionElement(feelingLuckyButton);
        return new GoogleResultsPage();
    }

    private void bypassPrompts(){
        if(DriverUtils.elementExists(cookieIframe)){
            driver.switchTo().frame(cookieIframe);
            cookieConfirmationButton.click();
            driver.switchTo().defaultContent();
        }
    }

    @Override
    public boolean isVisible() {
        if( googleSearchButton.isDisplayed() && googleSearchButton.isEnabled()){
            return true;
        } else {
            return false;
        }
    }
}
