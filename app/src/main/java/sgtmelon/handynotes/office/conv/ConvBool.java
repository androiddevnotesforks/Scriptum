package sgtmelon.handynotes.office.conv;

import androidx.room.TypeConverter;

public class ConvBool {

    private final static int boolFalse = 0, boolTrue = 1;

    @TypeConverter
    public int fromBool(boolean bool){
        return bool ? boolTrue : boolFalse;
    }

    @TypeConverter
    public boolean toBool(int bool){
        return bool == boolTrue;
    }

}
