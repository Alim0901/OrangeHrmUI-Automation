package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ScreenshotType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportScreenshotUtils {

    private static ExtentReports extent;

    /* ───────────── Screenshot helpers ───────────── */

    /** Attach screenshot bytes to Cucumber report */
    public static byte[] takeScreenshot(Page page, String scenarioName) {
        return page.screenshot(new Page.ScreenshotOptions()
                .setFullPage(true)
                .setType(ScreenshotType.PNG));
    }

    /** Save screenshot as file (for Extent or archival) and return the path */
    public static String takeScreenshotAsFile(Page page, String scenarioName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        String destPath  = "target/screenshots/" + scenarioName + "_" + timestamp + ".png";

        try {
            Path path = Paths.get(destPath);
            Files.createDirectories(path.getParent());

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(path)
                    .setFullPage(true)
                    .setType(ScreenshotType.PNG));

            return destPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* ───────────── ExtentReports singleton ───────────── */

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReport.html");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Framework", "Playwright + Cucumber");
        }
        return extent;
    }
}
