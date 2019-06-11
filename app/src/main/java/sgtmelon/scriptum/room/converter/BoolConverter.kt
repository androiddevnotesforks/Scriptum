package sgtmelon.scriptum.room.converter

import androidx.annotation.IntRange
import androidx.room.TypeConverter

class BoolConverter {

    @TypeConverter fun toInt(value: Boolean) = if (value) trueValue else falseValue

    @TypeConverter fun toBool(@IntRange(from = 0, to = 1) value: Int) = value == trueValue

    private companion object {
        const val trueValue = 1
        const val falseValue = 0
    }

}