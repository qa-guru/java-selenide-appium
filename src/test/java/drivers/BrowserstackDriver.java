package drivers;

import com.codeborne.selenide.WebDriverProvider;
import config.AuthConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
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
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAutomationName("UIAutomator2");
        options.setPlatformName("Android");
        options.setDeviceName("Samsung Galaxy S22 Ultra");
        options.setPlatformVersion("12.0");
        options.setApp("bs://sample.app");

        String user = authConfig.user();
        String key = authConfig.key();
        if (user == null || user.isBlank() || key == null || key.isBlank()) {
            throw new IllegalStateException(
                    "STOP: set browserstack.user / browserstack.key in src/test/resources/browserstack.properties "
                            + "(gitignored), or pass -Dbrowserstack.user= / -Dbrowserstack.key=");
        }

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName", user);
        bstackOptions.put("accessKey", key);
        bstackOptions.put("appiumVersion", "2.6.0");
        bstackOptions.put("projectName", "QA.GURU Java Selenide Appium");
        bstackOptions.put("buildName", "step-1");
        bstackOptions.put("sessionName", "Android Wikipedia search");
        options.setCapability("bstack:options", bstackOptions);

        return new AndroidDriver(hubUrl(), options);
    }

    private static java.net.URL hubUrl() {
        try {
            return URI.create("https://hub.browserstack.com/wd/hub").toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            throw new IllegalStateException("Invalid BrowserStack hub URL", e);
        }
    }
}
