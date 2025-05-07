package co.arcaptcha.arcaptcha_native_sdk.remote

import co.arcaptcha.arcaptcha_native_sdk.models.captchas.ClassicCaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.ClassicCaptchaRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CaptchaApiInterface {
    @POST("arcaptcha/api/create")
    fun getClassicCaptcha(@Body answer: ClassicCaptchaRequest): Call<ClassicCaptchaData>
}