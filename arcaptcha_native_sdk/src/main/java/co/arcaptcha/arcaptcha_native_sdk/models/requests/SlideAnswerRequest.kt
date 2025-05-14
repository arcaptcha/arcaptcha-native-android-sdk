package co.arcaptcha.arcaptcha_native_sdk.models.requests

import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

class SlideAnswerRequest(
    arcApi: ArcaptchaAPI,
    val challenge_id: String,
    val  answers: Int,
    val  drag_drop: Int,
    val  drag_drop_time: Int
) : BaseRequest(arcApi)