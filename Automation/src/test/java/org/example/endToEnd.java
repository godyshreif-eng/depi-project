package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Collections;

public class endToEnd {
    WebDriver w1;
    WebDriverWait wait;
    static String dynamicEmail = "faris" + System.currentTimeMillis() + "@gmail.com";

    @BeforeClass
    public void open() {
        WebDriverManager.chromedriver().setup();
        w1 = new ChromeDriver();
        w1.manage().window().maximize();
        wait = new WebDriverWait(w1, Duration.ofSeconds(10));
        w1.get("https://practicesoftwaretesting.com/");
    }

    @AfterMethod
    public void refreshPage() {
        // w1.navigate().refresh();
        System.out.println("Test Case Finished.");
    }

    @Test(priority = 1)
    public void registerUser() throws InterruptedException {
        w1.get("https://practicesoftwaretesting.com/");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-sign-in']"))).click();
        Thread.sleep(500);

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='register-link']"))).click();
        Thread.sleep(500);

        w1.findElement(By.id("first_name")).sendKeys("faris");
        Thread.sleep(500);

        w1.findElement(By.id("last_name")).sendKeys("nabil");
        Thread.sleep(500);

        w1.findElement(By.id("dob")).sendKeys("2000-01-01");
        Thread.sleep(500);

        WebElement dropdownElement = w1.findElement(By.id("country"));
        Select countrySelect = new Select(dropdownElement);
        countrySelect.selectByVisibleText("Algeria");
        Thread.sleep(500);

        w1.findElement(By.id("postal_code")).sendKeys("12345");
        Thread.sleep(500);

        w1.findElement(By.id("house_number")).sendKeys("10");
        Thread.sleep(500);

        w1.findElement(By.id("street")).clear();
        w1.findElement(By.id("street")).sendKeys("main street");
        Thread.sleep(500);

        w1.findElement(By.id("city")).clear();
        w1.findElement(By.id("city")).sendKeys("algiers");
        Thread.sleep(500);

        w1.findElement(By.id("state")).clear();
        w1.findElement(By.id("state")).sendKeys("algeria");
        Thread.sleep(500);

        w1.findElement(By.id("phone")).sendKeys("01015555");
        Thread.sleep(500);

        w1.findElement(By.id("email")).sendKeys(dynamicEmail);
        Thread.sleep(500);

        w1.findElement(By.id("password")).sendKeys("SecurePass_2026!");
        Thread.sleep(500);

        w1.findElement(By.cssSelector(".btnSubmit.mb-3")).click();

        wait.until(ExpectedConditions.urlContains("auth/login"));
    }

    @Test(priority = 2)
    public void login() throws InterruptedException {
        w1.get("https://practicesoftwaretesting.com/");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-sign-in']"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(dynamicEmail);
        w1.findElement(By.id("password")).sendKeys("SecurePass_2026!");

        w1.findElement(By.className("btnSubmit")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='nav-menu']")));
    }

    @Test(priority = 3)
    public void addToCart() throws InterruptedException {
        w1.get("https://practicesoftwaretesting.com/");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("img[alt='Combination Pliers']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))).click();
        Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();
        Thread.sleep(2000);
    }

    @Test(priority = 4)
    public void checkout() throws InterruptedException {
        // Step 1
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='proceed-1']"))).click();
        Thread.sleep(1000);

        // Step 2
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='proceed-2']"))).click();
        Thread.sleep(1000);

        // Billing Address (Step 3)
        WebElement countryDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country")));
        new Select(countryDropdown).selectByVisibleText("Algeria");
        w1.findElement(By.id("postal_code")).clear();
        w1.findElement(By.id("postal_code")).sendKeys("12345");
        w1.findElement(By.id("house_number")).clear();
        w1.findElement(By.id("house_number")).sendKeys("10");
        w1.findElement(By.id("street")).clear();
        w1.findElement(By.id("street")).sendKeys("main street");
        w1.findElement(By.id("city")).clear();
        w1.findElement(By.id("city")).sendKeys("algiers");
        w1.findElement(By.id("state")).clear();
        w1.findElement(By.id("state")).sendKeys("algeria");
        WebElement proceed3 = wait
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='proceed-3']")));
        ((org.openqa.selenium.JavascriptExecutor) w1).executeScript("arguments[0].scrollIntoView(true);", proceed3);
        Thread.sleep(500);
        proceed3.click();
        WebElement paymentMethod = wait.until(ExpectedConditions.elementToBeClickable(By.id("payment-method")));
        new Select(paymentMethod).selectByVisibleText("Cash on Delivery");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='finish']"))).click();
        Thread.sleep(2000);
    }

    @AfterClass
    public void teardown() {
        if (w1 != null) {
            w1.quit();
        }
    }
}