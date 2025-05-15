package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.managers.SlidePuzzleManager
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.SlidePuzzleData
import com.bumptech.glide.Glide
import co.arcaptcha.arcaptcha_native_sdk.components.PuzzleSlider
import co.arcaptcha.arcaptcha_native_sdk.models.InternalCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.CaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.VoiceChallengeData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseAnswerRequest

@SuppressLint("ClickableViewAccessibility")
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
    private var puzzleWidth = 0
    private var puzzleHeight = 0

    init {
        toggleButton.setImageResource(R.drawable.ic_image)
        orientation = VERTICAL

        puzzleSlider.sliderMaxValue = puzzleOriginalWidth.toFloat()
        puzzleSlider.setOnInputListener {realValue, scaledValue ->
            puzzlePieceImage.translationX = realValue
        }

        puzzleSlider.setOnChangeListener {realValue, scaledValue, dd, ddt ->
            Log.d("XQQQSSUserAnsReady", "($scaledValue, $dd)")
        }
    }

    override fun createSubmitRequest(): BaseAnswerRequest {
        TODO("Not yet implemented")
    }

    override fun onCaptchaLoaded(data: CaptchaData) {
        Log.d("XQQQStateSlide", "onCaptchaLoaded: ${data.captcha_type}, ${data.status}")
        val cContent = data.content
        challengeId = cContent!!.challenge_id!!

        if(cContent.puzzle_background != null && data.content.puzzle_piece != null){
            val puzzleBgUrl = arcaptchaApi.getOriginalImageUrl(cContent.puzzle_background)
            val puzzlePieceUrl = arcaptchaApi.getOriginalImageUrl(data.content.puzzle_piece)
            val puzzlePieceY = data.content.y ?: 0

            //hatman bad az visible shodane aks ha anjam shavad
            puzzleBgImage.post {
                val location = IntArray(2)
                puzzleBgImage.getLocationOnScreen(location)
                puzzleWidth = puzzleBgImage.width
                puzzleHeight = puzzleBgImage.height
                puzzlePieceImage.translationY = ((puzzleHeight.toFloat() / puzzleOriginalHeight) * puzzlePieceY)
            }

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
            CaptchaState.LoadingCaptcha, CaptchaState.SubmittingSolution -> {
                loadingMode()
                puzzleSlider.resetSlider()
            }
            CaptchaState.AwaitingUserInput -> contentMode()
            CaptchaState.WrongAnswer -> {}
            CaptchaState.Done -> {}
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
}
