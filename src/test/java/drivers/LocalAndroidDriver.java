package drivers;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class LocalAndroidDriver implements WebDriverProvider {

    private static final String WIKIPEDIA_ALPHA_APK =
            "https://github.com/wikimedia/apps-android-wikipedia/releases/download/latest/app-alpha-universal-release.apk";
    private static final Path DEFAULT_APK = Path.of("apps", "app-alpha-universal-release.apk");

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
        if (!"android".equals(System.getProperty("platform", ""))) {
            throw new IllegalStateException("STOP: -DdeviceHost=real is Android-only");
        }
        String udid = System.getProperty("udid", "");
        if (udid.isBlank()) {
            throw new IllegalStateException("STOP: pass -Dudid= (adb devices). Do not use an emulator.");
        }

        UiAutomator2Options options = new UiAutomator2Options();
        options.setAutomationName("UIAutomator2");
        options.setPlatformName("Android");
        options.setUdid(udid);
        options.setApp(apkPath());
        options.setAppPackage("org.wikipedia.alpha");
        options.setAppActivity("org.wikipedia.main.MainActivity");
        options.setAppWaitActivity("org.wikipedia.*");
        options.setCapability("appium:ignoreHiddenApiPolicyError", true);
        options.setAutoGrantPermissions(true);
        return new AndroidDriver(hubUrl(), options);
    }

    private static String apkPath() {
        String configured = System.getProperty("android.app", "");
        Path path;
        if (!configured.isBlank()) {
            path = resolveExisting(configured, "-Dandroid.app=");
            return path.toString();
        }
        path = Path.of(System.getProperty("user.dir")).resolve(DEFAULT_APK).toAbsolutePath().normalize();
        if (!Files.isRegularFile(path)) {
            downloadWikipediaAlpha(path);
        }
        return path.toString();
    }

    private static Path resolveExisting(String configured, String source) {
        Path path = Path.of(configured);
        if (!path.isAbsolute()) {
            path = Path.of(System.getProperty("user.dir")).resolve(path);
        }
        path = path.toAbsolutePath().normalize();
        if (!Files.isRegularFile(path)) {
            throw new IllegalStateException("STOP: APK not found at " + path + " (" + source + ")");
        }
        return path;
    }

    private static void downloadWikipediaAlpha(Path dest) {
        try {
            Files.createDirectories(dest.getParent());
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();
            HttpRequest request = HttpRequest.newBuilder(URI.create(WIKIPEDIA_ALPHA_APK))
                    .timeout(Duration.ofMinutes(3))
                    .GET()
                    .build();
            HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(dest));
            if (response.statusCode() < 200 || response.statusCode() >= 300
                    || !Files.isRegularFile(dest) || Files.size(dest) < 1_000_000) {
                Files.deleteIfExists(dest);
                throw new IllegalStateException(
                        "STOP: failed to download Wikipedia alpha APK (HTTP " + response.statusCode()
                                + "). Pass -Dandroid.app= to a local file.");
            }
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new IllegalStateException(
                    "STOP: download Wikipedia alpha APK from GitHub latest, or pass -Dandroid.app=", e);
        }
    }

    private static java.net.URL hubUrl() {
        String spec = System.getProperty("appiumUrl", "http://127.0.0.1:4723/wd/hub");
        if (spec.endsWith("/status")) {
            spec = spec.substring(0, spec.length() - "/status".length());
        }
        try {
            return URI.create(spec).toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            throw new IllegalStateException("Invalid Appium hub URL", e);
        }
    }
}
