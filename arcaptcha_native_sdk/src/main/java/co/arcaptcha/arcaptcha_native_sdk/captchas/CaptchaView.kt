package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import co.arcaptcha.arcaptcha_native_sdk.adapters.CaptchaImageAdapter
import co.arcaptcha.arcaptcha_native_sdk.databinding.CaptchaViewBinding
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.github.ybq.android.spinkit.style.ThreeBounce


open class CaptchaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    protected val binding: CaptchaViewBinding =
        CaptchaViewBinding.inflate(LayoutInflater.from(context), this, true)

    protected val loadingContainer: LinearLayout
    protected val captchaBox: LinearLayout
    protected val footerBox: RelativeLayout

    init {
        orientation = VERTICAL
        loadingContainer = binding.loadingContainer
        captchaBox = binding.classicCaptcha.captchaBox
        footerBox = binding.footer

        loadingMode()
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
}
