package co.arcaptcha.arcaptcha_native_sdk.containers

import android.content.Context
import android.util.AttributeSet
import co.arcaptcha.arcaptcha_native_sdk.captchas.CaptchaView

class QuestionContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ChallengeContainerView(context, attrs) {
    override val imageCaptchaView = binding.classicCaptchaView
    override val soundCaptchaView = binding.soundCaptchaView
    override var activeCaptcha: CaptchaView = imageCaptchaView

    init {
        orientation = VERTICAL
    }
}