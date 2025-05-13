package ir.sbpro.arcaptchafast

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaCallback
import ir.sbpro.arcaptchafast.databinding.ActivityCaptchaBinding
import co.arcaptcha.arcaptcha_native_sdk.models.InternalCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

class CaptchaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaptchaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptchaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val classicCaptcha = binding.classicCaptchaView
        val soundCaptcha = binding.soundCaptchaView

        val arcaptchaAPI = ArcaptchaAPI("afge5xjsq6", "localhost")
        classicCaptcha.initCaptcha(arcaptchaAPI) {
            Log.d("XQQQOutBoxOnCorrect", "HaBeleJan")
        }

        /*classicCaptcha.initCaptcha(arcaptchaAPI, object : CaptchaCallback {
            override fun onCorrectAnswer() {
                Log.d("XQQQOutBoxOnCorrect", "HaBeleJan")
            }
        })*/

        soundCaptcha.initCaptcha(arcaptchaAPI) {
            Log.d("XQQQOutBoxOnCorrect", "HaBeleJan")
        }

        classicCaptcha.loadCaptcha()
        soundCaptcha.loadCaptcha()
    }
}