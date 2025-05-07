package co.arcaptcha.arcaptcha_native_sdk.models.captchas

data class ClassicCaptchaData(
    val captcha_type: String?,
    val content: Content?,
    val status: String?
) {
    data class Content(
        val category: String?,
        val challenge_id: String?,
        val image_urls: List<String>?,
        val site_key: String?
    )
}