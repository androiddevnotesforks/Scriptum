package sgtmelon.scriptum.cleanup.presentation.provider

import android.os.Build
import sgtmelon.scriptum.BuildConfig

/**
 * Object for provide [Build] and [BuildConfig] variables.
 */
object BuildProvider {

    val isDebug get() = BuildConfig.DEBUG

    val noteHistoryMaxSize get() = BuildConfig.NOTE_HISTORY_MAX_SIZE

    object Version {
        val isPre30 get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
        val isPre33 get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
    }
}