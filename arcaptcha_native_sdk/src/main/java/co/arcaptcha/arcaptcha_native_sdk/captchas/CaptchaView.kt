package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import co.arcaptcha.arcaptcha_native_sdk.databinding.CaptchaViewBinding
import co.arcaptcha.arcaptcha_native_sdk.managers.CaptchaManager
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.BaseCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel


abstract class CaptchaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs), BaseCaptchaCallback {
    protected val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    protected val binding: CaptchaViewBinding =
        CaptchaViewBinding.inflate(LayoutInflater.from(context), this, true)

    abstract protected val manager: CaptchaManager
    protected abstract val captchaBox: LinearLayout
    protected lateinit var arcaptchaApi: ArcaptchaAPI
    protected var challengeId: String? = null
    protected var outerCallback: CaptchaCallback? = null
    protected val loadingContainer: LinearLayout
    protected val footerBox: RelativeLayout
    protected val refreshButton: ImageButton
    protected val toggleButton: ImageButton
    protected val infoButton: ImageButton

    init {
        orientation = VERTICAL
        loadingContainer = binding.loadingContainer
        footerBox = binding.footer
        refreshButton = binding.refreshButton
        toggleButton = binding.toggleButton
        infoButton = binding.infoButton

        refreshButton.setOnClickListener({
            loadCaptcha()
        })
    }

    fun loadCaptcha(){
        challengeId = null
        manager.loadCaptcha(arcaptchaApi)
    }

    protected fun submitAnswer(){
        if(challengeId == null) return
        manager.submitAnswer(createSubmitRequest())
    }

    abstract fun createSubmitRequest() : BaseAnswerRequest

    fun initCaptcha(arcAPI: ArcaptchaAPI, outCallback: CaptchaCallback){
        this.arcaptchaApi = arcAPI
        this.outerCallback = outCallback
    }

    fun loadingMode(){
        loadingContainer.visibility = VISIBLE
        captchaBox.visibility = GONE
        footerBox.visibility = GONE
    }

    fun contentMode(){
        loadingContainer.visibility = GONE
        captchaBox.visibility = VISIBLE
        footerBox.visibility = VISIBLE
    }

    fun disableMode(){

    }

    fun setToggleListener(listener: View.OnClickListener){
        toggleButton.setOnClickListener(listener)
    }

    open fun destroy() {
        coroutineScope.cancel()
    }
}
