package co.arcaptcha.arcaptcha_native_sdk.models

import co.arcaptcha.arcaptcha_native_sdk.models.captchas.CaptchaData

fun interface BaseCaptchaCallback {
    fun onStateChanged(state: CaptchaState){}
    fun onCorrectAnswer(token: String)
    fun onWrongAnswer(){}
    fun onError(message: String){}
}

fun interface CaptchaCallback : BaseCaptchaCallback {
    fun onCaptchaLoaded(){}
}

interface InternalCaptchaCallback : BaseCaptchaCallback {
    fun onCaptchaLoaded(data: CaptchaData)
}

