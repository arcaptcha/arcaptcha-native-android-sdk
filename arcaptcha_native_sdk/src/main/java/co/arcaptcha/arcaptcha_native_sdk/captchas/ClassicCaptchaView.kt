package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.adapters.CaptchaImageAdapter
import co.arcaptcha.arcaptcha_native_sdk.managers.ClassicCaptchaManager
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.InternalCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.CaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.ClassicAnswerRequest


class ClassicCaptchaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CaptchaView(context, attrs), InternalCaptchaCallback {
    override val manager = ClassicCaptchaManager(this)
    private val captchaAdapter = CaptchaImageAdapter(context)
    private val tbsStack: ArrayDeque<Long> = ArrayDeque()
    val classicCaptchaView = binding.classicCaptcha
    override val captchaBox: LinearLayout = classicCaptchaView.captchaBox
    val gridView = classicCaptchaView.gridView
    val confirmButton = classicCaptchaView.confirmButton

    init {
        toggleButton.setImageResource(R.drawable.ic_volume)
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
        classicCaptchaView.captchaMessage.setText("تصاویر شامل ${cContent.category} را انتخاب کنید")
        cContent.image_urls?.let {
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

    override fun reset() {
        tbsStack.clear()
        tbsStack.addLast(System.currentTimeMillis())
        challengeId = null
    }

    override fun lock() {
        confirmButton.isEnabled = false
        captchaAdapter.isEnabled = false
    }

    override fun unlock() {
        confirmButton.isEnabled = true
        captchaAdapter.isEnabled = true
    }
}
