package sgtmelon.scriptum.cleanup.presentation.provider

import android.os.Build
import sgtmelon.scriptum.BuildConfig

/**
 * Object for provide [Build] and [BuildConfig] variables.
 */
object BuildProvider {

    fun isDebug() = BuildConfig.DEBUG

    fun inputControlMaxSize() = BuildConfig.INPUT_CONTROL_MAX_SIZE

    object Version {

        fun isMarshmallowLess(): Boolean {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
        }
    }
}