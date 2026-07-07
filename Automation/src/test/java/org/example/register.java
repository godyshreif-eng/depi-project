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
import java.util.Collections;

public class register {
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
    }

    @Test(priority = 1)
    public void happyScenario1() throws InterruptedException {
        registerUser("Faris", "Nabil", "2000-01-01", "Algeria", "12345", "10", "Main St", "Algiers", "State1",
                "01015555", "faris" + System.currentTimeMillis() + "@test.com", "SecurePass_2026!");
    }

    @Test(priority = 2)
    public void happyScenario2() throws InterruptedException {
        registerUser("Ahmed", "Ali", "1995-05-15", "Egypt", "54321", "22", "Cairo Rd", "Cairo", "Cairo", "0123456789",
                "ahmed" + System.currentTimeMillis() + "@test.com", "Ahmed_Secure_123!");
    }

    @Test(priority = 3)
    public void happyScenario3() throws InterruptedException {
        registerUser("John", "Doe", "1988-10-20", "France", "75001", "1", "Rue de Rivoli", "Paris",
                "Paris", "33123456789", "john" + System.currentTimeMillis() + "@test.com", "John_Paris_99!");
    }

    @Test(priority = 4)
    public void happyScenario4() throws InterruptedException {
        registerUser("Sarah", "Muller", "1992-02-14", "Germany", "10115", "45", "Berlin Str", "Berlin", "Berlin",
                "49301234567", "sarah" + System.currentTimeMillis() + "@test.com", "Sarah_Berlin_2026!");
    }

    @Test(priority = 5)
    public void happyScenario5() throws InterruptedException {
        registerUser("Maria", "Garcia", "1990-12-25", "Spain", "28001", "100", "Madrid Ave", "Madrid", "Madrid",
                "34912345678", "maria" + System.currentTimeMillis() + "@test.com", "Maria_Madrid_SP!");
    }

    @Test(priority = 6)
    public void negativeScenario_EmptyFirstName() throws InterruptedException {
        registerUser("", "LastNameOnly", "1990-01-01", "Algeria", "12345", "1", "Empty St", "City", "State", "01111111",
                "fail" + System.currentTimeMillis() + "@test.com", "FailPass_123!");

        WebElement errorMsg = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='first-name-error']")));
        assert errorMsg.getText().contains("First name is required");
    }
}