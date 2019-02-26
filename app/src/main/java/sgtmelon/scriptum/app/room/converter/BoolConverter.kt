package sgtmelon.scriptum.app.room.converter

import androidx.room.TypeConverter

class BoolConverter {

    @TypeConverter
    fun fromBool(value: Boolean) = if (value) trueValue else falseValue

    @TypeConverter
    fun toBool(value: Int) = value == trueValue

    private companion object {
        const val trueValue = 1
        const val falseValue = 0
    }

}