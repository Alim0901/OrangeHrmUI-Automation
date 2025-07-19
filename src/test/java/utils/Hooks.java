package utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.Status;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ScreenshotType;
import io.cucumber.java.*;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Hooks {

    private static final ExtentReports extent = ReportScreenshotUtils.getInstance();
    private static final ThreadLocal<ExtentTest> feature  = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> scenario = new ThreadLocal<>();

    public static ExtentTest getTest() { return scenario.get(); }

    private static String featureNameOf(Scenario sc) {
        String raw = sc.getUri().toString();
        return raw.substring(raw.lastIndexOf('/') + 1).replace(".feature", "");
    }

    @Before
    public void setUp(Scenario sc) {
        System.out.println("⏱ Running scenario: " + sc.getName() +
                " | Thread: " + Thread.currentThread().getName() +
                " | Time: " + java.time.LocalTime.now());

        PlaywrightFactory.initBrowser();

        if (extent != null) {
            ExtentTest parent  = extent.createTest(featureNameOf(sc));
            ExtentTest child   = parent.createNode(sc.getName());
            feature.set(parent);
            scenario.set(child);
        }
    }

    @AfterStep
    public void afterEachStep(Scenario sc) {
        Page page = PlaywrightFactory.getPage();
        if (page == null) return;

        boolean failed = sc.isFailed();
        boolean takeScreenshot = (failed && XMLConfigLoader.getBoolean("ScreenShotOnFail")) ||
                (!failed && XMLConfigLoader.getBoolean("ScreenShotOnPass"));

        if (!takeScreenshot) return;

        String shotName = sc.getName() + "_step_" + System.currentTimeMillis();

        // Take screenshot bytes
        byte[] png = page.screenshot(new Page.ScreenshotOptions()
                .setFullPage(true)
                .setType(ScreenshotType.PNG));

        // Attach to Allure (does not log Hooks class in steps)
        Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(png), ".png");

        // If ExtentReports enabled, save file and attach
        if (extent != null) {
            try {
                Path screenshotsDir = Paths.get("target/screenshots");
                Files.createDirectories(screenshotsDir);
                Path screenshotPath = screenshotsDir.resolve(shotName + ".png");

                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(screenshotPath)
                        .setFullPage(true)
                        .setType(ScreenshotType.PNG));

                getTest().log(failed ? Status.FAIL : Status.INFO, "Screenshot attached")
                        .addScreenCaptureFromPath(screenshotPath.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @After
    public void tearDown(Scenario sc) {
        if (extent != null && getTest() != null) {
            if (sc.isFailed()) getTest().fail("Scenario failed: " + sc.getName());
            else               getTest().pass("Scenario passed: " + sc.getName());

            extent.flush();
        }

        PlaywrightFactory.closeBrowser();
    }
}
