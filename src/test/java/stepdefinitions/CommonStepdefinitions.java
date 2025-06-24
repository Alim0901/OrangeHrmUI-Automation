package stepdefinitions;

import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import pages.CommonPage;
import utils.DriverManager;

public class CommonStepdefinitions {

    WebDriver driver;
    CommonPage commonPage;

    public CommonStepdefinitions() {
        this.driver = DriverManager.getDriver();
        commonPage = new CommonPage(driver);
    }

    @When("User clicks on logout Btn on the page")
    public void userClicksOnLogoutBtnOnThePage() {
        commonPage.logout();
    }
}