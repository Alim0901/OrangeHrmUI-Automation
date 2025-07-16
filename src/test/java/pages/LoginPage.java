package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class LoginPage {

    private final Page page;
    private final Locator username;
    private final Locator password;
    private final Locator loginButton;
    private final Locator errorMessage;

    public LoginPage(Page page) {
        this.page = page;
        this.username     = page.locator("input[name='username']");
        this.password     = page.locator("input[name='password']");
        this.loginButton  = page.locator("button[type='submit']");
        this.errorMessage = page.locator("//div[@role='alert']//p");
    }

    public void enterUsername(String user) {
        username.fill(user);          // auto‑waits for visibility & enabled state
    }

    public void enterPassword(String pass) {
        password.fill(pass);
    }

    public void clickLogin() {
        loginButton.click();          // waits for navigation if one occurs
    }

    public String getErrorMessage() {
        return errorMessage.textContent();
    }

    public boolean isOnLoginPage() {
        username.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)   // wait until visible
                .setTimeout(10_000));
        return username.isVisible() && password.isVisible();
    }
}
