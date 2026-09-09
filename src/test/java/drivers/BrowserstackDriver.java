package drivers;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BrowserstackDriver implements WebDriverProvider {

    private static final Properties RESOURCES = loadResources();

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
        String user = credential("browserstack.user", "BROWSERSTACK_USERNAME");
        String key = credential("browserstack.key", "BROWSERSTACK_ACCESS_KEY");

        UiAutomator2Options options = new UiAutomator2Options();
        options.setAutomationName("UIAutomator2");
        options.setPlatformName("Android");
        options.setDeviceName("Samsung Galaxy S22 Ultra");
        options.setPlatformVersion("12.0");
        options.setApp("bs://sample.app");

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

    private static String credential(String propertyKey, String envName) {
        String fromSystem = System.getProperty(propertyKey);
        if (notBlank(fromSystem)) {
            return fromSystem.trim();
        }
        String fromFile = RESOURCES.getProperty(propertyKey);
        if (notBlank(fromFile)) {
            return fromFile.trim();
        }
        String fromEnv = System.getenv(envName);
        if (notBlank(fromEnv)) {
            return fromEnv.trim();
        }
        throw new IllegalStateException(
                "STOP: set " + propertyKey + " in src/test/resources/browserstack.properties "
                        + "(gitignored; copy from browserstack.properties.example), "
                        + "or pass -D" + propertyKey + " / env " + envName);
    }

    private static boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    private static Properties loadResources() {
        Properties properties = new Properties();
        try (InputStream in = BrowserstackDriver.class.getClassLoader()
                .getResourceAsStream("browserstack.properties")) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read browserstack.properties", e);
        }
        return properties;
    }

    private static java.net.URL hubUrl() {
        try {
            return URI.create("https://hub.browserstack.com/wd/hub").toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            throw new IllegalStateException("Invalid BrowserStack hub URL", e);
        }
    }
}
