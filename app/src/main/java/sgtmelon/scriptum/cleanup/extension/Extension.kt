package sgtmelon.scriptum.cleanup.extension

import android.net.Uri
import sgtmelon.scriptum.cleanup.presentation.control.system.AlarmControl

/**
 * Function for lazy property, call inside onCreate/onViewCreated.
 *
 * Value must contains context parameter in constructor. Like in [AlarmControl].
 *
 * If lazy property not initialized and rotation happen it comes to troubles.
 * Value will be wrong.
 */
fun Any.initLazy() = this.run {}

// TODO add firebase log
fun String.toUri(): Uri? = let {
    return@let try {
        Uri.parse(it)
    } catch (e: Throwable) {
        null
    }
}

fun String.clearSpace() = trim().replace("\\s+".toRegex(), replacement = " ")