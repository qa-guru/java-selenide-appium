package tests;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;

public class IosSampleTest extends TestBase {

    @Override
    protected String platform() {
        return "ios";
    }

    @Test
    void textInputOutput() {
        $(AppiumBy.accessibilityId("Text Button")).should(exist).click();
        $(AppiumBy.accessibilityId("Text Input")).should(exist).sendKeys("hello@browserstack.com\n");
        $(AppiumBy.accessibilityId("Text Output")).should(exist);
    }
}
