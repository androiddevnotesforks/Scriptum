package sgtmelon.scriptum.cleanup.extension

import android.net.Uri
import sgtmelon.scriptum.cleanup.presentation.control.system.AlarmControl
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Function for lazy property, call inside onCreate/onViewCreated.
 *
 * Value must contains context parameter in constructor. Like in [AlarmControl].
 *
 * If lazy property not initialized and rotation happen it comes to troubles.
 * Value will be wrong.
 */
@Deprecated("Use createOnUi")
fun Any.initLazy() = this.run {}

@Deprecated("Use UriConverter")
fun String.toUriOrNull(): Uri? {
    return try {
        Uri.parse(this)
    } catch (e: Throwable) {
        e.record()
        null
    }
}

fun String.clearSpace() = trim().replace("\\s+".toRegex(), replacement = " ")