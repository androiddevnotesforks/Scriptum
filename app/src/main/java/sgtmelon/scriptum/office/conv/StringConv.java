package sgtmelon.scriptum.office.conv;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.room.TypeConverter;
import sgtmelon.scriptum.office.annot.DbAnn;

/**
 * Преобразование String - List<Long>
 * В строке разделителем является divider {@link DbAnn}
 */
public final class StringConv {

    @TypeConverter
    public List<Long> fromString(String value) {
        if (!value.equals(DbAnn.none)) {
            final String[] stringArray = value.split(DbAnn.divider);
            final List<Long> longList = new ArrayList<>();

            for (String str : stringArray) {
                longList.add(Long.parseLong(str));
            }

            return longList;
        } else {
            return new ArrayList<>();
        }
    }

    @TypeConverter
    public String toString(List<Long> value) {
        if (value != null && value.size() != 0) {
            return TextUtils.join(DbAnn.divider, value);
        } else {
            return DbAnn.none;
        }
    }

}