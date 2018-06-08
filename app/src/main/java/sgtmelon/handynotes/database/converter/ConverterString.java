package sgtmelon.handynotes.database.converter;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import sgtmelon.handynotes.database.DataBaseDescription;

public class ConverterString extends DataBaseDescription{

    @TypeConverter
    public String[] fromString (String string){
        if (string.equals(none)) return new String[0];
        else return string.split(divider);
    }

    @TypeConverter
    public String toString(String[] string){
        if (string.length == 0) return none;
        else return TextUtils.join(divider, string);
    }

}
