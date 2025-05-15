package co.arcaptcha.arcaptcha_native_sdk.managers

import android.util.Log
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.InternalCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.ClassicCaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.CaptchaDataRequest
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI
import co.arcaptcha.arcaptcha_native_sdk.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassicCaptchaManager(callback: InternalCaptchaCallback) : CaptchaManager(callback) {
    override fun loadCaptcha(arcaptchaAPI: ArcaptchaAPI) {
        this.challengeId = null
        this.arcaptchaAPI = arcaptchaAPI
        callback.onStateChanged(CaptchaState.LoadingCaptcha)
        val api = RetrofitClient.getInstance(arcaptchaAPI.apiBaseUrl).api

        val reqBody = CaptchaDataRequest(arcaptchaAPI, BaseRequest.CLASSIC_CAPTCHA_TYPE)
        api.getClassicCaptcha(reqBody).enqueue(object : Callback<ClassicCaptchaData> {
            override fun onResponse(call: Call<ClassicCaptchaData>, response: Response<ClassicCaptchaData>) {
                controlCaptchaData(response)
            }

            override fun onFailure(call: Call<ClassicCaptchaData>, t: Throwable) {
                onCaptchaError(t.message ?: "Unknown error")
            }
        })
    }
}