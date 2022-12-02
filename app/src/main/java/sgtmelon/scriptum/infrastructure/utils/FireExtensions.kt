@file:JvmName(name = "FireExtensionsUtils")

package sgtmelon.scriptum.infrastructure.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

fun getCrashlytics() = FirebaseCrashlytics.getInstance()

fun Throwable.record(withPrint: Boolean = true) = apply {
    if (withPrint) {
        printStackTrace()
    }

    getCrashlytics().recordException(this)
}

fun recordException(text: String) {
    Throwable(text).record()
}