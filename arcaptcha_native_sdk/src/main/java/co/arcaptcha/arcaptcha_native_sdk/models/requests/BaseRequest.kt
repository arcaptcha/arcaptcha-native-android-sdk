package co.arcaptcha.arcaptcha_native_sdk.models.requests

import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

open class BaseRequest(arcApi: ArcaptchaAPI){
    public lateinit var site_key: String
    public lateinit var domain: String

    init {
        site_key = arcApi.siteKey
        domain = arcApi.domain
    }

    companion object {
        const val CLASSIC_CAPTCHA_TYPE = "question"
        const val VOICE_CAPTCHA_TYPE = "question"
        const val SLIDE_CAPTCHA_TYPE = "puzzle"
    }
}