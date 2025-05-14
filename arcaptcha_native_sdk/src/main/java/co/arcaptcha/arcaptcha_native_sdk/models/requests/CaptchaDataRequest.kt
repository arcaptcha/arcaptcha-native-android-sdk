package co.arcaptcha.arcaptcha_native_sdk.models.requests

import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

class CaptchaDataRequest(
    arcApi: ArcaptchaAPI,
    var captcha_type: String,
    val bowman_id: String = "",
    val components: CaptchaRequestComponents = CaptchaRequestComponents(),
    val fp: String = "",
    val referer: String = "",
    val system_time: String = ""
) : BaseRequest(arcApi)

data class CaptchaRequestComponents(
    val botD: BotD = BotD("vb1"),
    val fingerprintComponents: FingerprintComponents = FingerprintComponents("vc1"),
    val mouseTracker: MouseTracker = MouseTracker("vm1"),
    val timeTaking: Int = 5000
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
