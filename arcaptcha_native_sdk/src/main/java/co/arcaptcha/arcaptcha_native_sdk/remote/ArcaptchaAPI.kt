package co.arcaptcha.arcaptcha_native_sdk.remote

class ArcaptchaAPI (siteKey: String, domain: String) {
    var apiBaseUrl: String = "https://android-api.arcaptcha.co/"

    fun getImageUrl(resourceId: String) : String {
        return "${apiBaseUrl}arcaptcha/api/images/${resourceId}"
    }

    fun getVoiceUrl(resourceId: String) : String {
        return "${apiBaseUrl}arcaptcha/api/voices/${resourceId}"
    }
}