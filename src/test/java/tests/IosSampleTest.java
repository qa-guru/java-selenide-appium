package tests;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;

@EnabledIfSystemProperty(named = "platform", matches = "ios")
public class IosSampleTest extends TestBase {

    @Test
    void textInputOutput() {
        $(AppiumBy.accessibilityId("Text Button")).should(exist).click();
        $(AppiumBy.accessibilityId("Text Input")).should(exist).sendKeys("hello@browserstack.com\n");
        $(AppiumBy.accessibilityId("Text Output")).should(exist);
    }
}
