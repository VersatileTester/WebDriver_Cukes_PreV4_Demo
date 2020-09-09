package com.versatiletester.page;

import com.versatiletester.util.DriverUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@SuppressWarnings("unused")
public class GoogleResultsPage extends GooglePage {

    @FindAll({ @FindBy(xpath = "//h3/..") })
    private List<WebElement> searchResultList;

    /**
     * Index starts at 1 for the first search result link.
     * @param linkPosition
     */
    public void clickSearchResult(int linkPosition){
        DriverUtils.clickPageTransitionElement(searchResultList.get(linkPosition - 1));
    }

    public WebElement getFirstResultContainingText(String text){
        for(WebElement element : searchResultList){
            if(element.getAttribute("innerText").contains(text)){
                return element;
            }
        }
        return null;
    }

    @Override
    public boolean isVisible() {
        if( driver.getCurrentUrl().contains(pageUrl) &&
            !searchResultList.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}
