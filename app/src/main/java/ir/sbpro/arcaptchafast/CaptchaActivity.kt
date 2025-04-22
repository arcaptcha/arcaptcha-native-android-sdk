package ir.sbpro.arcaptchafast

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ir.sbpro.arcaptchafast.databinding.ActivityCaptchaBinding

class CaptchaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaptchaBinding
    private lateinit var captchaAdapter: CaptchaImageAdapter

    private val imageUrls = listOf(
        "https://example.com/image1.jpg",
        "https://example.com/image2.jpg",
        "https://example.com/image3.jpg",
        "https://example.com/image4.jpg",
        "https://example.com/image5.jpg",
        "https://example.com/image6.jpg",
        "https://example.com/image7.jpg",
        "https://example.com/image8.jpg",
        "https://example.com/image9.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptchaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        captchaAdapter = CaptchaImageAdapter(this, imageUrls)
        binding.captchaGrid.gridView.adapter = captchaAdapter

        binding.captchaGrid.confirmButton.setOnClickListener {
            val selected = captchaAdapter.getSelectedIndices()
            Toast.makeText(this, "انتخاب‌شده‌ها: $selected", Toast.LENGTH_SHORT).show()
        }
    }
}