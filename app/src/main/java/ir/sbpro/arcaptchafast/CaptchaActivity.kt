package ir.sbpro.arcaptchafast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.sbpro.arcaptchafast.databinding.ActivityCaptchaBinding
import co.arcaptcha.arcaptcha_native_sdk.adapters.CaptchaImageAdapter

class CaptchaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaptchaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptchaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.captchaView.setImages(listOf(
            "https://yavuzceliker.github.io/sample-images/image-1.jpg",
            "https://yavuzceliker.github.io/sample-images/image-2.jpg",
            "https://yavuzceliker.github.io/sample-images/image-3.jpg",
            "https://yavuzceliker.github.io/sample-images/image-4.jpg",
            "https://yavuzceliker.github.io/sample-images/image-5.jpg",
            "https://yavuzceliker.github.io/sample-images/image-6.jpg",
            "https://yavuzceliker.github.io/sample-images/image-7.jpg",
            "https://yavuzceliker.github.io/sample-images/image-8.jpg",
            "https://yavuzceliker.github.io/sample-images/image-9.jpg"
        ))
    }
}