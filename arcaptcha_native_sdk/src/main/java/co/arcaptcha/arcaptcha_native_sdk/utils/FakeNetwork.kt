package co.arcaptcha.arcaptcha_native_sdk.utils

import kotlinx.coroutines.delay

class FakeNetwork {
    companion object {
        suspend fun request(successCallback: () -> Unit, errorCallback: () -> Unit = {}){
            val seconds : Long = 1 + ((0 until 5).random()).toLong()
            delay(seconds * 1000)
            successCallback()
        }
    }
}