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

public class Login {
    WebDriver w1;
    WebDriverWait wait;
    String userEmail = "testuser" + System.currentTimeMillis() + "@test.com";
    String userPass = "SecurePass_2026!";

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

    private void registerUser(String firstName, String lastName, String dob, String country, String postCode,
            String houseNum, String street, String city, String state, String phone, String email, String password)
            throws InterruptedException {

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-sign-in']"))).click();
        Thread.sleep(500);

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='register-link']"))).click();
        Thread.sleep(500);

        if (!firstName.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first_name"))).sendKeys(firstName);
        }
        Thread.sleep(500);

        w1.findElement(By.id("last_name")).sendKeys(lastName);
        Thread.sleep(500);

        w1.findElement(By.id("dob")).sendKeys(dob);
        Thread.sleep(500);

        WebElement dropdownElement = w1.findElement(By.id("country"));
        Select countrySelect = new Select(dropdownElement);
        if (!country.isEmpty()) {
            countrySelect.selectByVisibleText(country);
        }
        Thread.sleep(500);

        w1.findElement(By.id("postal_code")).sendKeys(postCode);
        Thread.sleep(500);

        w1.findElement(By.id("house_number")).sendKeys(houseNum);
        Thread.sleep(500);

        w1.findElement(By.id("street")).sendKeys(street);
        Thread.sleep(500);

        w1.findElement(By.id("city")).sendKeys(city);
        Thread.sleep(500);

        w1.findElement(By.id("state")).sendKeys(state);
        Thread.sleep(500);

        w1.findElement(By.id("phone")).sendKeys(phone);
        Thread.sleep(500);

        w1.findElement(By.id("email")).sendKeys(email);
        Thread.sleep(500);

        w1.findElement(By.id("password")).sendKeys(password);
        Thread.sleep(500);

        w1.findElement(By.cssSelector(".btnSubmit.mb-3")).click();
        Thread.sleep(1000);
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
    public void registerOnce() throws InterruptedException {
        registerUser("Faris", "Nabil", "2000-01-01", "Algeria", "12345", "10", "Main St", "Algiers", "State1",
                "01015555", userEmail, userPass);
    }

    @Test(priority = 2)
    public void happyScenario1() throws InterruptedException {
        loginUser(userEmail, userPass);
    }

    @Test(priority = 3)
    public void badScenario1_WrongPassword() throws InterruptedException {
        loginUser(userEmail, "WrongPass123!");
    }

    @Test(priority = 4)
    public void badScenario2_WrongEmail() throws InterruptedException {
        loginUser("wrong_" + userEmail, userPass);
    }

    @Test(priority = 5)
    public void badScenario3_EmptyPassword() throws InterruptedException {
        loginUser(userEmail, "");
    }

    @Test(priority = 6)
    public void badScenario4_EmptyEmail() throws InterruptedException {
        loginUser("", userPass);
    }

    @Test(priority = 7)
    public void badScenario5_EmptyBoth() throws InterruptedException {
        loginUser("", "");
    }
}