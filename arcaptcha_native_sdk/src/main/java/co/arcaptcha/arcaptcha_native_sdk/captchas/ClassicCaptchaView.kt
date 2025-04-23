package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.Toast
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.adapters.CaptchaImageAdapter
import co.arcaptcha.arcaptcha_native_sdk.utils.FakeNetwork
import kotlinx.coroutines.launch


class ClassicCaptchaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CaptchaView(context, attrs) {
    private val captchaAdapter = CaptchaImageAdapter(context)
    override val captchaBox: LinearLayout = binding.classicCaptcha.captchaBox

    init {
        toggleButton.setImageResource(R.drawable.ic_volume)
        orientation = VERTICAL

        binding.classicCaptcha.gridView.adapter = captchaAdapter
        binding.classicCaptcha.confirmButton.setOnClickListener {
            val selected = captchaAdapter.getSelectedIndices()
            Toast.makeText(context, "انتخاب‌شده‌ها: $selected", Toast.LENGTH_SHORT).show()
        }

        fetchCaptcha()
    }

    fun setImages(urls: List<String>) {
        captchaAdapter.setImages(urls)
    }

    fun getSelectedIndices(): Set<Int> {
        return captchaAdapter.getSelectedIndices()
    }

    override fun fetchCaptcha() {
        loadingMode()
        coroutineScope.launch {
            FakeNetwork.request({
                contentMode()
            })
        }
    }
}
