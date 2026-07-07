package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class CartTests {
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
    public void testAddToCart() throws InterruptedException {
        // Click on the product image (Combination Pliers)
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("img[alt='Combination Pliers']"))).click();
        Thread.sleep(1000);

        // Click Add to Cart button
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))).click();
        Thread.sleep(1000);

        // Navigate to the cart page
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();
        Thread.sleep(1500);

        // Verify the product is visible in the cart
        WebElement productInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//td[contains(.,'Combination Pliers')] | //span[contains(text(),'Combination Pliers')]")
        ));
        assert productInCart.isDisplayed();
    }

    @Test(priority = 2)
    public void testUpdateQuantity() throws InterruptedException {
        // Go directly to the cart page (the item should still be in the session/cart)
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();
        Thread.sleep(1000);

        // Find the quantity input field
        WebElement quantityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[@type='number'] | //input[contains(@class,'quantity')]")
        ));

        // Update the quantity to 3
        quantityInput.clear();
        quantityInput.sendKeys("3");
        quantityInput.sendKeys(Keys.TAB); // Trigger blur event to update cart totals
        Thread.sleep(1500);

        // Re-find the quantity input element to avoid StaleElementReferenceException due to page re-rendering
        WebElement quantityInputUpdated = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[@type='number'] | //input[contains(@class,'quantity')]")
        ));
        String currentQuantity = quantityInputUpdated.getAttribute("value");
        assert "3".equals(currentQuantity);
    }

    @Test(priority = 3)
    public void testRemoveFromCart() throws InterruptedException {
        // Go directly to the cart page
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();
        Thread.sleep(1000);

        // Find and click the delete/remove button
        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
            "//a[contains(@class,'btn-danger')] | //button[contains(@class,'btn-danger')] | //*[@data-test='remove']"
        )));
        removeButton.click();
        Thread.sleep(2000);

        // Assert that the item "Combination Pliers" is no longer visible in the cart
        boolean isInvisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//td[contains(.,'Combination Pliers')] | //span[contains(text(),'Combination Pliers')]")
        ));
        assert isInvisible;
    }
}
