package tests;

import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.back;

public class AndroidSearchTest extends TestBase {

    @Override
    protected String platform() {
        return "android";
    }

    @Test
    void searchWikipedia() {
        skipOnboarding();

        $(AppiumBy.id("org.wikipedia.alpha:id/nav_tab_search")).should(exist).click();
        $(AppiumBy.accessibilityId("Close")).should(exist).click();

        SelenideElement searchCab = $(AppiumBy.id("org.wikipedia.alpha:id/search_cab_view"));
        if (searchCab.exists()) {
            searchCab.click();
        } else {
            $(AppiumBy.xpath("//*[contains(@text,'Search Wikipedia')]")).should(exist).click();
        }

        $(AppiumBy.id("org.wikipedia.alpha:id/search_src_text")).should(exist).sendKeys("Appium");
        $(AppiumBy.id("org.wikipedia.alpha:id/fragment_search_results"))
                .$(AppiumBy.xpath(".//*[@text='Appium']"))
                .should(exist);
    }

    private static void skipOnboarding() {
        back();
        SelenideElement close = $(AppiumBy.accessibilityId("Close"));
        if (close.exists()) {
            close.click();
        }
    }
}
