package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.managers.VoiceChallengeManager
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.InternalCaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.CaptchaData
import co.arcaptcha.arcaptcha_native_sdk.models.requests.BaseAnswerRequest
import co.arcaptcha.arcaptcha_native_sdk.models.requests.VoiceAnswerRequest

class SoundCaptchaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CaptchaView(context, attrs), InternalCaptchaCallback {
    override val manager = VoiceChallengeManager(this)
    val soundCaptchaView = binding.soundCaptcha
    override val captchaBox: LinearLayout = soundCaptchaView.captchaBox

    private lateinit var mediaPlayer: MediaPlayer
    val confirmButton = soundCaptchaView.confirmButton
    private var captchaEditText: EditText = soundCaptchaView.captchaEditText
    private var playButton: ImageButton = soundCaptchaView.playButton
    private var isSoundPlaying = false
    private var audioUrl = ""

    init {
        toggleButton.setImageResource(R.drawable.ic_image)

        playButton.setOnClickListener {
            if (!isSoundPlaying && audioUrl.isNotEmpty()) playAudio()
        }

        orientation = VERTICAL

        confirmButton.setOnClickListener {
            if(captchaEditText.text.isEmpty()) return@setOnClickListener
            captchaEditText.clearFocus()
            submitAnswer()
        }
    }

    private fun playAudio() {
        if(audioUrl.isEmpty()) return;

        isSoundPlaying = true
        playButton.setImageResource(R.drawable.ic_pause)

        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioUrl)
            prepareAsync()
            setOnPreparedListener {
                start()
                isSoundPlaying = true
                playButton.setImageResource(R.drawable.ic_pause)
            }
            setOnCompletionListener {
                isSoundPlaying = false
                playButton.setImageResource(R.drawable.ic_play)
                release()
            }
        }
    }

    override fun destroy() {
        super.destroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }

    override fun createSubmitRequest(): BaseAnswerRequest {
        return VoiceAnswerRequest(arcaptchaApi, challengeId!!, captchaEditText.text.toString())
    }

    override fun onCaptchaLoaded(data: CaptchaData) {
        Log.d("XQQQStateVC", "onCaptchaLoaded: ${data.captcha_type}, ${data.status}")
        reset()
        val cContent = data.content
        challengeId = cContent!!.challenge_id!!
        cContent.path?.let {
            audioUrl = arcaptchaApi.getVoiceUrl(it)
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
        captchaEditText.setText("")
        challengeId = null
    }

    override fun lock() {
        confirmButton.isEnabled = false
        captchaEditText.isEnabled = false
    }

    override fun unlock() {
        confirmButton.isEnabled = true
        captchaEditText.isEnabled = true
    }
}
