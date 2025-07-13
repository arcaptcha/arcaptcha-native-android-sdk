package co.arcaptcha.arcaptcha_native_sdk.managers

import co.arcaptcha.arcaptcha_native_sdk.models.InternalCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.VoiceChallengeData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.CaptchaDataRequest
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI
import co.arcaptcha.arcaptcha_native_sdk.utils.FakeNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoiceChallengeManager(callback: InternalCaptchaCallback) : CaptchaManager(callback) {
    override fun loadCaptcha(coroutineScope: CoroutineScope, arcaptchaAPI: ArcaptchaAPI) {
        val api = preLoadCaptcha(arcaptchaAPI)
        val reqBody = CaptchaDataRequest(arcaptchaAPI, BaseRequest.VOICE_CAPTCHA_TYPE)

        api.getVoiceChallenge(reqBody).enqueue(object : Callback<VoiceChallengeData> {
            override fun onResponse(call: Call<VoiceChallengeData>, response: Response<VoiceChallengeData>) {
                controlCaptchaData(response)
            }

            override fun onFailure(call: Call<VoiceChallengeData>, t: Throwable) {
                onCaptchaError(t.message ?: "Unknown error")
            }
        })
    }
}