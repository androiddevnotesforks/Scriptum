package sgtmelon.scriptum.infrastructure.model.exception

import kotlin.reflect.KProperty0


/**
 * Exception for detecting cases when receive not valid data (e.g. when open a new screen).
 *
 * Data class needed mainly for testing.
 */
data class BundleException(val value: KProperty0<*>) :
    Throwable("Get wrong data for value: ${value.name}")
