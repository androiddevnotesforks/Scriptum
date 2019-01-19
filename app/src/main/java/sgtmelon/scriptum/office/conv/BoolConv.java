package sgtmelon.scriptum.office.conv;

import androidx.room.TypeConverter;

/**
 * Преобразование int - boolean
 */
public final class BoolConv {

    private final static int boolFalse = 0, boolTrue = 1;

    @TypeConverter
    public int fromBool(boolean value) {
        return value
                ? boolTrue
                : boolFalse;
    }

    @TypeConverter
    public boolean toBool(int value) {
        return value == boolTrue;
    }

}
