package sgtmelon.scriptum.cleanup.presentation.provider

import android.os.Build
import sgtmelon.scriptum.BuildConfig

/**
 * Object for provide [Build] and [BuildConfig] variables.
 */
object BuildProvider {

    fun isDebug() = BuildConfig.DEBUG

    fun noteHistoryMaxSize() = BuildConfig.INPUT_CONTROL_MAX_SIZE

}