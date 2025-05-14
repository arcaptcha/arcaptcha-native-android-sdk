package co.arcaptcha.arcaptcha_native_sdk.models.requests

import co.arcaptcha.arcaptcha_native_sdk.remote.ArcaptchaAPI

class ClassicAnswerRequest(
    arcApi: ArcaptchaAPI,
    val challenge_id: String,
    val  fp: String,
    val  time_between_selections: List<Int>,
) : BaseRequest(arcApi){
    data class ClassicAnswersBody (
        val a: String,
        val m: List<Boolean>
    )

    companion object {
        fun getInstance(challengeId: String, imagesStatus: List<Boolean>, tbSelections: List<Int>) : ClassicAnswerRequest {
            TODO()
        }
    }
}