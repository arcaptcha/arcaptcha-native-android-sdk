package co.arcaptcha.arcaptcha_native_sdk.managers

import co.arcaptcha.arcaptcha_native_sdk.models.InternalCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.SlidePuzzleData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.CaptchaDataRequest
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SlidePuzzleManager(callback: InternalCaptchaCallback) : CaptchaManager(callback) {
    override fun loadCaptcha(coroutineScope: CoroutineScope, arcaptchaAPI: ArcaptchaAPI) {
        val api = preLoadCaptcha(arcaptchaAPI)
        val reqBody = CaptchaDataRequest(arcaptchaAPI, BaseRequest.SLIDE_CAPTCHA_TYPE)

        api.getSlidePuzzle(reqBody).enqueue(object : Callback<SlidePuzzleData> {
            override fun onResponse(call: Call<SlidePuzzleData>, response: Response<SlidePuzzleData>) {
                controlCaptchaData(response)
            }

            override fun onFailure(call: Call<SlidePuzzleData>, t: Throwable) {
                onCaptchaError(t.message ?: "Unknown error")
            }
        })
    }
}