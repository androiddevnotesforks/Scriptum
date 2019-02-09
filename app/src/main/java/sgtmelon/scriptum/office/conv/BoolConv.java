package sgtmelon.scriptum.office.conv;

import androidx.room.TypeConverter;

final public class BoolConv {

    @TypeConverter
    public int fromBool(boolean value) {
        return value ? 1 : 0;
    }

    @TypeConverter
    public boolean toBool(int value) {
        return value == 1;
    }

}