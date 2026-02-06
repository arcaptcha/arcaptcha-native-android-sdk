# Arcaptcha Native SDK - ProGuard/R8 consumer rules
# These rules are applied when an app that uses this SDK is built in release mode
# with minification enabled. They prevent model/request/response classes from
# being stripped or obfuscated, so Gson can correctly deserialize API responses.

# Keep all Arcaptcha model classes and their members (used by Gson/Retrofit).
# Without this, release builds fail when casting responses to Arcaptcha models.
-keep class co.arcaptcha.arcaptcha_native_sdk.models.** { *; }

# Keep Retrofit API interface and implementation
-keep interface co.arcaptcha.arcaptcha_native_sdk.remote.CaptchaApiInterface { *; }

# Keep generic signature of Retrofit Call adapters (for correct response types)
-keepattributes Signature
-keepattributes Exceptions

# Gson: keep type information for generic types
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
