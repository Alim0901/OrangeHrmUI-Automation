package stepdefinitions;

import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.DashboardPage;
import utils.DriverManager;

public class DashboardStepdefinition {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardStepdefinition.class);
    private DashboardPage dashboardPage;
    WebDriver driver;

    public DashboardStepdefinition() {
        this.driver = DriverManager.getDriver();
        this.dashboardPage = new DashboardPage(driver);
    }

    @Then("User should be navigated to the dashboard page")
    public void User_should_be_navigated_to_the_dashboard_page() {
        try {
            boolean isOnDashboard = dashboardPage.isUserOnDashboardPage();
            Assert.assertTrue(isOnDashboard);
            LOGGER.info("PASS: User is successfully navigated to the dashboard page.");
        } catch (AssertionError ae) {
            LOGGER.error("ASSERTION FAILED: User is not on the dashboard page.", ae);
            throw ae; // re-throw to let the test fail
        } catch (Exception e) {
            LOGGER.error("ERROR: Exception occurred while verifying dashboard page.", e);
            throw new RuntimeException(e);
        }
    }
}
