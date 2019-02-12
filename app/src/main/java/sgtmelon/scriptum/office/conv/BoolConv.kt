package sgtmelon.scriptum.office.conv

import androidx.room.TypeConverter

class BoolConv {

    @TypeConverter
    fun fromBool(value: Boolean) = if (value) 1 else 0

    @TypeConverter
    fun toBool(value: Int) = value == 1

}