package sgtmelon.scriptum.office.converter

import androidx.room.TypeConverter

class BoolConverter {

    @TypeConverter
    fun fromBool(value: Boolean) = if (value) 1 else 0

    @TypeConverter
    fun toBool(value: Int) = value == 1

}