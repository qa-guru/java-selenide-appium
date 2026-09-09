package drivers;

import com.codeborne.selenide.WebDriverProvider;
import config.AuthConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BrowserstackDriver implements WebDriverProvider {

    private static final AuthConfig authConfig = ConfigFactory.create(AuthConfig.class, System.getProperties());

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
        String user = authConfig.user();
        String key = authConfig.key();
        if (user == null || user.isBlank() || key == null || key.isBlank()) {
            throw new IllegalStateException(
                    "STOP: set browserstack.user / browserstack.key in src/test/resources/browserstack.properties "
                            + "(gitignored), or pass -Dbrowserstack.user= / -Dbrowserstack.key=");
        }

        String platform = System.getProperty("platform", "");
        return switch (platform) {
            case "android" -> androidDriver(user, key);
            case "ios" -> iosDriver(user, key);
            default -> throw new IllegalStateException("STOP: pass -Dplatform=android or -Dplatform=ios");
        };
    }

    private static WebDriver androidDriver(String user, String key) {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAutomationName("UIAutomator2");
        options.setPlatformName("Android");
        options.setDeviceName("Samsung Galaxy S22 Ultra");
        options.setPlatformVersion("12.0");
        options.setApp("bs://sample.app");
        options.setCapability("bstack:options", bstackOptions(user, key, "Android Wikipedia search"));
        return new AndroidDriver(hubUrl(), options);
    }

    private static WebDriver iosDriver(String user, String key) {
        XCUITestOptions options = new XCUITestOptions();
        options.setAutomationName("XCUITest");
        options.setPlatformName("iOS");
        options.setDeviceName("iPhone 14");
        options.setPlatformVersion("16");
        options.setApp("bs://sample.app");
        options.setCapability("bstack:options", bstackOptions(user, key, "iOS sample Text Button"));
        return new IOSDriver(hubUrl(), options);
    }

    private static Map<String, Object> bstackOptions(String user, String key, String sessionName) {
        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName", user);
        bstackOptions.put("accessKey", key);
        bstackOptions.put("appiumVersion", "2.6.0");
        bstackOptions.put("projectName", "QA.GURU Java Selenide Appium");
        bstackOptions.put("buildName", "step-2");
        bstackOptions.put("sessionName", sessionName);
        return bstackOptions;
    }

    private static java.net.URL hubUrl() {
        try {
            return URI.create("https://hub.browserstack.com/wd/hub").toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            throw new IllegalStateException("Invalid BrowserStack hub URL", e);
        }
    }
}
