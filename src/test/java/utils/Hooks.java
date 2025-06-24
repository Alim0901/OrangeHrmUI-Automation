package utils;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import com.aventstack.extentreports.*;
import org.openqa.selenium.WebDriver;

import java.util.Base64;

public class Hooks {

    WebDriver driver = DriverManager.getDriver();

    private static ExtentReports extent = ReportScreenshotUtils.getInstance();
    private static ThreadLocal<ExtentTest> feature = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> scenario = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return scenario.get();
    }

    private String getFeatureName(Scenario scenario) {
        String raw = scenario.getUri().toString(); // e.g., file:/.../Login.feature
        return raw.substring(raw.lastIndexOf("/") + 1).replace(".feature", "");
    }

    @Before
    public void setup(Scenario scenarioObj) {
        driver = DriverManager.getDriver();
        String featureName = getFeatureName(scenarioObj);
        ExtentTest parent = extent.createTest(featureName);
        ExtentTest child = parent.createNode(scenarioObj.getName());
        feature.set(parent);
        scenario.set(child);
    }

    @AfterStep
    public void afterEachStep(Scenario scenario) {
        boolean isFail = scenario.isFailed();
        boolean shouldTakeScreenshot = (isFail && XMLConfigLoader.getBoolean("ScreenShotOnFail"))
                || (!isFail && XMLConfigLoader.getBoolean("ScreenShotOnPass"));

        if (shouldTakeScreenshot) {
            String screenshotName = scenario.getName() + "_step_" + System.currentTimeMillis();
            byte[] screenshot = ReportScreenshotUtils.takeScreenshot(driver, screenshotName);

            // Attach to Cucumber HTML report
            scenario.attach(screenshot, "image/png", "Step Screenshot");

            // Attach to Extent Report
            String base64 = Base64.getEncoder().encodeToString(screenshot);
            getTest().log(isFail ? Status.FAIL : Status.INFO, "Step Screenshot")
                    .addScreenCaptureFromBase64String(base64, "Screenshot");

            // Optionally save file for external reports or archive
            ReportScreenshotUtils.takeScreenshotAsFile(driver, screenshotName);
        }
    }

    @After
    public void tearDown(Scenario scenarioObj) {
        if (scenarioObj.isFailed()) {
            getTest().fail("Scenario failed: " + scenarioObj.getName());
        } else {
            getTest().pass("Scenario passed: " + scenarioObj.getName());
        }

        extent.flush();
        DriverManager.quitDriver();
    }
}
