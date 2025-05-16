package co.arcaptcha.arcaptcha_native_sdk.containers

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import co.arcaptcha.arcaptcha_native_sdk.captchas.CaptchaView
import co.arcaptcha.arcaptcha_native_sdk.databinding.ChallengeContainerViewBinding
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

abstract class ChallengeContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    protected val binding = ChallengeContainerViewBinding.inflate(LayoutInflater.from(context), this, true)
    abstract val imageCaptchaView: CaptchaView
    abstract val soundCaptchaView: CaptchaView
    abstract var activeCaptcha: CaptchaView

    init {
        orientation = VERTICAL
    }

    fun loadCaptcha(){
        if(activeCaptcha == imageCaptchaView) loadImageCaptcha()
        else loadVoiceCaptcha()
    }

    fun loadImageCaptcha(){
        soundCaptchaView.visibility = GONE
        soundCaptchaView.reset()
        imageCaptchaView.loadCaptcha()
        imageCaptchaView.visibility = VISIBLE
        activeCaptcha = imageCaptchaView
    }

    fun loadVoiceCaptcha(){
        imageCaptchaView.visibility = GONE
        imageCaptchaView.reset()
        soundCaptchaView.loadCaptcha()
        soundCaptchaView.visibility = VISIBLE
        activeCaptcha = soundCaptchaView
    }

    fun initCaptcha(arcAPI: ArcaptchaAPI, outCallback: CaptchaCallback){
        imageCaptchaView.initCaptcha(arcAPI, outCallback)
        soundCaptchaView.initCaptcha(arcAPI, outCallback)

        imageCaptchaView.setToggleListener {
            loadVoiceCaptcha()
        }

        soundCaptchaView.setToggleListener {
            loadImageCaptcha()
        }
    }
}