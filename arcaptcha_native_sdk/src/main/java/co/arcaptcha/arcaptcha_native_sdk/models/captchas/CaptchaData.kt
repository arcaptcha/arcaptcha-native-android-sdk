package co.arcaptcha.arcaptcha_native_sdk.models.captchas

open class CaptchaData(
    val captcha_type: String?,
    val status: String?,
    val content: CaptchaContent?
)

open class CaptchaContent (
    val challenge_id: String?,
    val site_key: String,
    val category: String? = null,
    val image_urls: List<String>? = null,
    val puzzle_background: String? = null,
    val puzzle_piece: String? = null,
    val y: Int? = null,
    val path: String? = null,
    val lang: String? = null,
    val type: String? = null
)