package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.exception.EnumConverterException
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Parent class for all enum converters:
 * - Int to Enum
 * - Enum to Int
 *
 * Also it reports to firebase if converting not goes well.
 */
abstract class ParentEnumConverter<E : Enum<E>> {

    abstract val values: Array<E>

    abstract fun getOrdinalException(ordinal: Int): EnumConverterException

    fun toInt(value: E): Int = value.ordinal

    fun toEnum(value: Int): E? {
        val enum = values.getOrNull(value)

        if (enum == null) {
            getOrdinalException(value).record()
        }

        return enum
    }
}