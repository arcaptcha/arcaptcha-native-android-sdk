package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import co.arcaptcha.arcaptcha_native_sdk.adapters.CaptchaImageAdapter
import co.arcaptcha.arcaptcha_native_sdk.databinding.CaptchaViewBinding

class CaptchaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val binding: CaptchaViewBinding =
        CaptchaViewBinding.inflate(LayoutInflater.from(context), this, true)

    private val captchaAdapter = CaptchaImageAdapter(context)

    init {
        orientation = VERTICAL
        binding.gridView.adapter = captchaAdapter

        binding.confirmButton.setOnClickListener {
            val selected = captchaAdapter.getSelectedIndices()
            Toast.makeText(context, "انتخاب‌شده‌ها: $selected", Toast.LENGTH_SHORT).show()
        }
    }

    fun setImages(urls: List<String>) {
        captchaAdapter.setImages(urls)
    }

    fun getSelectedIndices(): Set<Int> {
        return captchaAdapter.getSelectedIndices()
    }
}
