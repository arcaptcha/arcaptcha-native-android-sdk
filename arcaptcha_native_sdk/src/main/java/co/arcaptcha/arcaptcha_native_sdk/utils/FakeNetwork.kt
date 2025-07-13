package co.arcaptcha.arcaptcha_native_sdk.utils

import kotlinx.coroutines.delay

class FakeNetwork {
    companion object {
        suspend fun request(constDelay: Int, varDelay: Int, successCallback: () -> Unit){
            val seconds : Long = constDelay +
                    if(varDelay > 0) ((0 until varDelay).random()).toLong() else 0L
            if(seconds > 0) delay(seconds * 1000)
            successCallback()
        }
    }
}