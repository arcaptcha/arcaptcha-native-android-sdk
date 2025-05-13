package co.arcaptcha.arcaptcha_native_sdk.models

import co.arcaptcha.arcaptcha_native_sdk.models.captchas.ClassicCaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.VoiceChallengeData

fun interface InternalCaptchaCallback {
    fun onStateChanged(state: CaptchaState){}
    fun onCorrectAnswer()
    fun onWrongAnswer(){}
    fun onError(message: String){}
}

fun interface CaptchaCallback : InternalCaptchaCallback {
    fun onCaptchaLoaded(){}
}

interface ClassicCaptchaCallback : InternalCaptchaCallback {
    fun onCaptchaLoaded(data: ClassicCaptchaData)
}

interface VoiceChallengeCallback : InternalCaptchaCallback {
    fun onCaptchaLoaded(data: VoiceChallengeData)
}