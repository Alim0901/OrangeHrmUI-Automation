package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardPage {

    private final Page page;
    private final Locator dashboardHeader;
    private final Locator selectedSideNavTab;
    private final Locator logoutLink;

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardPage.class);

    public DashboardPage(Page page) {
        this.page = page;
        this.dashboardHeader = page.locator("//h6[text()='Dashboard']");
        this.selectedSideNavTab = page.locator("//a[contains(@class, 'active')]/span");
        this.logoutLink = page.locator("//a[text()='Logout']");
    }

    public boolean isUserOnDashboardPage() {
        try {
            dashboardHeader.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)   // wait until visible
                    .setTimeout(10_000));                    // milliseconds
            return true;
        } catch (Exception e) {
            LOGGER.error("Dashboard header not visible within timeout.", e);
            return false;
        }
    }


}
