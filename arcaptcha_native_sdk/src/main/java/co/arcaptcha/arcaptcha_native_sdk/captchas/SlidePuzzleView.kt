package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.managers.SlidePuzzleManager
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import com.bumptech.glide.Glide
import co.arcaptcha.arcaptcha_native_sdk.components.PuzzleSlider
import co.arcaptcha.arcaptcha_native_sdk.models.InternalCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.CaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.SlideAnswerRequest

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
    private var puzzleSlider: PuzzleSlider = slidePuzzleView.puzzleSlider
    private var finalScaledAnswer = 0
    private var finalDD = 0

    init {
        toggleButton.setImageResource(R.drawable.ic_volume)
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
