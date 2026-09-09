# java-selenide-appium

JUnit 5 + Selenide + Appium. Учебный репозиторий [QA.GURU](https://qa.guru) Java.

Ключи BrowserStack: скопируй `src/test/resources/browserstack.properties.example` в `browserstack.properties` (файл в `.gitignore`). Либо `-Dbrowserstack.user` / `-Dbrowserstack.key`.

Android (не `bs://sample.app`): загрузи Wikipedia alpha и положи `browserstack.app=bs://…` в properties:

```bash
curl -u "$BROWSERSTACK_USERNAME:$BROWSERSTACK_ACCESS_KEY" \
  -X POST "https://api-cloud.browserstack.com/app-automate/upload" \
  -F "url=https://github.com/wikimedia/apps-android-wikipedia/releases/download/latest/app-alpha-universal-release.apk"
```

```bash
./gradlew test -Dplatform=android
./gradlew test -Dplatform=ios
```
