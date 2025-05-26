package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.databinding.CaptchaViewBinding
import co.arcaptcha.arcaptcha_native_sdk.managers.CaptchaManager
import co.arcaptcha.arcaptcha_native_sdk.models.BaseCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI
import co.arcaptcha.arcaptcha_native_sdk.themes.AppFont
import co.arcaptcha.arcaptcha_native_sdk.themes.FontManager
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
    protected var infoText: String = ""
    protected abstract val captchaBox: LinearLayout
    protected lateinit var arcaptchaApi: ArcaptchaAPI
    protected var challengeId: String? = null
    protected var outerCallback: CaptchaCallback? = null
    protected val loadingContainer: LinearLayout
    protected val footerBox: RelativeLayout
    protected val txvArcLicense: TextView
    protected val refreshButton: ImageButton
    protected val toggleButton: ImageButton
    protected val infoButton: ImageButton

    init {
        orientation = VERTICAL
        loadingContainer = binding.loadingContainer
        footerBox = binding.footer
        txvArcLicense = binding.txvArcLicense
        refreshButton = binding.refreshButton
        toggleButton = binding.toggleButton
        infoButton = binding.infoButton

        refreshButton.setOnClickListener({
            loadCaptcha()
        })

        txvArcLicense.setTypeface(getDefaultTypeface())

        infoButton.setOnClickListener {
            val inflater = LayoutInflater.from(context)
            val popupView = inflater.inflate(R.layout.info_popup, null)
            val infoTextView = popupView.findViewById<TextView>(R.id.infoTextView)

            val popupWindow = PopupWindow(
                popupView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                true //baraye inke ba clicke birun baste beshe
            )

            infoTextView.setText(infoText)
            popupWindow.animationStyle = android.R.style.Animation_Dialog //optional
            popupWindow.showAsDropDown(infoButton, -260, -120)
        }
    }

    private fun getDefaultTypeface(): Typeface {
        return FontManager.getTypeface(context, AppFont.YEKAN)
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
    abstract fun lock()
    abstract fun unlock()
    abstract fun reset()
    abstract fun applyDefaultFont(typeface: Typeface)

    fun initCaptcha(arcAPI: ArcaptchaAPI, outCallback: CaptchaCallback){
        this.arcaptchaApi = arcAPI
        this.outerCallback = outCallback
        applyDefaultFont(getDefaultTypeface())
    }

    fun showContent(){
        loadingContainer.visibility = GONE
        captchaBox.visibility = VISIBLE
        footerBox.visibility = VISIBLE
    }

    fun loadingMode(){
        loadingContainer.visibility = VISIBLE
        captchaBox.visibility = GONE
        footerBox.visibility = GONE
    }

    fun contentMode(){
        showContent()
        isEnabled = true
        refreshButton.isEnabled = true
        infoButton.isEnabled = true
        toggleButton.isEnabled = true
        unlock()
    }

    fun disableMode(){
        showContent()
        isEnabled = false
        refreshButton.isEnabled = false
        infoButton.isEnabled = false
        toggleButton.isEnabled = false
        lock()
    }

    fun setToggleListener(listener: OnClickListener){
        toggleButton.setOnClickListener(listener)
    }

    open fun destroy() {
        coroutineScope.cancel()
    }
}
