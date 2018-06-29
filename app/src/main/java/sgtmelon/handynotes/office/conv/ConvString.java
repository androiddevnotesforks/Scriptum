package sgtmelon.handynotes.office.conv;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import sgtmelon.handynotes.app.data.DataInfo;

public class ConvString extends DataInfo {

    @TypeConverter
    public Long[] fromString(String string) {
        if (string.equals(none)) return new Long[0];
        else {
            String[] arrStr = string.split(divider);
            int size = arrStr.length;

            Long[] array = new Long[size];
            for (int i = 0; i < size; i++) array[i] = Long.parseLong(arrStr[i]);

            return array;
        }
    }

    @TypeConverter
    public String toString(Long[] string) {
        if (string == null || string.length == 0) return none;
        else return TextUtils.join(divider, string);
    }

}
