package co.arcaptcha.arcaptcha_native_sdk.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor(baseUrl: String) {
    val api: CaptchaApiInterface

    init {
        api = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CaptchaApiInterface::class.java)
    }

    companion object {
        @Volatile private var INSTANCE: RetrofitClient? = null

        fun getInstance(baseUrl: String): RetrofitClient {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RetrofitClient(baseUrl).also { INSTANCE = it }
            }
        }
    }
}