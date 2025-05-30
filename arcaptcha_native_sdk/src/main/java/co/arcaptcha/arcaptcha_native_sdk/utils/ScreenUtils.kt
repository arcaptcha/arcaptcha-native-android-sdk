package co.arcaptcha.arcaptcha_native_sdk.utils

import android.content.Context

class ScreenUtils {
    companion object {
        fun dpToPx(context: Context, dp: Int): Int {
            val density = context.resources.displayMetrics.density
            return (dp * density + 0.5f).toInt()
        }
    }
}