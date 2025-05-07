package co.arcaptcha.arcaptcha_native_sdk.models

enum class CaptchaState {
    LoadingCaptcha,
    AwaitingUserInput,
    SubmittingSolution,
    Done,
    Error
}