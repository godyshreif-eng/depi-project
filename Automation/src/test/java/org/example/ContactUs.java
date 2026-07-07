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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class ContactUs {
    WebDriver w1;
    WebDriverWait wait;

    @BeforeClass
    public void open() {
        WebDriverManager.chromedriver().setup();
        w1 = new ChromeDriver();
        w1.manage().window().maximize();
        wait = new WebDriverWait(w1, Duration.ofSeconds(10));
    }

    @BeforeMethod
    public void goToHome() {
        w1.get("https://practicesoftwaretesting.com/");
    }

    @AfterMethod
    public void refreshPage() {
        w1.navigate().refresh();
        System.out.println("Test Case Finished and Page Refreshed.");
    }

    @AfterClass
    public void teardown() {
        if (w1 != null) {
            w1.quit();
        }
    }

    private void fillContactForm(String firstName, String lastName, String email, String subjectVal, String message) throws InterruptedException {
        // Navigate to contact page
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-contact']"))).click();
        Thread.sleep(1000);

        // Fill form fields
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first_name"))).sendKeys(firstName);
        Thread.sleep(500);
        w1.findElement(By.id("last_name")).sendKeys(lastName);
        Thread.sleep(500);
        w1.findElement(By.id("email")).sendKeys(email);
        Thread.sleep(500);

        // Select subject dropdown
        WebElement dropdownElement = w1.findElement(By.id("subject"));
        Select subjectSelect = new Select(dropdownElement);
        subjectSelect.selectByValue(subjectVal);
        Thread.sleep(500);

        w1.findElement(By.id("message")).sendKeys(message);
        Thread.sleep(500);

        // Click submit
        w1.findElement(By.cssSelector("[data-test='contact-submit']")).click();
        Thread.sleep(1000);
    }

    @Test(priority = 1)
    public void testContactUsSuccessfully() throws InterruptedException {
        fillContactForm("Faris", "Nabil", "test@test.com", "customer-service", "This is a test message regarding customer service support.");
        
        // Assert success message is displayed
        WebElement successAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
        assert successAlert.getText().contains("Thanks for your message!");
    }
}
