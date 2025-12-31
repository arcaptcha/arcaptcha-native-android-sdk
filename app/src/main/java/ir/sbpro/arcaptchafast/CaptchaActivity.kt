package ir.sbpro.arcaptchafast

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import co.arcaptcha.arcaptcha_native_sdk.containers.QuestionContainerView
import co.arcaptcha.arcaptcha_native_sdk.containers.PuzzleContainerView
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaCallback
import co.arcaptcha.arcaptcha_native_sdk.models.CaptchaState
import ir.sbpro.arcaptchafast.databinding.ActivityCaptchaBinding
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI
import androidx.core.content.edit
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        binding = ActivityCaptchaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainQuestContainer = binding.mainQuestionContainer
        mainQuestStatus = binding.mainQuestStatus
        altQuestContainer = binding.altQuestionContainer
        altQuestStatus = binding.altQuestStatus
        puzzleContainer = binding.puzzleContainer
        puzzleStatus = binding.slidePuzzleStatus

        val fab: FloatingActionButton = binding.fabTheme

        fab.setOnClickListener {
            toggleTheme()
        }

        val questArcApi = ArcaptchaAPI("afge5xjsq6", "localhost")
        val puzzleArcApi = ArcaptchaAPI("bq44zwr6cn", "localhost")

        mainQuestContainer.initCaptcha(questArcApi, object : CaptchaCallback {
            override fun onCorrectAnswer(token: String) {
                showStatus(mainQuestStatus, "Token: $token")
            }

            override fun onError(errorCode: Int, message: String) {
                finishStatus(mainQuestContainer, mainQuestStatus, "$message ($errorCode)")
            }

            override fun onStateChanged(state: CaptchaState) {
                if(state == CaptchaState.LoadingCaptcha) hideStatus(mainQuestStatus)
            }
        })

        altQuestContainer.initCaptcha(questArcApi, object : CaptchaCallback {
            override fun onCorrectAnswer(token: String) {
                showStatus(altQuestStatus, "Token: $token")
            }

            override fun onError(errorCode: Int, message: String) {
                finishStatus(altQuestContainer, altQuestStatus, "$message ($errorCode)")
            }

            override fun onStateChanged(state: CaptchaState) {
                if(state == CaptchaState.LoadingCaptcha) hideStatus(altQuestStatus)
            }
        })

        puzzleContainer.initCaptcha(puzzleArcApi, object : CaptchaCallback {
            override fun onCorrectAnswer(token: String) {
                showStatus(puzzleStatus, "Token: $token")
            }

            override fun onError(errorCode: Int, message: String) {
                finishStatus(puzzleContainer, puzzleStatus, "$message ($errorCode)")
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

    private fun toggleTheme() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", false)

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        prefs.edit { putBoolean("dark_mode", !isDark) }
    }
}