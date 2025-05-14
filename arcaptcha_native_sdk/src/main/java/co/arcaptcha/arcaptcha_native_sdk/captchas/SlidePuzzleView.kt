package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.managers.SlidePuzzleManager
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.SlidePuzzleCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.SlidePuzzleData
import com.bumptech.glide.Glide
import android.view.MotionEvent

class SlidePuzzleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CaptchaView(context, attrs), SlidePuzzleCallback {
    override val manager = SlidePuzzleManager(this)
    val slidePuzzleView = binding.slideCaptcha
    override val captchaBox: LinearLayout = slidePuzzleView.captchaBox
    private var puzzleBgImage: ImageView = slidePuzzleView.puzzleBgImage
    private var puzzlePieceImage: ImageView = slidePuzzleView.puzzlePieceImage
    private var sliderContainer: RelativeLayout = slidePuzzleView.sliderContainer
    private var sliderThumb: ImageView = slidePuzzleView.sliderThumb

    init {
        toggleButton.setImageResource(R.drawable.ic_image)
        orientation = VERTICAL

        sliderThumb.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // این خط خیلی مهمه!
                    sliderContainer.parent.requestDisallowInterceptTouchEvent(true)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val location = IntArray(2)
                    sliderContainer.getLocationOnScreen(location)
                    val containerX = location[0]

                    val containerWidth = sliderContainer.width
                    val maxX = containerWidth - sliderThumb.width

                    val newX = (event.rawX - containerX - sliderThumb.width / 2)
                        .coerceIn(0f, maxX.toFloat())

                    sliderThumb.translationX = newX
                    puzzlePieceImage.translationX = newX

                    val value = (newX / maxX * 260).toInt()
                    Log.d("XQQQSSliderValue", "Current Value: $value")
                    true
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // پایان تعامل
                    sliderContainer.parent.requestDisallowInterceptTouchEvent(false)
                    v.performClick()
                    true
                }

                else -> false
            }
        }
    }

    override fun onCaptchaLoaded(data: SlidePuzzleData) {
        Log.d("XQQQStateSlide", "onCaptchaLoaded: ${data.captcha_type}, ${data.status}")
        if(data.content?.puzzle_background != null && data.content.puzzle_piece != null){
            val puzzleBgUrl = arcaptchaApi.getOriginalImageUrl(data.content.puzzle_background)
            val puzzlePieceUrl = arcaptchaApi.getOriginalImageUrl(data.content.puzzle_piece)

            Glide.with(context)
                .load(puzzleBgUrl)
                .into(puzzleBgImage)

            Glide.with(context)
                .load(puzzlePieceUrl)
                .into(puzzlePieceImage)

            Log.d("XQQQStateSlideBGUrl", puzzleBgUrl)
            Log.d("XQQQStateSlidePieceUrl", puzzlePieceUrl)
        }
        outerCallback?.onCaptchaLoaded()
    }

    override fun onStateChanged(state: CaptchaState) {
        when (state) {
            CaptchaState.LoadingCaptcha, CaptchaState.SubmittingSolution -> loadingMode()
            CaptchaState.AwaitingUserInput -> contentMode()
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
