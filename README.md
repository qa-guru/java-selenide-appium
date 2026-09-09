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
./gradlew test -Dplatform=android -DdeviceHost=real -Dudid=<adb udid>
./gradlew test -Dplatform=ios
```

`-DdeviceHost=` — `browserstack` (дефолт) или `real`. На `real` ключи BrowserStack не нужны: сессия идёт на локальный Appium (`ensure.py appium-wd-hub`). Обязателен `-Dudid=` с USB-устройства (`adb devices`); эмулятор не подставлять. APK — Wikipedia alpha с GitHub `latest` в `apps/` (`*.apk` в `.gitignore`) или `-Dandroid.app=`.
