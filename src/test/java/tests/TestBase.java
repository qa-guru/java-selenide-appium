package tests;

import com.codeborne.selenide.Configuration;
import drivers.BrowserstackDriver;
import drivers.LocalAndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public abstract class TestBase {

    @BeforeAll
    static void configureSelenide() {
        Configuration.browser = driverClass().getName();
        Configuration.browserSize = null;
        Configuration.timeout = 30_000;
    }

    private static Class<?> driverClass() {
        String host = System.getProperty("deviceHost", "browserstack");
        return switch (host) {
            case "browserstack" -> BrowserstackDriver.class;
            case "real" -> LocalAndroidDriver.class;
            default -> throw new IllegalStateException(
                    "STOP: pass -DdeviceHost=browserstack or -DdeviceHost=real (default browserstack)");
        };
    }

    @BeforeEach
    void setUp() {
        System.setProperty("platform", platform());
        open();
    }

    protected abstract String platform();

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }
}
