package ir.sbpro.arcaptchafast

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Visibility
import co.arcaptcha.arcaptcha_native_sdk.captchas.CaptchaView
import co.arcaptcha.arcaptcha_native_sdk.captchas.ClassicCaptchaView
import co.arcaptcha.arcaptcha_native_sdk.captchas.SlidePuzzleView
import co.arcaptcha.arcaptcha_native_sdk.captchas.SoundCaptchaView
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import ir.sbpro.arcaptchafast.databinding.ActivityCaptchaBinding
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

class CaptchaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaptchaBinding
    protected lateinit var classicCaptcha: ClassicCaptchaView
    protected lateinit var soundCaptcha: SoundCaptchaView
    protected lateinit var slideCaptcha: SlidePuzzleView
    protected lateinit var classicCaptchaStatus: TextView
    protected lateinit var voiceChallengeStatus: TextView
    protected lateinit var slidePuzzleStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptchaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        classicCaptcha = binding.classicCaptchaView
        classicCaptchaStatus = binding.classicCaptchaStatus
        soundCaptcha = binding.soundCaptchaView
        voiceChallengeStatus = binding.voiceChallengeStatus
        slideCaptcha = binding.slideCaptchaView
        slidePuzzleStatus = binding.slidePuzzleStatus

        val questArcApi = ArcaptchaAPI("afge5xjsq6", "localhost")
        val puzzleArcApi = ArcaptchaAPI("bq44zwr6cn", "localhost")

        classicCaptcha.initCaptcha(questArcApi, object : CaptchaCallback {
            override fun onCorrectAnswer() {
                showStatus(classicCaptchaStatus, "پازل حل شد!")
            }

            override fun onError(message: String) {
                finishStatus(classicCaptcha, classicCaptchaStatus, message)
            }

            override fun onWrongAnswer() {
                classicCaptcha.loadCaptcha()
            }

            override fun onStateChanged(state: CaptchaState) {
                if(state == CaptchaState.LoadingCaptcha) hideStatus(classicCaptchaStatus)
            }
        })

        soundCaptcha.initCaptcha(questArcApi, object : CaptchaCallback {
            override fun onCorrectAnswer() {
                showStatus(voiceChallengeStatus, "پازل حل شد!")
            }

            override fun onError(message: String) {
                finishStatus(soundCaptcha, voiceChallengeStatus, message)
            }

            override fun onWrongAnswer() {
                soundCaptcha.loadCaptcha()
            }

            override fun onStateChanged(state: CaptchaState) {
                if(state == CaptchaState.LoadingCaptcha) hideStatus(voiceChallengeStatus)
            }
        })

        slideCaptcha.initCaptcha(puzzleArcApi, object : CaptchaCallback {
            override fun onCorrectAnswer() {
                showStatus(slidePuzzleStatus, "پازل حل شد!")
            }

            override fun onError(message: String) {
                finishStatus(slideCaptcha, slidePuzzleStatus, message)
            }

            override fun onWrongAnswer() {
                slideCaptcha.loadCaptcha()
            }

            override fun onStateChanged(state: CaptchaState) {
                if(state == CaptchaState.LoadingCaptcha) hideStatus(slidePuzzleStatus)
            }
        })

        classicCaptcha.loadCaptcha()
        soundCaptcha.loadCaptcha()
        slideCaptcha.loadCaptcha()
    }

    fun finishStatus(captcha: CaptchaView, tv: TextView, message: String){
        captcha.visibility = View.GONE
        tv.setText(message)
        tv.visibility = View.VISIBLE
    }

    fun showStatus(tv: TextView, message: String){
        tv.setText(message)
        tv.visibility = View.VISIBLE
    }

    fun hideStatus(tv: TextView){
        tv.visibility = View.GONE
    }
}