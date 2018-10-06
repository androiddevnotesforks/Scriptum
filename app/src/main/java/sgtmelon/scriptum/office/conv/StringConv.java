package sgtmelon.scriptum.office.conv;

import android.text.TextUtils;

import androidx.room.TypeConverter;
import sgtmelon.scriptum.office.annot.DbAnn;

public final class StringConv {

    @TypeConverter
    public Long[] fromString(String string) {
        if (string.equals(DbAnn.none)) return new Long[0];
        else {
            String[] strArr = string.split(DbAnn.divider);
            int size = strArr.length;

            Long[] array = new Long[size];
            for (int i = 0; i < size; i++) array[i] = Long.parseLong(strArr[i]);

            return array;
        }
    }

    @TypeConverter
    public String toString(Long[] string) {
        if (string == null || string.length == 0) return DbAnn.none;
        else return TextUtils.join(DbAnn.divider, string);
    }

}
