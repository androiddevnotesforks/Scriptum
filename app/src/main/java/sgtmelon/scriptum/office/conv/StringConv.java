package sgtmelon.scriptum.office.conv;

import android.text.TextUtils;

import androidx.room.TypeConverter;
import sgtmelon.scriptum.office.annot.DbAnn;

/**
 * Преобразование String - Long[]
 * В строке разделителем является divider {@link DbAnn}
 */
public final class StringConv {

    @TypeConverter
    public Long[] fromString(String value) {
        if (value.equals(DbAnn.none)) return new Long[0];
        else {
            final String[] stringArray = value.split(DbAnn.divider);
            final int size = stringArray.length;

            final Long[] longArray = new Long[size];
            for (int i = 0; i < size; i++) {
                longArray[i] = Long.parseLong(stringArray[i]);
            }

            return longArray;
        }
    }

    @TypeConverter
    public String toString(Long[] value) {
        if (value == null || value.length == 0) return DbAnn.none;
        else return TextUtils.join(DbAnn.divider, value);
    }

}