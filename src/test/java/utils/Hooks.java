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
import java.util.Base64;

public class Hooks {

    /* ───── ExtentReports plumbing ───── */
    private static final ExtentReports extent = ReportScreenshotUtils.getInstance();
    private static final ThreadLocal<ExtentTest> feature  = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> scenario = new ThreadLocal<>();

    public static ExtentTest getTest() { return scenario.get(); }

    /* ───── Helpers ───── */
    private static String featureNameOf(Scenario sc) {
        String raw = sc.getUri().toString();          // e.g. file:/…/Login.feature
        return raw.substring(raw.lastIndexOf('/') + 1).replace(".feature", "");
    }

    /* ───── Cucumber hooks ───── */

    @Before
    public void setUp(Scenario sc) {

        System.out.println("⏱ Running scenario: " + sc.getName() +
                " | Thread: " + Thread.currentThread().getName() +
                " | Time: " + java.time.LocalTime.now());
        // create browser/context/page for this thread
        PlaywrightFactory.initBrowser();   // or true/false as you wish
        // Extent hierarchy
        ExtentTest parent  = extent.createTest(featureNameOf(sc));
        ExtentTest child   = parent.createNode(sc.getName());
        feature.set(parent);
        scenario.set(child);
    }

    @AfterStep
    public void afterEachStep(Scenario sc) {
        Page page = PlaywrightFactory.getPage();
        if (page == null) return;

        boolean failed = sc.isFailed();
        boolean takeShot = (failed && XMLConfigLoader.getBoolean("ScreenShotOnFail"))
                || (!failed && XMLConfigLoader.getBoolean("ScreenShotOnPass"));

        if (takeShot) {
            String shotName = sc.getName() + "_step_" + System.currentTimeMillis();
            byte[] png = page.screenshot(new Page.ScreenshotOptions()
                    .setFullPage(true)
                    .setType(ScreenshotType.PNG));

            // Attach ONLY to Allure (for allure reports)
            Allure.addAttachment("Step Screenshot", "image/png", new ByteArrayInputStream(png), ".png");

            // Save screenshot file for ExtentReports
            Path screenshotsDir = Paths.get("target/screenshots");
            try {
                Files.createDirectories(screenshotsDir); // ensure directory exists
                Path screenshotPath = screenshotsDir.resolve(shotName + ".png");

                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(screenshotPath)
                        .setFullPage(true)
                        .setType(ScreenshotType.PNG));

                // Attach screenshot to ExtentReports from saved file
                getTest().log(failed ? Status.FAIL : Status.INFO, "Step Screenshot")
                        .addScreenCaptureFromPath(screenshotPath.toString(), "Screenshot");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @After
    public void tearDown(Scenario sc) {
        if (sc.isFailed()) getTest().fail("Scenario failed: " + sc.getName());
        else               getTest().pass("Scenario passed: " + sc.getName());

        extent.flush();
        PlaywrightFactory.closeBrowser();   // closes context, browser, playwright
    }
}
