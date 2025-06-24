package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.LoginPage;
import utils.DataReader;
import utils.DriverManager;
import utils.XMLConfigLoader;

public class LoginStepdefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginStepdefinitions.class);
    private WebDriver driver;
    private LoginPage loginPage;

    public LoginStepdefinitions() {
        this.driver = DriverManager.getDriver();
        this.loginPage = new LoginPage(driver);
    }

    @Given("User navigates to the login page")
    public void user_navigates_to_the_login_page() {
        driver.get(XMLConfigLoader.get("Url"));
    }

    @When("User enters {string} credentials on login page")
    public void user_enters_credentials_on_login_page(String input) {
        String username = DataReader.get(input + ".username");
        String password = DataReader.get(input + ".password");
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @And("User clicks on login Btn on login page")
    public void user_clicks_on_login_Btn_on_login_page() {
        loginPage.clickLogin();
    }

    @Then("User should be on the login page")
    public void user_should_be_on_the_login_page() {
        try {
            Assert.assertTrue(loginPage.isOnLoginPage());
            LOGGER.info("PASS: User is successfully navigated to the login page.");
        } catch (AssertionError ae) {
            LOGGER.error("ASSERTION FAILED: User is not on the login page.", ae);
            throw ae; // re-throw to let the test fail
        } catch (Exception e) {
            LOGGER.error("ERROR: Exception occurred while verifying login page.", e);
            throw new RuntimeException(e); // wrap and throw
        }
    }

    @Then("User should see 'unable to login' error message")
    public void user_should_see_an_error_message() {
        try {
            Assert.assertEquals("Invalid credentials", loginPage.getErrorMessage());
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
