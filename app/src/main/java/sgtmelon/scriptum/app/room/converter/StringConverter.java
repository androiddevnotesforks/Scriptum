package sgtmelon.scriptum.app.room.converter;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;
import sgtmelon.scriptum.office.annot.DbAnn;

/**
 * Преобразование String - List<Long>
 * В строке разделителем является DIVIDER {@link DbAnn}
 */
public final class StringConverter {

    @TypeConverter
    public List<Long> fromString(@NonNull String value) {
        if (!value.equals(DbAnn.Value.NONE) && !value.equals("")) {
            final String[] stringArray = value.split(DbAnn.Value.DIVIDER);
            final List<Long> listLong = new ArrayList<>();

            for (String str : stringArray) {
                listLong.add(Long.parseLong(str));
            }

            return listLong;
        } else {
            return new ArrayList<>();
        }
    }

    @TypeConverter
    public String toString(List<Long> value) {
        if (value != null && value.size() != 0) {
            return TextUtils.join(DbAnn.Value.DIVIDER, value);
        } else {
            return DbAnn.Value.NONE;
        }
    }

}