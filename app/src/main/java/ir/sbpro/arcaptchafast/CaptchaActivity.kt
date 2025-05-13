package ir.sbpro.arcaptchafast

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ir.sbpro.arcaptchafast.databinding.ActivityCaptchaBinding
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

class CaptchaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaptchaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptchaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val questArcApi = ArcaptchaAPI("afge5xjsq6", "localhost")
        val puzzleArcApi = ArcaptchaAPI("bq44zwr6cn", "localhost")

        val classicCaptcha = binding.classicCaptchaView
        val soundCaptcha = binding.soundCaptchaView
        val slideCaptcha = binding.slideCaptchaView

        classicCaptcha.initCaptcha(questArcApi) {
            Log.d("XQQQOutBoxOnCorrect", "HaBeleJan Classic")
        }

        /*classicCaptcha.initCaptcha(arcaptchaAPI, object : CaptchaCallback {
            override fun onCorrectAnswer() {
                Log.d("XQQQOutBoxOnCorrect", "HaBeleJan")
            }
        })*/

        soundCaptcha.initCaptcha(questArcApi) {
            Log.d("XQQQOutBoxOnCorrect", "HaBeleJan Sound")
        }

        slideCaptcha.initCaptcha(puzzleArcApi) {
            Log.d("XQQQOutBoxOnCorrect", "HaBeleJan Slide")
        }

        classicCaptcha.loadCaptcha()
        soundCaptcha.loadCaptcha()
        slideCaptcha.loadCaptcha()
    }
}