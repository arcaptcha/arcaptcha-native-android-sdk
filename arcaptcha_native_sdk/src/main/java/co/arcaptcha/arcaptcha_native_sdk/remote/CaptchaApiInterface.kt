package co.arcaptcha.arcaptcha_native_sdk.remote

import co.arcaptcha.arcaptcha_native_sdk.models.responses.CaptchaAnswerResponse
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.ClassicCaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.SlidePuzzleData
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.VoiceChallengeData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.CaptchaDataRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.ClassicAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.SlideAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.VoiceAnswerRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CaptchaApiInterface {
    @POST("arcaptcha/api/create")
    fun getClassicCaptcha(@Body answer: CaptchaDataRequest): Call<ClassicCaptchaData>

    @POST("arcaptcha/api/create/voice")
    fun getVoiceChallenge(@Body answer: CaptchaDataRequest): Call<VoiceChallengeData>

    @POST("arcaptcha/api/create")
    fun getSlidePuzzle(@Body answer: CaptchaDataRequest): Call<SlidePuzzleData>

    @POST("arcaptcha/api/answer")
    fun submitClassicCaptchaAnswer(@Body answer: ClassicAnswerRequest): Call<CaptchaAnswerResponse>

    @POST("arcaptcha/api/answer")
    fun submitVoiceChallengeAnswer(@Body answer: VoiceAnswerRequest): Call<CaptchaAnswerResponse>

    @POST("arcaptcha/api/answer")
    fun submitSlidePuzzleAnswer(@Body answer: SlideAnswerRequest): Call<CaptchaAnswerResponse>
}