package co.arcaptcha.arcaptcha_native_sdk.managers

import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.ClassicCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.SlidePuzzleCallback
import co.arcaptcha.arcaptcha_native_sdk.models.VoiceChallengeCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.ClassicCaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.SlidePuzzleData
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.VoiceChallengeData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.puzzleRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.questionRequest
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI
import co.arcaptcha.arcaptcha_native_sdk.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SlidePuzzleManager(private val callback: SlidePuzzleCallback) : CaptchaManager {
    override fun loadCaptcha(arcaptchaAPI: ArcaptchaAPI) {
        callback.onStateChanged(CaptchaState.LoadingCaptcha)
        val api = RetrofitClient.getInstance(arcaptchaAPI.apiBaseUrl).api

        val reqBody = puzzleRequest.copy().apply {
            this.site_key = arcaptchaAPI.siteKey
        }

        api.getSlidePuzzle(reqBody).enqueue(object : Callback<SlidePuzzleData> {
            override fun onResponse(call: Call<SlidePuzzleData>, response: Response<SlidePuzzleData>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onCaptchaLoaded(response.body()!!)
                    callback.onStateChanged(CaptchaState.AwaitingUserInput)
                } else {
                    callback.onStateChanged(CaptchaState.Error)
                    callback.onError("Failed to load captcha.")
                }
            }

            override fun onFailure(call: Call<SlidePuzzleData>, t: Throwable) {
                callback.onStateChanged(CaptchaState.Error)
                callback.onError(t.message ?: "Unknown error")
            }
        })
    }
}