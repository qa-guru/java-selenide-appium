package tests;

import com.codeborne.selenide.Configuration;
import drivers.BrowserstackDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class TestBase {

    @BeforeAll
    static void configureSelenide() {
        Configuration.browser = BrowserstackDriver.class.getName();
        Configuration.browserSize = null;
        Configuration.timeout = 30_000;
    }

    @BeforeEach
    void setUp() {
        System.setProperty("platform", getClass().getSimpleName().startsWith("Ios") ? "ios" : "android");
        open();
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }
}
