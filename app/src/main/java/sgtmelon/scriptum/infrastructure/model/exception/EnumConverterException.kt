package sgtmelon.scriptum.infrastructure.model.exception

import kotlin.reflect.KClass

/**
 * Exception for detecting bad cases when convert ordinal into enum value.
 *
 * Data class needed mainly for testing.
 */
data class EnumConverterException(
    private val ordinal: Int,
    private val enumClass: KClass<*>,
    private val converterClass: KClass<*>
) : Throwable(
    "Ordinal value passed inside converter is invalid for this enum type!\n" +
            "Can't convert value=$ordinal into '${enumClass.simpleName}' " +
            "(via '${converterClass.simpleName}')"
)