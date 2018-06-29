package sgtmelon.handynotes.office.conv;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import sgtmelon.handynotes.office.annot.Db;

public class ConvString {

    @TypeConverter
    public Long[] fromString(String string) {
        if (string.equals(Db.none)) return new Long[0];
        else {
            String[] arrStr = string.split(Db.divider);
            int size = arrStr.length;

            Long[] array = new Long[size];
            for (int i = 0; i < size; i++) array[i] = Long.parseLong(arrStr[i]);

            return array;
        }
    }

    @TypeConverter
    public String toString(Long[] string) {
        if (string == null || string.length == 0) return Db.none;
        else return TextUtils.join(Db.divider, string);
    }

}
