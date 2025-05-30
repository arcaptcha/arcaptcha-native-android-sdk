package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.components.PuzzleSlider
import co.arcaptcha.arcaptcha_native_sdk.managers.SlidePuzzleManager
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.InternalCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.CaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.SlideAnswerRequest
import com.bumptech.glide.Glide

class SlidePuzzleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CaptchaView(context, attrs), InternalCaptchaCallback {
    companion object {
        const val puzzleOriginalWidth = 260
        const val puzzleOriginalHeight = 160
    }

    override val manager = SlidePuzzleManager(this)
    val slidePuzzleView = binding.slideCaptcha
    override val captchaBox: LinearLayout = slidePuzzleView.captchaBox
    private var puzzleBgImage: ImageView = slidePuzzleView.puzzleBgImage
    private var puzzlePieceImage: ImageView = slidePuzzleView.puzzlePieceImage
    private var slideMessage: TextView = slidePuzzleView.slideMessage
    private var puzzleSlider: PuzzleSlider = slidePuzzleView.puzzleSlider
    private var finalScaledAnswer = 0
    private var finalDD = 0

    init {
        toggleButton.setImageResource(R.drawable.ic_volume)
        infoText = context.getString(R.string.info_slide_puzzle)
        orientation = VERTICAL

        puzzleSlider.setMaxValue(puzzleOriginalWidth.toFloat())
        puzzleSlider.setOnInputListener {realValue, scaledValue ->
            puzzlePieceImage.translationX = realValue
        }

        puzzleSlider.setOnChangeListener {realValue, scaledValue, dd, ddt ->
            finalScaledAnswer = scaledValue
            finalDD = dd
            submitAnswer()
        }
    }

    override fun createSubmitRequest(): BaseAnswerRequest {
        Log.d("XQQQSSScaledValue", "$finalScaledAnswer")
        return SlideAnswerRequest(arcaptchaApi, challengeId!!, finalScaledAnswer, finalDD, finalDD)
    }

    override fun onCaptchaLoaded(data: CaptchaData) {
        Log.d("XQQQStateSlide", "onCaptchaLoaded: ${data.captcha_type}, ${data.status}")
        reset()
        val cContent = data.content
        challengeId = cContent!!.challenge_id!!

        if(cContent.puzzle_background != null && data.content.puzzle_piece != null){
            val puzzleBgUrl = arcaptchaApi.getOriginalImageUrl(cContent.puzzle_background)
            val puzzlePieceUrl = arcaptchaApi.getOriginalImageUrl(data.content.puzzle_piece)
            val puzzlePieceY = data.content.y ?: 0

            val updatePieceLocation: (Int) -> Unit = {
                //hatman bad az visible shodane aks ha anjam shavad
                puzzleBgImage.post {
                    puzzlePieceImage.translationY = ((it.toFloat() / puzzleOriginalHeight) * puzzlePieceY)
                }
            }

            puzzleBgImage.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val puzzleHeight = puzzleBgImage.height
                    if(puzzleHeight > 0){
                        updatePieceLocation(puzzleHeight)
                        puzzleBgImage.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            })

            Glide.with(context)
                .load(puzzleBgUrl)
                .into(puzzleBgImage)

            Glide.with(context)
                .load(puzzlePieceUrl)
                .into(puzzlePieceImage)
        }
        outerCallback?.onCaptchaLoaded()
    }

    fun animateMessage(isCorrect: Boolean, finishCallback: () -> Unit){
        if(isCorrect){
            slideMessage.setBackgroundColor("#06966a".toColorInt())
            slideMessage.setTextColor("#b7e6e4".toColorInt())
            slideMessage.setText("چالش با موفقیت حل شد")
        }
        else {
            slideMessage.setBackgroundColor("#ed4542".toColorInt())
            slideMessage.setTextColor("#432721".toColorInt())
            slideMessage.setText("لطفا پازل را در جایش قرار دهید")
        }

        disableMode()
        slideMessage.alpha = 0f
        slideMessage.translationY = 100f
        slideMessage.visibility = VISIBLE

        slideMessage.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                slideMessage.postDelayed({
                    slideMessage.animate()
                        .translationY(100f)
                        .alpha(0f)
                        .setDuration(300)
                        .setInterpolator(AccelerateInterpolator())
                        .withEndAction {
                            slideMessage.visibility = GONE
                            finishCallback()
                        }.start()
                }, 800)
            }.start()
    }

    override fun onStateChanged(state: CaptchaState) {
        when (state) {
            CaptchaState.LoadingCaptcha, CaptchaState.SubmittingSolution -> loadingMode()
            CaptchaState.AwaitingUserInput -> contentMode()
            CaptchaState.WrongAnswer -> {}
            CaptchaState.Done -> disableMode()
            CaptchaState.Error -> {}
        }
        outerCallback?.onStateChanged(state)
    }

    override fun onCorrectAnswer() {
        animateMessage(true, {
            outerCallback?.onCorrectAnswer()
        })
    }

    override fun onWrongAnswer() {
        animateMessage(false, {
            outerCallback?.onWrongAnswer()
        })
    }

    override fun onError(message: String) {
        Log.d("XQQQStateError", message)
        outerCallback?.onError(message)
    }

    override fun applyDefaultFont(typeface: Typeface) {
        puzzleSlider.setTypeface(typeface)
        slideMessage.setTypeface(typeface)
    }

    override fun reset() {
        finalScaledAnswer = 0
        finalDD = 0
        puzzlePieceImage.translationY = 0f
        puzzlePieceImage.translationX = 0f
        puzzleSlider.resetSlider()
        challengeId = null
    }

    override fun lock() {
        puzzleSlider.isEnabled = false
    }

    override fun unlock() {
        puzzleSlider.isEnabled = true
    }
}
