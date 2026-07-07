package org.example;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public WebDriver driver;
    public ExtentReports extent;
    public ExtentTest test;

    @BeforeSuite
    public void setup() {
        // مكان حفظ التقرير
        ExtentSparkReporter spark = new ExtentSparkReporter("BugReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeMethod
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void myTest() {
        test = extent.createTest("فحص تسجيل الدخول");
        driver.get("https://www.google.com");

        // جرب تفشل التست ده عشان تشوف التقرير
        String title = driver.getTitle();
        if(title.equals("Google")) {
            test.pass("العنوان صح");
        } else {
            // دي هتخلي التست يفشل ويروح للـ AfterMethod
            assert false : "العنوان غلط!";
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = getScreenshot(result.getName());
            test.fail("الخطأ هو: " + result.getThrowable());
            test.addScreenCaptureFromPath(screenshotPath); // ربط الصورة بالتقرير
        }
        driver.quit();
    }

    @AfterSuite
    public void finish() {
        extent.flush(); // دي اللي بتبني ملف الـ HTML في الآخر
    }

    // ميثود احترافية لتصوير الشاشة
    public String getScreenshot(String screenshotName) throws IOException {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        String destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName + dateName + ".png";
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
        return destination;
    }
}