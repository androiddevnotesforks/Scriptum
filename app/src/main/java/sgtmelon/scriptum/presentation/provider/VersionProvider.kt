package sgtmelon.scriptum.presentation.provider

import android.os.Build

/**
 * Object for get version compare and tests.
 */
object VersionProvider {

    fun isMarshmallowLess(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
    }

}