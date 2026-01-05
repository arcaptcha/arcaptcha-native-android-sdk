package co.arcaptcha.arcaptcha_native_sdk.remote

class ArcaptchaAPI (val siteKey: String, val domain: String) {
    var apiBaseUrl: String = "https://android-api.arcaptcha.ir/"

    fun getImageUrl(resourceId: String) : String {
        return "${apiBaseUrl}arcaptcha/api/images/${resourceId}"
    }

    fun getOriginalImageUrl(resourceId: String) : String {
        return "${apiBaseUrl}arcaptcha/api/puzzles/${siteKey}/:original_image_id/${resourceId}"
    }

    fun getVoiceUrl(resourceId: String) : String {
        return "${apiBaseUrl}arcaptcha/api/voices/${resourceId}"
    }
}