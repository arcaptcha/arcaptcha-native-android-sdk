## Android SDK for ARCaptcha

###### [Installation](#installation) | [Example](#display-a-arcaptcha-challenge)

This SDK provides a native sdk for [ARCaptcha](https://www.arcaptcha.ir). You will need to configure a `site key` and a `secret key` from your arcaptcha account in order to use it.


## Installation

### Gradle
<pre>
// Step 1. Add it in your root settings.gradle at the end of repositories:
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
// Step 2. Add the dependency
dependencies {
    implementation 'com.github.arcaptcha:arcaptcha-native-android-sdk:v1.0.1'
}
</pre>

### Maven
```xml
//Step 1. Add to pom.xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

//Step 2. Add the dependency
<dependency>
    <groupId>com.github.arcaptcha</groupId>
    <artifactId>arcaptcha-native-android-sdk</artifactId>
    <version>v1.0.1</version>
</dependency>
```



## Display a Question challenge

The following snippet code will ask the user to complete a Question challenge. 

### XML
```xml
<co.arcaptcha.arcaptcha_native_sdk.containers.QuestionContainerView
    android:id="@+id/mainQuestionContainer"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

### Kotlin
```kotlin
val questArcApi = ArcaptchaAPI("<YOUR_SITE_KEY>", "<YOUR_DOMAIN>")

mainQuestContainer.initCaptcha(questArcApi, object : CaptchaCallback {
    override fun onCorrectAnswer(token: String) {
        Log.d("Puzzle Token", token)
    }

    override fun onError(message: String) {
        Log.d("Puzzle Error", message)
    }

    override fun onWrongAnswer() {
        mainQuestContainer.loadCaptcha()
    }

    override fun onStateChanged(state: CaptchaState) {
        if(state == CaptchaState.LoadingCaptcha) {
        }
    }
})

mainQuestContainer.loadImageCaptcha()
// OR: mainQuestContainer.loadVoiceCaptcha()
```

## Display a Puzzle challenge

The following snippet code will ask the user to complete a Puzzle challenge.

### XML
```xml
<co.arcaptcha.arcaptcha_native_sdk.containers.PuzzleContainerView
    android:id="@+id/puzzleContainer"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

### Kotlin
```kotlin
val puzzleArcApi = ArcaptchaAPI("<YOUR_SITE_KEY>", "<YOUR_DOMAIN>")

puzzleContainer.initCaptcha(puzzleArcApi, object : CaptchaCallback {
    override fun onCorrectAnswer(token: String) {
        Log.d("Puzzle Token", token)
    }

    override fun onError(message: String) {
        Log.d("Puzzle Error", message)
    }

    override fun onWrongAnswer() {
        puzzleContainer.loadCaptcha()
    }

    override fun onStateChanged(state: CaptchaState) {
        if(state == CaptchaState.LoadingCaptcha) {
        }
    }
})

puzzleContainer.loadCaptcha()
```
