package stepdefinitions;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.When;
import pages.CommonPage;
import utils.PlaywrightFactory;

public class CommonStepdefinitions {

    Page page;
    CommonPage commonPage;

    public CommonStepdefinitions() {
        this.page = PlaywrightFactory.getPage(); // updated method to return Playwright Page
        commonPage = new CommonPage(page);
    }

    @When("User clicks on logout Btn on the page")
    public void userClicksOnLogoutBtnOnThePage() {
        commonPage.logout();
    }
}
