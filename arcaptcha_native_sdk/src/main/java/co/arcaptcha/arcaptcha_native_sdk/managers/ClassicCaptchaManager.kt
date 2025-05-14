package co.arcaptcha.arcaptcha_native_sdk.managers

import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.ClassicCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.ClassicCaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.CaptchaDataRequest
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI
import co.arcaptcha.arcaptcha_native_sdk.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassicCaptchaManager(callback: ClassicCaptchaCallback) : CaptchaManager(callback) {
    override fun loadCaptcha(arcaptchaAPI: ArcaptchaAPI) {
        this.challengeId = null
        this.arcaptchaAPI = arcaptchaAPI
        callback.onStateChanged(CaptchaState.LoadingCaptcha)
        val api = RetrofitClient.getInstance(arcaptchaAPI.apiBaseUrl).api

        val reqBody = CaptchaDataRequest(arcaptchaAPI, BaseRequest.CLASSIC_CAPTCHA_TYPE)
        val ccmThis = this
        api.getClassicCaptcha(reqBody).enqueue(object : Callback<ClassicCaptchaData> {
            override fun onResponse(call: Call<ClassicCaptchaData>, response: Response<ClassicCaptchaData>) {
                if (response.isSuccessful && response.body()?.content?.challenge_id != null) {
                    ccmThis.challengeId = response.body()!!.content!!.challenge_id
                    (callback as ClassicCaptchaCallback).onCaptchaLoaded(response.body()!!)
                    callback.onStateChanged(CaptchaState.AwaitingUserInput)
                } else {
                    callback.onStateChanged(CaptchaState.Error)
                    callback.onError("Failed to load captcha.")
                    ccmThis.challengeId = null
                }
            }

            override fun onFailure(call: Call<ClassicCaptchaData>, t: Throwable) {
                callback.onStateChanged(CaptchaState.Error)
                callback.onError(t.message ?: "Unknown error")
                ccmThis.challengeId = null
            }
        })
    }
}