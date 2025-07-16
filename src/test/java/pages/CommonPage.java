package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonPage {

    private final Page page;
    private final Locator userDropdownBtn;
    private final Locator logOutBtn;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonPage.class);

    public CommonPage(Page page) {
        this.page = page;
        this.userDropdownBtn = page.locator("//li[contains(@class, 'userdropdown')]");
        this.logOutBtn = page.locator("//li/a[text()='Logout']");
    }

    public void logout() {
        try {
            userDropdownBtn.waitFor(); // auto-wait for visible + enabled
            userDropdownBtn.click();
            LOGGER.info("PASS: User clicked user dropdown button on the page.");

            logOutBtn.waitFor();
            logOutBtn.click();
            LOGGER.info("PASS: User clicked logout button on the page.");
        } catch (Exception e) {
            LOGGER.error("FAIL: Exception occurred while attempting to logout.", e);
            throw new RuntimeException("Logout failed", e);
        }
    }
}
