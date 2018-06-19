package sgtmelon.handynotes.data.converter;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import sgtmelon.handynotes.data.DataInfo;

public class ConverterString extends DataInfo {

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
