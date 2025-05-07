package co.arcaptcha.arcaptcha_native_sdk.managers

import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

interface CaptchaManager {
    fun loadCaptcha(arcaptchaAPI: ArcaptchaAPI)
}