package co.arcaptcha.arcaptcha_native_sdk.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor(baseUrl: String) {
    val api: CaptchaApiInterface

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // برای دیدن کامل body درخواست و پاسخ
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        api = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
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