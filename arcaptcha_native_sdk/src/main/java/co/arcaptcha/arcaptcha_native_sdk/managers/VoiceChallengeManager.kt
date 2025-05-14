package co.arcaptcha.arcaptcha_native_sdk.managers

import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.VoiceChallengeCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.VoiceChallengeData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.CaptchaDataRequest
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI
import co.arcaptcha.arcaptcha_native_sdk.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoiceChallengeManager(private val callback: VoiceChallengeCallback) : CaptchaManager {
    override fun loadCaptcha(arcaptchaAPI: ArcaptchaAPI) {
        callback.onStateChanged(CaptchaState.LoadingCaptcha)
        val api = RetrofitClient.getInstance(arcaptchaAPI.apiBaseUrl).api

        val reqBody = CaptchaDataRequest(arcaptchaAPI, BaseRequest.VOICE_CAPTCHA_TYPE)
        api.getVoiceChallenge(reqBody).enqueue(object : Callback<VoiceChallengeData> {
            override fun onResponse(call: Call<VoiceChallengeData>, response: Response<VoiceChallengeData>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onCaptchaLoaded(response.body()!!)
                    callback.onStateChanged(CaptchaState.AwaitingUserInput)
                } else {
                    callback.onStateChanged(CaptchaState.Error)
                    callback.onError("Failed to load captcha.")
                }
            }

            override fun onFailure(call: Call<VoiceChallengeData>, t: Throwable) {
                callback.onStateChanged(CaptchaState.Error)
                callback.onError(t.message ?: "Unknown error")
            }
        })
    }
}