@file:JvmName(name = "ExtensionUtils")

package sgtmelon.scriptum.infrastructure.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

fun getCrashlytics() = FirebaseCrashlytics.getInstance()