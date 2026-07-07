package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class SearchAndFilter {
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

    @Test(priority = 1)
    public void searchForProduct() throws InterruptedException {
        // Find search field and enter product query
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-query']")));
        searchBox.clear();
        searchBox.sendKeys("Pliers");
        Thread.sleep(500);

        // Click search submit button
        w1.findElement(By.cssSelector("[data-test='search-submit']")).click();
        Thread.sleep(1500);

        // Assert card title contains "Pliers"
        WebElement productName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".card-title")));
        assert productName.getText().contains("Pliers");
    }

    @Test(priority = 2)
    public void filterByCategory() throws InterruptedException {
        // Find Hand Tools category checkbox and select it
        WebElement categoryCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
            "//label[contains(.,'Hand Tools')]//input | //label[contains(.,'Hand Tools')]/preceding-sibling::input"
        )));
        
        if (!categoryCheckbox.isSelected()) {
            categoryCheckbox.click();
        }
        Thread.sleep(1500);

        // Assert product cards are visible on the page
        WebElement productCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".card")));
        assert productCard.isDisplayed();
    }
}