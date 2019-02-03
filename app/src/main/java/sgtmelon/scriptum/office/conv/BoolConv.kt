package sgtmelon.scriptum.office.conv

import androidx.room.TypeConverter

private const val boolFalse = 0
private const val boolTrue = 1

/**
 * Преобразование int - boolean
 */
class BoolConv {

    @TypeConverter
    fun fromBool(value: Boolean): Int {
        return if (value) boolTrue else boolFalse
    }

    @TypeConverter
    fun toBool(value: Int): Boolean {
        return value == boolTrue
    }

}
