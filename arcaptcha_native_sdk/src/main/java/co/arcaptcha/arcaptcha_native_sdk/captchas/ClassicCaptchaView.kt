package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.adapters.CaptchaImageAdapter
import co.arcaptcha.arcaptcha_native_sdk.managers.ClassicCaptchaManager
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.ClassicCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.ClassicCaptchaData


class ClassicCaptchaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CaptchaView(context, attrs), ClassicCaptchaCallback {
    override val manager = ClassicCaptchaManager(this)
    private val captchaAdapter = CaptchaImageAdapter(context)
    val classicCaptchaView = binding.classicCaptcha
    override val captchaBox: LinearLayout = classicCaptchaView.captchaBox

    init {
        toggleButton.setImageResource(R.drawable.ic_volume)
        orientation = VERTICAL

        classicCaptchaView.gridView.adapter = captchaAdapter
        classicCaptchaView.confirmButton.setOnClickListener {
            val selected = captchaAdapter.getSelectedIndices()
            Toast.makeText(context, "انتخاب‌شده‌ها: $selected", Toast.LENGTH_SHORT).show()
        }
    }

    fun getSelectedIndices(): Set<Int> {
        return captchaAdapter.getSelectedIndices()
    }

    override fun onCaptchaLoaded(data: ClassicCaptchaData) {
        Log.d("XQQQStateClassic", "onCaptchaLoaded: ${data.captcha_type}, ${data.status}")
        classicCaptchaView.captchaMessage.setText("تصاویر شامل ${data.content?.category} را انتخاب کنید")
        data.content?.image_urls?.let {
            val finalImageUrls = it.map { item ->
                arcaptchaApi.getImageUrl(item)
            }

            captchaAdapter.setImages(finalImageUrls)
        }
        outerCallback?.onCaptchaLoaded()
    }

    override fun onStateChanged(state: CaptchaState) {
        when (state) {
            CaptchaState.LoadingCaptcha, CaptchaState.SubmittingSolution -> loadingMode()
            CaptchaState.AwaitingUserInput -> contentMode()
            CaptchaState.Done -> {}
            CaptchaState.Error -> {}
        }
        outerCallback?.onStateChanged(state)
    }

    override fun onCorrectAnswer() {
        outerCallback?.onCorrectAnswer()
    }

    override fun onWrongAnswer() {
        outerCallback?.onWrongAnswer()
    }

    override fun onError(message: String) {
        Log.d("XQQQStateError", message)
        outerCallback?.onError(message)
    }
}
