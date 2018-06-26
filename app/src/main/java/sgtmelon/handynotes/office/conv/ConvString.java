package sgtmelon.handynotes.office.conv;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import sgtmelon.handynotes.app.data.DataInfo;

public class ConvString extends DataInfo {

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
