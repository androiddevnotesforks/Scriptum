@file:JvmName(name = "FireExtensionsUtils")

package sgtmelon.scriptum.infrastructure.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

fun getCrashlytics() = FirebaseCrashlytics.getInstance()

fun Throwable.record() = getCrashlytics().recordException(this)

fun Throwable.recordThrow(): Nothing = throw this.apply { record() }