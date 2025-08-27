package ir.sbpro.arcaptchafast

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import co.arcaptcha.arcaptcha_native_sdk.containers.QuestionContainerView
import co.arcaptcha.arcaptcha_native_sdk.containers.PuzzleContainerView
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import ir.sbpro.arcaptchafast.databinding.ActivityCaptchaBinding
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

class CaptchaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCaptchaBinding
    protected lateinit var mainQuestContainer: QuestionContainerView
    protected lateinit var altQuestContainer: QuestionContainerView
    protected lateinit var puzzleContainer: PuzzleContainerView
    protected lateinit var mainQuestStatus: TextView
    protected lateinit var altQuestStatus: TextView
    protected lateinit var puzzleStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptchaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainQuestContainer = binding.mainQuestionContainer
        mainQuestStatus = binding.mainQuestStatus
        altQuestContainer = binding.altQuestionContainer
        altQuestStatus = binding.altQuestStatus
        puzzleContainer = binding.puzzleContainer
        puzzleStatus = binding.slidePuzzleStatus

        val questArcApi = ArcaptchaAPI("afge5xjsq6", "localhost")
        val puzzleArcApi = ArcaptchaAPI("bq44zwr6cn", "localhost")

        mainQuestContainer.initCaptcha(questArcApi, object : CaptchaCallback {
            override fun onCorrectAnswer(token: String) {
                showStatus(mainQuestStatus, "پازل حل شد!")
            }

            override fun onError(message: String) {
                finishStatus(mainQuestContainer, mainQuestStatus, message)
            }

            override fun onWrongAnswer() {
                mainQuestContainer.loadCaptcha()
            }

            override fun onStateChanged(state: CaptchaState) {
                if(state == CaptchaState.LoadingCaptcha) hideStatus(mainQuestStatus)
            }
        })

        altQuestContainer.initCaptcha(questArcApi, object : CaptchaCallback {
            override fun onCorrectAnswer(token: String) {
                showStatus(altQuestStatus, "پازل حل شد!")
            }

            override fun onError(message: String) {
                finishStatus(altQuestContainer, altQuestStatus, message)
            }

            override fun onWrongAnswer() {
                altQuestContainer.loadCaptcha()
            }

            override fun onStateChanged(state: CaptchaState) {
                if(state == CaptchaState.LoadingCaptcha) hideStatus(altQuestStatus)
            }
        })

        puzzleContainer.initCaptcha(puzzleArcApi, object : CaptchaCallback {
            override fun onCorrectAnswer(token: String) {
                showStatus(puzzleStatus, "پازل حل شد!")
            }

            override fun onError(message: String) {
                finishStatus(puzzleContainer, puzzleStatus, message)
            }

            override fun onWrongAnswer() {
                puzzleContainer.loadCaptcha()
            }

            override fun onStateChanged(state: CaptchaState) {
                if(state == CaptchaState.LoadingCaptcha) hideStatus(puzzleStatus)
            }
        })

        mainQuestStatus.setOnClickListener {
            mainQuestContainer.visibility = View.VISIBLE
            mainQuestContainer.loadCaptcha()
        }

        altQuestStatus.setOnClickListener {
            altQuestContainer.visibility = View.VISIBLE
            altQuestContainer.loadCaptcha()
        }

        puzzleStatus.setOnClickListener {
            puzzleContainer.visibility = View.VISIBLE
            puzzleContainer.loadCaptcha()
        }

        mainQuestContainer.loadImageCaptcha()
        altQuestContainer.loadVoiceCaptcha()
        puzzleContainer.loadCaptcha()
    }

    fun finishStatus(container: View, tv: TextView, message: String){
        container.visibility = View.GONE
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