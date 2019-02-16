package sgtmelon.scriptum.office.converter

import androidx.room.TypeConverter

class BoolConverter {

    private companion object {
        const val trueValue = 1
        const val falseValue = 0
    }

    @TypeConverter
    fun fromBool(value: Boolean) = if (value) trueValue else falseValue

    @TypeConverter
    fun toBool(value: Int) = value == trueValue

}