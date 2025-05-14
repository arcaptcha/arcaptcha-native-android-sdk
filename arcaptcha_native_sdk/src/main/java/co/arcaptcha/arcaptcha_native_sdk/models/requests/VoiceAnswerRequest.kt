package co.arcaptcha.arcaptcha_native_sdk.models.requests

import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

class VoiceAnswerRequest(
    arcApi: ArcaptchaAPI,
    challenge_id: String,
    val  answers: String
) : BaseAnswerRequest(arcApi, challenge_id)