package sgtmelon.scriptum.extension

import android.net.Uri
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import java.util.*

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

/**
 * Make string upper/lowerCase without warning and without arguments.
 */
fun String.toUpperCase() = toUpperCase(Locale.ROOT)
fun String.toLowerCase() = toLowerCase(Locale.ROOT)

fun String.clearSpace() = trim().replace("\\s+".toRegex(), replacement = " ")