package co.arcaptcha.arcaptcha_native_sdk.themes

import android.content.Context
import android.graphics.Typeface

enum class AppFont(val fileName: String) {
    YEKAN("Yekan.ttf")
}

object FontManager {
    private val fontCache = mutableMapOf<AppFont, Typeface>()

    fun getTypeface(context: Context, font: AppFont): Typeface {
        return fontCache.getOrPut(font) {
            Typeface.createFromAsset(context.assets, "fonts/${font.fileName}")
        }
    }
}