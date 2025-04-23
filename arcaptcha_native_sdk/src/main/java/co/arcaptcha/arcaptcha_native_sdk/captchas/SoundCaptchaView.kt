package co.arcaptcha.arcaptcha_native_sdk.captchas

import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import co.arcaptcha.arcaptcha_native_sdk.R

class SoundCaptchaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CaptchaView(context, attrs) {
    override val captchaBox: LinearLayout = binding.soundCaptcha.captchaBox
    private lateinit var mediaPlayer: MediaPlayer
    private var playButton: ImageButton = binding.soundCaptcha.playButton
    private var isSoundPlaying = false
    private val audioUrl = "https://download.samplelib.com/mp3/sample-3s.mp3"

    init {
        toggleButton.setImageResource(R.drawable.ic_image)

        playButton.setOnClickListener {
            if (!isSoundPlaying) playAudio()
        }

        orientation = VERTICAL

        binding.soundCaptcha.confirmButton.setOnClickListener {
            Toast.makeText(context, "متن وارد شده: ${"dd"}", Toast.LENGTH_SHORT).show()
        }

        contentMode()
    }

    private fun playAudio() {
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

    fun destroy() {
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
