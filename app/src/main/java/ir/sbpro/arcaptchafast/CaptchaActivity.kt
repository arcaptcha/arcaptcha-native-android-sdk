package ir.sbpro.arcaptchafast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.sbpro.arcaptchafast.databinding.ActivityCaptchaBinding
import co.arcaptcha.arcaptcha_native_sdk.adapters.CaptchaImageAdapter
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
        classicCaptcha.setApi(arcaptchaAPI)
        soundCaptcha.setApi(arcaptchaAPI)
        classicCaptcha.loadCaptcha()
    }
}