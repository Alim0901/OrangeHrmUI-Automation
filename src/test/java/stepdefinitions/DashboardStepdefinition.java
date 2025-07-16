package stepdefinitions;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.DashboardPage;
import utils.PlaywrightFactory;

public class DashboardStepdefinition {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardStepdefinition.class);
    private final DashboardPage dashboardPage;
    private final Page page;

    public DashboardStepdefinition() {
        this.page = PlaywrightFactory.getPage(); // Playwright Page instead of WebDriver
        this.dashboardPage = new DashboardPage(page);
    }

    @Then("User should be navigated to the dashboard page")
    public void User_should_be_navigated_to_the_dashboard_page() {
        try {
            boolean isOnDashboard = dashboardPage.isUserOnDashboardPage();
            Assertions.assertTrue(isOnDashboard,"User is not on the dashboard page");
            LOGGER.info("PASS: User is successfully navigated to the dashboard page.");
        } catch (AssertionError ae) {
            LOGGER.error("ASSERTION FAILED: User is not on the dashboard page.", ae);
            throw ae;
        } catch (Exception e) {
            LOGGER.error("ERROR: Exception occurred while verifying dashboard page.", e);
            throw new RuntimeException(e);
        }
    }
}
