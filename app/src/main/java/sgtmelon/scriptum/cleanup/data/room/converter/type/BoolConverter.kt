package sgtmelon.scriptum.cleanup.data.room.converter.type

import androidx.annotation.IntRange
import androidx.room.TypeConverter

/**
 * Converter from number to boolean value and vice versa
 */
class BoolConverter {

    @TypeConverter fun toInt(value: Boolean) = if (value) TRUE else FALSE

    @TypeConverter fun toBool(@IntRange(from = 0, to = 1) value: Int) = value == TRUE

    private companion object {
        const val TRUE = 1
        const val FALSE = 0
    }

}