package stepdefinitions;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.LoginPage;
import utils.DataReader;
import utils.PlaywrightFactory;
import utils.XMLConfigLoader;

public class LoginStepdefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginStepdefinitions.class);

    private final Page page;
    private final LoginPage loginPage;

    public LoginStepdefinitions() {
        this.page      = PlaywrightFactory.getPage(); // Playwright Page, not WebDriver
        this.loginPage = new LoginPage(page);
    }

    /* ------------------- Steps ------------------- */

    @Given("User navigates to the login page")
    public void user_navigates_to_the_login_page() {
        page.navigate(XMLConfigLoader.get("Url"));   // Playwright navigation
    }

    @When("User enters {string} credentials on login page")
    public void user_enters_credentials_on_login_page(String input) {
        loginPage.enterUsername(DataReader.get(input + ".username"));
        loginPage.enterPassword(DataReader.get(input + ".password"));
    }

    @And("User clicks on login Btn on login page")
    public void user_clicks_on_login_Btn_on_login_page() {
        loginPage.clickLogin();
    }

    @Then("User should be on the login page")
    public void user_should_be_on_the_login_page() {
        try {
            Assertions.assertTrue( loginPage.isOnLoginPage(),"User is not on the login page");
            LOGGER.info("PASS: User is successfully navigated to the login page.");
        } catch (AssertionError ae) {
            LOGGER.error("ASSERTION FAILED: User is not on the login page.", ae);
            throw ae;
        } catch (Exception e) {
            LOGGER.error("ERROR: Exception occurred while verifying login page.", e);
            throw new RuntimeException(e);
        }
    }

    @Then("User should see 'unable to login' error message")
    public void user_should_see_an_error_message() {
        try {
            Assertions.assertEquals("Invalid credentials", loginPage.getErrorMessage());
            LOGGER.info("PASS: Error message is displayed for invalid credentials on Login page.");
        } catch (AssertionError ae) {
            LOGGER.error("ASSERTION FAILED: Error message is NOT displayed for invalid credentials on login page.", ae);
            throw ae;
        } catch (Exception e) {
            LOGGER.error("ERROR: Exception occurred while logging in with invalid credentials on login page.", e);
            throw new RuntimeException(e);
        }
    }
}
