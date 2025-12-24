package co.arcaptcha.arcaptcha_native_sdk.models

enum class ArcaptchaError(val code: Int) {
    CreateNetworkError(102),
    CreateServerError(101),
    AnswerNetworkError(202),
    AnswerServerError(201),
    AnswerWrongError(203),
    UnknownError(401)
}
