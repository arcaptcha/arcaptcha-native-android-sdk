package co.arcaptcha.arcaptcha_native_sdk.models.captchas

data class VoiceChallengeData(
    val status: String?,
    val captcha_type: String?,
    val content: Content?
) {
    data class Content(
        val site_key: String?,
        val challenge_id: String?,
        val path: String?,
        val lang: String?,
        val type: String?
    )
}