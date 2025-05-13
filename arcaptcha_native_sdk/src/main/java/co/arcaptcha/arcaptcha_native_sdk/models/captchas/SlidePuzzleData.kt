package co.arcaptcha.arcaptcha_native_sdk.models.captchas

data class SlidePuzzleData(
    val status: String?,
    val captcha_type: String?,
    val content: Content?
) {
    data class Content(
        val challenge_id: String?,
        val site_key: String?,
        val puzzle_background: String?,
        val puzzle_piece: String?,
        val y: Int?
    )
}