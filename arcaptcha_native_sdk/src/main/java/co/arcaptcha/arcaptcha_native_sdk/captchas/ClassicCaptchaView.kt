package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.adapters.CaptchaImageAdapter
import co.arcaptcha.arcaptcha_native_sdk.managers.ClassicCaptchaManager
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.InternalCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.CaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.ClassicAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.utils.ScreenUtils


class ClassicCaptchaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CaptchaView(context, attrs), InternalCaptchaCallback {
    override val manager = ClassicCaptchaManager(this)
    private val captchaAdapter = CaptchaImageAdapter(context)
    private val tbsStack: ArrayDeque<Long> = ArrayDeque()
    val classicCaptchaView = binding.classicCaptcha
    val captchaMessage = classicCaptchaView.captchaMessage
    override val captchaBox: LinearLayout = classicCaptchaView.captchaBox
    val gridView = classicCaptchaView.gridView
    val confirmButton = classicCaptchaView.confirmButton

    init {
        toggleButton.setImageResource(R.drawable.ic_volume)
        toggleButton.contentDescription = "کپچای صوتی"

        infoText = context.getString(R.string.info_classic_captcha)
        orientation = VERTICAL

        gridView.adapter = captchaAdapter

        confirmButton.setOnClickListener {
            submitAnswer()
        }

        captchaAdapter.setCallbacks (onRemove = {
            tbsStack.removeLastOrNull()
        }) {
            tbsStack.addLast(System.currentTimeMillis())
        }
    }

    override fun createSubmitRequest(): ClassicAnswerRequest {
        val selectedIndices = captchaAdapter.getSelectedIndices()
        val tilesList = ArrayList<Boolean>()
        for(i in 0 until 9){
            if(selectedIndices.contains(i)) tilesList.add(true)
            else tilesList.add(false)
        }

        val tbs = ArrayList<Int>()
        var popped = tbsStack.removeFirstOrNull()
        while (popped != null){
            val next = tbsStack.removeFirstOrNull()
            if(next == null) break
            tbs.add((next - popped).toInt())
            popped = next
        }

        return ClassicAnswerRequest.getInstance(arcaptchaApi, challengeId!!, tilesList, tbs)
    }

    override fun onCaptchaLoaded(data: CaptchaData) {
        Log.d("XQQQStateClassic", "onCaptchaLoaded: ${data.captcha_type}, ${data.status}")
        reset()

        val cContent = data.content!!
        challengeId = cContent.challenge_id!!

        var title = "تصاویر شامل "
        val cateStart = title.length
        val cateEnd = cateStart + (cContent.category?.length ?: 0)
        title = title + cContent.category
        title = title + " را انتخاب کنید."
        val spannableTitle = SpannableString(title)

        val cateColor = ContextCompat.getColor(context, R.color.main_button_color)
        spannableTitle.setSpan(ForegroundColorSpan(cateColor), cateStart, cateEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        captchaMessage.setText(spannableTitle)

        cContent.image_urls?.let {
            val finalImageUrls = it.map { item ->
                arcaptchaApi.getImageUrl(item)
            }

            val targetGridWidth = containerAvailableWidth - ScreenUtils.dpToPx(context, 18)
            captchaAdapter.gridWidth = targetGridWidth
            captchaAdapter.setImages(finalImageUrls)

            val height = captchaAdapter.calculateGridHeight()
            gridView.layoutParams.height = height
            gridView.requestLayout()
            outerCallback?.onCaptchaLoaded()
        }
    }

    override fun onStateChanged(state: CaptchaState) {
        when (state) {
            CaptchaState.LoadingCaptcha, CaptchaState.SubmittingSolution -> loadingMode()
            CaptchaState.AwaitingUserInput -> contentMode()
            CaptchaState.Done -> disableMode()
            CaptchaState.WrongAnswer -> {}
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

    override fun applyDefaultFont(typeface: Typeface) {
        captchaMessage.setTypeface(typeface)
        confirmButton.setTypeface(typeface)
    }

    override fun reset() {
        tbsStack.clear()
        tbsStack.addLast(System.currentTimeMillis())
        challengeId = null
    }

    override fun lock() {
        confirmButton.isEnabled = false
        captchaAdapter.isEnabled = false
        confirmButton.icon.alpha = (0.5f * 255).toInt()
    }

    override fun unlock() {
        confirmButton.isEnabled = true
        captchaAdapter.isEnabled = true
        confirmButton.icon.alpha = 255
    }
}
