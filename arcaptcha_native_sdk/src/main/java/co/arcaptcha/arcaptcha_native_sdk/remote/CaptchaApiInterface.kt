package co.arcaptcha.arcaptcha_native_sdk.remote

import co.arcaptcha.arcaptcha_native_sdk.models.captchas.ClassicCaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.SlidePuzzleData
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.VoiceChallengeData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.CaptchaDataRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CaptchaApiInterface {
    @POST("arcaptcha/api/create")
    fun getClassicCaptcha(@Body answer: CaptchaDataRequest): Call<ClassicCaptchaData>

    @POST("arcaptcha/api/create")
    fun getSlidePuzzle(@Body answer: CaptchaDataRequest): Call<SlidePuzzleData>

    @POST("arcaptcha/api/create/voice")
    fun getVoiceChallenge(@Body answer: CaptchaDataRequest): Call<VoiceChallengeData>
}