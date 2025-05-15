package co.arcaptcha.arcaptcha_native_sdk.managers

import android.util.Log
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.InternalCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.CaptchaAnswerResponse
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.CaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.ClassicAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.SlideAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.VoiceAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI
import co.arcaptcha.arcaptcha_native_sdk.remote.CaptchaApiInterface
import co.arcaptcha.arcaptcha_native_sdk.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class CaptchaManager(protected val callback: InternalCaptchaCallback) {
    protected var arcaptchaAPI: ArcaptchaAPI? = null
    protected var challengeId: String? = null

    abstract fun loadCaptcha(arcaptchaAPI: ArcaptchaAPI)

    fun <T: CaptchaData> controlCaptchaData(response: Response<T>){
        if (response.isSuccessful && response.body()?.content?.challenge_id != null) {
            challengeId = response.body()!!.content!!.challenge_id
            callback.onCaptchaLoaded(response.body()!!)
            callback.onStateChanged(CaptchaState.AwaitingUserInput)
        } else {
            onCaptchaError("Failed to load captcha.")
        }
    }

    fun preLoadCaptcha(arcaptchaAPI: ArcaptchaAPI) : CaptchaApiInterface {
        this.challengeId = null
        this.arcaptchaAPI = arcaptchaAPI
        callback.onStateChanged(CaptchaState.LoadingCaptcha)
        return RetrofitClient.getInstance(arcaptchaAPI.apiBaseUrl).api
    }

    fun onCaptchaError(message: String){
        callback.onStateChanged(CaptchaState.Error)
        callback.onError(message)
        challengeId = null
    }

    fun submitAnswer(requestBody: BaseAnswerRequest){
        if(arcaptchaAPI == null || this.challengeId == null) return
        callback.onStateChanged(CaptchaState.SubmittingSolution)

        val api = RetrofitClient.getInstance(arcaptchaAPI!!.apiBaseUrl).api

        val retrofitRequest = object : Callback<CaptchaAnswerResponse> {
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
                    onCaptchaError("Failed to submit captcha.")
                }
            }

            override fun onFailure(call: Call<CaptchaAnswerResponse>, t: Throwable) {
                onCaptchaError(t.message ?: "Unknown error")
            }
        }

        Log.d("XQQQSS1", "submitAnswer: ")
        if(requestBody is ClassicAnswerRequest) api.submitClassicCaptchaAnswer(requestBody).enqueue(retrofitRequest)
        else if(requestBody is VoiceAnswerRequest) api.submitVoiceChallengeAnswer(requestBody).enqueue(retrofitRequest)
        else if(requestBody is SlideAnswerRequest) api.submitSlidePuzzleAnswer(requestBody).enqueue(retrofitRequest)
        else Log.d("XQQQS2", "ELSE")
    }
}