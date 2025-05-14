package co.arcaptcha.arcaptcha_native_sdk.models.requests

import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

class VoiceAnswerRequest(
    arcApi: ArcaptchaAPI,
    val challenge_id: String,
    val  answers: String
) : BaseRequest(arcApi)