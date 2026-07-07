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

public class adminLogin {
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
        try {
            java.util.List<WebElement> navMenu = w1.findElements(By.cssSelector("[data-test='nav-menu']"));
            if (!navMenu.isEmpty() && navMenu.get(0).isDisplayed()) {
                navMenu.get(0).click();
                Thread.sleep(500);
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-sign-out']"))).click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Could not perform UI logout: " + e.getMessage());
        } finally {
            try {
                w1.manage().deleteAllCookies();
                ((org.openqa.selenium.JavascriptExecutor) w1).executeScript("window.localStorage.clear(); window.sessionStorage.clear();");
            } catch (Exception ex) {
                System.out.println("Could not clear session: " + ex.getMessage());
            }
        }
        w1.navigate().refresh();
        System.out.println("Test Case Finished and Page Refreshed.");
    }

    @AfterClass
    public void teardown() {
        if (w1 != null) {
            w1.quit();
        }
    }

    private void loginUser(String email, String password) throws InterruptedException {

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-sign-in']"))).click();
        Thread.sleep(500);

        WebElement emailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        if (!email.isEmpty()) {
            emailElement.sendKeys(email);
        }
        Thread.sleep(500);
        
        if (!password.isEmpty()) {
            w1.findElement(By.id("password")).sendKeys(password);
        }
        Thread.sleep(500);

        w1.findElement(By.cssSelector(".btnSubmit")).click();
        Thread.sleep(1000);
    }

    @Test(priority = 1)
    public void happyScenario1() throws InterruptedException {
        loginUser("admin@practicesoftwaretesting.com", "welcome01");
    }

    @Test(priority = 2)
    public void happyScenario2() throws InterruptedException {
        // حالة صحيحة مع فراغات حول الإيميل
        loginUser(" admin@practicesoftwaretesting.com ", "welcome01");
    }

    @Test(priority = 3)
    public void happyScenario3() throws InterruptedException {
        // حالة صحيحة مع حروف كبيرة في الإيميل
        loginUser("ADMIN@practicesoftwaretesting.com", "welcome01");
    }

    @Test(priority = 4)
    public void happyScenario4() throws InterruptedException {
        // محاولة صحيحة أخرى
        loginUser("admin@practicesoftwaretesting.com", "welcome01");
    }

    @Test(priority = 5)
    public void happyScenario5() throws InterruptedException {
        // محاولة صحيحة أخرى
        loginUser("admin@practicesoftwaretesting.com", "welcome01");
    }

    @Test(priority = 6)
    public void badScenario1_WrongPassword() throws InterruptedException {
        loginUser("admin@practicesoftwaretesting.com", "WrongPass123!");
    }

    @Test(priority = 7)
    public void badScenario2_WrongEmail() throws InterruptedException {
        loginUser("notadmin@practicesoftwaretesting.com", "welcome01");
    }

    @Test(priority = 8)
    public void badScenario3_EmptyPassword() throws InterruptedException {
        loginUser("admin@practicesoftwaretesting.com", "");
    }

    @Test(priority = 9)
    public void badScenario4_EmptyEmail() throws InterruptedException {
        loginUser("", "welcome01");
    }

    @Test(priority = 10)
    public void badScenario5_EmptyBoth() throws InterruptedException {
        loginUser("", "");
    }
}
