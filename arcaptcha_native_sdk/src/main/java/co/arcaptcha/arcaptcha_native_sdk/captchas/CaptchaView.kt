package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
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


abstract class CaptchaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    protected val binding: CaptchaViewBinding =
        CaptchaViewBinding.inflate(LayoutInflater.from(context), this, true)

    protected abstract val captchaBox: LinearLayout
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

    fun setRefreshListener(listener: View.OnClickListener){
        refreshButton.setOnClickListener(listener)
    }

    fun setToggleListener(listener: View.OnClickListener){
        toggleButton.setOnClickListener(listener)
    }
}
