package co.arcaptcha.arcaptcha_native_sdk.models

import co.arcaptcha.arcaptcha_native_sdk.models.captchas.ClassicCaptchaData

interface CaptchaCallback {
    fun onStateChanged(state: CaptchaState)
    fun onResult(success: Boolean)
    fun onError(message: String)
}

interface ClassicCaptchaCallback : CaptchaCallback {
    fun onCaptchaLoaded(data: ClassicCaptchaData)
}