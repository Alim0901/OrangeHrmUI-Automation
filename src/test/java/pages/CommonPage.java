package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.WaitUtils;

public class CommonPage {

    WebDriver driver;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonPage.class);

    public CommonPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//li[contains(@class, 'userdropdown')]")
    private WebElement userDropdownBtn;

    @FindBy(xpath = "//li/a[text()='Logout']")
    private WebElement logOutBtn;

    public void logout() {
        try {
            WaitUtils.waitForVisibility(driver, userDropdownBtn, 10);
            userDropdownBtn.click();
            LOGGER.info("PASS: User clicked user dropdown button on the page.");

            WaitUtils.waitForVisibility(driver, logOutBtn, 10);
            logOutBtn.click();
            LOGGER.info("PASS: User clicked logout button on the page.");
        } catch (Exception e) {
            LOGGER.error("FAIL: Exception occurred while attempting to logout.", e);
            throw new RuntimeException("Logout failed", e);
        }
    }

}

