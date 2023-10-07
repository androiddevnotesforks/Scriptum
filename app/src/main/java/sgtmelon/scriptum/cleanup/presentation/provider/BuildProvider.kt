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
        val current get() = Build.VERSION.SDK_INT

        val isPre30 get() = current < Build.VERSION_CODES.R
    }
}