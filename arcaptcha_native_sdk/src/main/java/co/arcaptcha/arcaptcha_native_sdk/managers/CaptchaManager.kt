package co.arcaptcha.arcaptcha_native_sdk.managers

import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.ClassicCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.InternalCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.CaptchaAnswerResponse
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI
import co.arcaptcha.arcaptcha_native_sdk.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class CaptchaManager(protected val callback: InternalCaptchaCallback) {
    protected var arcaptchaAPI: ArcaptchaAPI? = null
    protected var challengeId: String? = null

    abstract fun loadCaptcha(arcaptchaAPI: ArcaptchaAPI)

    fun submitAnswer(requestBody: BaseAnswerRequest){
        if(arcaptchaAPI == null || this.challengeId == null) return
        callback.onStateChanged(CaptchaState.SubmittingSolution)

        val api = RetrofitClient.getInstance(arcaptchaAPI!!.apiBaseUrl).api
        //val reqBody = ClassicAnswerRequest.getInstance(arcaptchaAPI!!, challengeId!!, imagesStatus, tbSelections)

        api.submitCaptchaAnswer(requestBody).enqueue(object : Callback<CaptchaAnswerResponse> {
            override fun onResponse(call: Call<CaptchaAnswerResponse>, response: Response<CaptchaAnswerResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    if(response.body()!!.success){
                        callback.onCorrectAnswer()
                        callback.onStateChanged(CaptchaState.Done)
                    }
                    else {
                        callback.onWrongAnswer()
                        callback.onStateChanged(CaptchaState.WrongAnswer)
                    }
                } else {
                    callback.onStateChanged(CaptchaState.Error)
                    callback.onError("Failed to submit captcha.")
                }
            }

            override fun onFailure(call: Call<CaptchaAnswerResponse>, t: Throwable) {
                callback.onStateChanged(CaptchaState.Error)
                callback.onError(t.message ?: "Unknown error")
            }
        })
    }
}