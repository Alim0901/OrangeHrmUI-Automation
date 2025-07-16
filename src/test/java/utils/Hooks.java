package utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.Status;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ScreenshotType;
import io.cucumber.java.*;

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
        Page page = PlaywrightFactory.getPage();          // fetch after it exists
        if (page == null) return;                         // safety guard

        boolean failed   = sc.isFailed();
        boolean takeShot = (failed  && XMLConfigLoader.getBoolean("ScreenShotOnFail"))
                || (!failed && XMLConfigLoader.getBoolean("ScreenShotOnPass"));

        if (takeShot) {
            String shotName = sc.getName() + "_step_" + System.currentTimeMillis();
            byte[] png = page.screenshot(new Page.ScreenshotOptions()
                    .setFullPage(true)
                    .setType(ScreenshotType.PNG));

            sc.attach(png, "image/png", "Step Screenshot");        // Cucumber
            String b64 = Base64.getEncoder().encodeToString(png);  // Extent
            getTest().log(failed ? Status.FAIL : Status.INFO, "Step Screenshot")
                    .addScreenCaptureFromBase64String(b64, "Screenshot");

            page.screenshot(new Page.ScreenshotOptions()           // archive file
                    .setPath(Paths.get("screenshots/" + shotName + ".png"))
                    .setFullPage(true)
                    .setType(ScreenshotType.PNG));
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
