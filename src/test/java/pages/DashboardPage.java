package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.WaitUtils;


public class DashboardPage {

    WebDriver driver;
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardPage.class);

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//h6[text()='Dashboard']")
    private WebElement dashboardHeader;

    @FindBy(xpath = "//a[contains(@class, 'active')]/span")
    private WebElement selectedSideNavTab;

    @FindBy(xpath = "//a[text()='Logout']")
    private WebElement logoutLink;

    public boolean isUserOnDashboardPage() {
        WaitUtils.waitForVisibility(driver, dashboardHeader, 10);
        return dashboardHeader.isDisplayed();
    }
}