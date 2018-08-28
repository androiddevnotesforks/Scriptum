package sgtmelon.handynotes.office.conv;

import android.text.TextUtils;

import androidx.room.TypeConverter;
import sgtmelon.handynotes.office.annot.Db;

public class ConvString {

    @TypeConverter
    public Long[] fromString(String string) {
        if (string.equals(Db.none)) return new Long[0];
        else {
            String[] strArr = string.split(Db.divider);
            int size = strArr.length;

            Long[] array = new Long[size];
            for (int i = 0; i < size; i++) array[i] = Long.parseLong(strArr[i]);

            return array;
        }
    }

    @TypeConverter
    public String toString(Long[] string) {
        if (string == null || string.length == 0) return Db.none;
        else return TextUtils.join(Db.divider, string);
    }

}
