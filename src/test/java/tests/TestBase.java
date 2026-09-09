package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import drivers.BrowserstackDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.MutableCapabilities;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    @BeforeAll
    static void configureSelenide() {
        Configuration.browserSize = null;
        Configuration.timeout = 30_000;
    }

    @BeforeEach
    void setUp() {
        WebDriverRunner.setWebDriver(new BrowserstackDriver().createDriver(new MutableCapabilities()));
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }
}
