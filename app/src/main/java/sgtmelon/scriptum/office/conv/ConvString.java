package sgtmelon.scriptum.office.conv;

import android.text.TextUtils;

import androidx.room.TypeConverter;
import sgtmelon.scriptum.office.annot.def.db.DefDb;

public class ConvString {

    @TypeConverter
    public Long[] fromString(String string) {
        if (string.equals(DefDb.none)) return new Long[0];
        else {
            String[] strArr = string.split(DefDb.divider);
            int size = strArr.length;

            Long[] array = new Long[size];
            for (int i = 0; i < size; i++) array[i] = Long.parseLong(strArr[i]);

            return array;
        }
    }

    @TypeConverter
    public String toString(Long[] string) {
        if (string == null || string.length == 0) return DefDb.none;
        else return TextUtils.join(DefDb.divider, string);
    }

}
