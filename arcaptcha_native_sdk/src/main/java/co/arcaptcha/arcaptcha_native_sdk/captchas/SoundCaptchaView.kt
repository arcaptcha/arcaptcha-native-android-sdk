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
import co.arcaptcha.arcaptcha_native_sdk.managers.VoiceChallengeManager
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import co.arcaptcha.arcaptcha_native_sdk.models.VoiceChallengeCallback
import co.arcaptcha.arcaptcha_native_sdk.models.captchas.VoiceChallengeData

class SoundCaptchaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CaptchaView(context, attrs), VoiceChallengeCallback {
    override val manager = VoiceChallengeManager(this)
    override val captchaBox: LinearLayout = binding.soundCaptcha.captchaBox
    private lateinit var mediaPlayer: MediaPlayer
    private var captchaEditText: EditText = binding.soundCaptcha.captchaEditText
    private var playButton: ImageButton = binding.soundCaptcha.playButton
    private var isSoundPlaying = false
    private var audioUrl = "https://download.samplelib.com/mp3/sample-3s.mp3"

    init {
        toggleButton.setImageResource(R.drawable.ic_image)

        playButton.setOnClickListener {
            if (!isSoundPlaying && audioUrl.isNotEmpty()) playAudio()
        }

        orientation = VERTICAL

        binding.soundCaptcha.confirmButton.setOnClickListener {
            Toast.makeText(context, "متن وارد شده: ${captchaEditText.text}",
                Toast.LENGTH_SHORT).show()
            captchaEditText.setText("")
            captchaEditText.clearFocus()
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

    override fun onCaptchaLoaded(data: VoiceChallengeData) {
        Log.d("XQQQStateVC", "onCaptchaLoaded: ${data.captcha_type}, ${data.status}")
        data.content?.path?.let {
            audioUrl = arcaptchaApi.getVoiceUrl(it)
        }
        outerCallback?.onCaptchaLoaded()
    }

    override fun onStateChanged(state: CaptchaState) {
        when (state) {
            CaptchaState.LoadingCaptcha, CaptchaState.SubmittingSolution -> loadingMode()
            CaptchaState.AwaitingUserInput -> contentMode()
            CaptchaState.Done -> TODO()
            CaptchaState.Error -> TODO()
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
