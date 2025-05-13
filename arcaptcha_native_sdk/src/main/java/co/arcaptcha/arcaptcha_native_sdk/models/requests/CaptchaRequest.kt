package co.arcaptcha.arcaptcha_native_sdk.models.requests

data class CaptchaRequest(
    val site_key: String?,
    val domain: String?,
    val bowman_id: String?,
    var captcha_type: String?,
    val components: Components?,
    val fp: String?,
    val referer: String?,
    val system_time: String?
) {
    data class Components(
        val botD: BotD?,
        val fingerprintComponents: FingerprintComponents?,
        val mouseTracker: MouseTracker?,
        val timeTaking: Int?
    ) {
        data class BotD(
            val kb1: String?
        )

        data class FingerprintComponents(
            val kc1: String?
        )

        data class MouseTracker(
            val km1: String?
        )
    }
}

//used for question and voice challenges
val questionRequest = CaptchaRequest(
    site_key = "afge5xjsq6",
    domain = "localhost",
    bowman_id = "dsads",
    captcha_type = "question",
    components = CaptchaRequest.Components(
        botD = CaptchaRequest.Components.BotD("vb1"),
        fingerprintComponents = CaptchaRequest.Components.FingerprintComponents("vc1"),
        mouseTracker = CaptchaRequest.Components.MouseTracker("vm1"),
        timeTaking = 5000
    ),
    fp = "testfp",
    referer = "folan",
    system_time = "2023-07-03T14:30:24+03:30"
)

//used for slide puzzles
val puzzleRequest = questionRequest.copy().let {
    it.captcha_type = "puzzle"
}
