package com.versatiletester.cukes.stepdefs;

import com.versatiletester.cukes.TestBase;
import com.versatiletester.page.GoogleResultsPage;
import com.versatiletester.page.GoogleSearchPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.*;

public class GoogleStepDefs extends TestBase {
    private static final String DEFAULT_SEARCH_TERM = "default";

    private GoogleSearchPage googleSearchPage;
    private GoogleResultsPage googleResultsPage;

    public GoogleStepDefs(GoogleSearchPage googleSearchPage){
        this.googleSearchPage = googleSearchPage;
    }

    @Given("^I navigate to the Google home page$")
    public void i_navigate_to_the_Google_home_page(){
        driver.get(TestBase.getProperty(GOOGLE_URL_PROPERTY_NAME));
    }

    @When("^I google search the term (.*)$")
    public void i_google_search_the_term_X(String searchTerm) {
        googleResultsPage = googleSearchPage.googleSearchForTerm(searchTerm);
    }

    @Then("^I should see at least one result containing the term (.*)$")
    public void i_should_see_at_least_one_result_containing_the_term_X(String searchTerm) {
        assertNotNull("Could not find any results matching the search term " + searchTerm,
                googleResultsPage.getFirstResultContainingText(searchTerm));
    }

    @When("^When I lucky search any valid word$")
    public void when_I_lucky_search_any_valid_word() {
        googleResultsPage = googleSearchPage.luckySearchForTerm(DEFAULT_SEARCH_TERM);
    }

    @Then("^I should not be taken to the google results page$")
    public void i_should_not_be_taken_to_the_google_results_page() {
        assertFalse(googleResultsPage.isVisible());
    }
}