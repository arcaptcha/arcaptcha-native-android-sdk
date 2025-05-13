package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.managers.SlidePuzzleManager
import co.arcaptcha.arcaptcha_native_sdk.managers.VoiceChallengeManager
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.SlidePuzzleCallback
import co.arcaptcha.arcaptcha_native_sdk.models.VoiceChallengeCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.SlidePuzzleData
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.VoiceChallengeData

class SlidePuzzleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CaptchaView(context, attrs), SlidePuzzleCallback {
    override val manager = SlidePuzzleManager(this)
    val slidePuzzleView = binding.slideCaptcha
    override val captchaBox: LinearLayout = slidePuzzleView.captchaBox

    init {
        toggleButton.setImageResource(R.drawable.ic_image)
        orientation = VERTICAL

        binding.soundCaptcha.confirmButton.setOnClickListener {

        }
    }

    override fun onCaptchaLoaded(data: SlidePuzzleData) {
        Log.d("XQQQStateSlide", "onCaptchaLoaded: ${data.captcha_type}, ${data.status}")
        if(data.content?.puzzle_background != null && data.content.puzzle_piece != null){
            val puzzleBgUrl = arcaptchaApi.getOriginalImageUrl(data.content.puzzle_background)
            val puzzlePieceUrl = arcaptchaApi.getOriginalImageUrl(data.content.puzzle_piece)

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
