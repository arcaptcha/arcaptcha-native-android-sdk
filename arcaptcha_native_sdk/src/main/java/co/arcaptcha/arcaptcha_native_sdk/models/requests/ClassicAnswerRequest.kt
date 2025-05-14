package co.arcaptcha.arcaptcha_native_sdk.models.requests

import java.security.MessageDigest
import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

class ClassicAnswerRequest(
    arcApi: ArcaptchaAPI,
    challenge_id: String,
    val  fp: String,
    val answers: ClassicAnswersValue,
    val  time_between_selections: List<Int>,
) : BaseAnswerRequest(arcApi, challenge_id){
    data class ClassicAnswersValue (
        val a: String,
        val m: List<Boolean>
    )

    companion object {
        fun getInstance(arcApi: ArcaptchaAPI, challengeId: String,
                        imagesStatus: List<Boolean>, tbSelections: List<Int>) : ClassicAnswerRequest {
            val rawSequenceBuilder = StringBuilder()
            imagesStatus.forEach {
                rawSequenceBuilder.append(if(it) "1" else "0")
            }
            val statusDecimal = rawSequenceBuilder.toString().toInt(2)
            val sdStr = statusDecimal.toString()

            val sdBytes = sdStr.toByteArray()
            val md = MessageDigest.getInstance("SHA-1")
            val digest = md.digest(sdBytes)
            val statusHash = digest.joinToString("") { "%02x".format(it) }

            return ClassicAnswerRequest(arcApi, challengeId, "testfp",
                ClassicAnswersValue(statusHash, imagesStatus), tbSelections)
        }
    }
}