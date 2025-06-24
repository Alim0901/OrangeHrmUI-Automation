package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportScreenshotUtils {

    private static ExtentReports extent;

    // For attaching screenshots to Cucumber report
    public static byte[] takeScreenshot(WebDriver driver, String scenarioName) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    // For saving screenshots as file (for ExtentReport or archiving)
    public static String takeScreenshotAsFile(WebDriver driver, String scenarioName) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        String destPath = "target/screenshots/" + scenarioName + "_" + timestamp + ".png";

        try {
            File destFile = new File(destPath);
            destFile.getParentFile().mkdirs();

            // Delete if already exists
            if (Files.exists(destFile.toPath())) {
                Files.delete(destFile.toPath());
            }

            Files.copy(src.toPath(), destFile.toPath());
            return destPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Singleton instance for ExtentReports
    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReport.html");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Framework", "JUnit + Cucumber");
        }
        return extent;
    }
}
