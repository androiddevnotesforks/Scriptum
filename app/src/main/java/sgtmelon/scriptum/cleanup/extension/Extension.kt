package sgtmelon.scriptum.cleanup.extension

import sgtmelon.scriptum.cleanup.presentation.control.system.AlarmDelegator

/**
 * Function for lazy property, call inside onCreate/onViewCreated.
 *
 * Value must contains context parameter in constructor. Like in [AlarmDelegator].
 *
 * If lazy property not initialized and rotation happen it comes to troubles.
 * Value will be wrong.
 */
@Deprecated("Use createOnUi")
fun Any.initLazy() = this.run {}

fun String.clearSpace() = trim().replace("\\s+".toRegex(), replacement = " ")