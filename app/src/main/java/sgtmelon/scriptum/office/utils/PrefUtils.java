package sgtmelon.scriptum.office.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.item.SortItem;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.SortDef;
import sgtmelon.scriptum.office.annot.def.ThemeDef;

/**
 * Класс для работы с настройками приложения, а так же @Singleton для SharedPreferences
 */
public final class PrefUtils {

    private static PrefUtils prefUtils;

    private final SharedPreferences preferences;
    private final Resources resources;

    public PrefUtils(@NonNull Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        resources = context.getResources();
    }

    public static PrefUtils getInstance(@NonNull Context context) {
        if (prefUtils == null) {
            prefUtils = new PrefUtils(context);
            synchronized (PrefUtils.class) {
                if (prefUtils == null) {
                    prefUtils = new PrefUtils(context);
                }
            }
        }

        return prefUtils;
    }

    /**
     * @return - Формирование поискового запроса относительно настроек
     */
    @NonNull
    public String getSortNoteOrder() {
        final String keysStr = getSort();
        final String[] keysArr = keysStr.split(SortDef.divider);

        final StringBuilder order = new StringBuilder();
        for (String aKey : keysArr) {
            final int key = Integer.parseInt(aKey);
            order.append(DbAnn.Note.orders[key]);

            if (key != SortDef.create && key != SortDef.change) {
                order.append(SortDef.divider);
            } else {
                break;
            }
        }

        return order.toString();
    }

    /**
     * @param listSort - Список моделей из диалога
     * @return - Строка сортировки
     */
    @NonNull
    public static String getSortByList(@NonNull List<SortItem> listSort) {
        final StringBuilder order = new StringBuilder();
        for (int i = 0; i < listSort.size(); i++) {
            order.append(Integer.toString(listSort.get(i).getKey()));

            if (i != listSort.size() - 1) {
                order.append(SortDef.divider);
            }
        }

        return order.toString();
    }

    @NonNull
    public String getSortSummary(@NonNull String keys) {
        final StringBuilder order = new StringBuilder();

        final String[] keysArr = keys.split(SortDef.divider);
        final String[] keysName = resources.getStringArray(R.array.pref_sort_text);

        for (int i = 0; i < keysArr.length; i++) {
            final int key = Integer.parseInt(keysArr[i]);

            String summary = keysName[key];
            if (i != 0) {
                summary = summary.replace(resources.getString(R.string.pref_sort_summary_start), "").replaceFirst(" ", "");
            }

            order.append(summary);
            if (key != SortDef.create && key != SortDef.change) {
                order.append(SortDef.divider);
            } else break;

        }

        return order.toString();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean getSortEqual(@NonNull String keys1, @NonNull String keys2) {
        final String[] keysArr1 = keys1.split(SortDef.divider);
        final String[] keysArr2 = keys2.split(SortDef.divider);

        for (int i = 0; i < keysArr1.length; i++) {
            if (!keysArr1[i].equals(keysArr2[i])) {
                return false;
            }
            if (keysArr1[i].equals(Integer.toString(SortDef.create)) || keysArr1[i].equals(Integer.toString(SortDef.change))) {
                break;
            }
        }

        return true;
    }

    public void listAllPref(@NonNull TextView textView) {
        textView.append("\n\nSort:");
        textView.append("\nNt: " + getSort());

        textView.append("\n\nNotes:");
        textView.append("\nColorDef: " + getDefaultColor());
        textView.append("\nPause:\t" + getPauseSave());
        textView.append("\nSave:\t" + getAutoSave());
        textView.append("\nSTime:\t" + getSaveTime());
        textView.append("\nTheme:\t" + getTheme());
    }

    public boolean getFirstStart() {
        return preferences.getBoolean(
                resources.getString(R.string.pref_first_start), resources.getBoolean(R.bool.pref_first_start_default)
        );
    }

    public void setFirstStart(boolean isFirst) {
        preferences.edit()
                .putBoolean(resources.getString(R.string.pref_first_start), isFirst)
                .apply();
    }

    @NonNull
    public String getSort() {
        return preferences.getString(resources.getString(R.string.pref_key_sort), SortDef.def);
    }

    public void setSort(@NonNull String sort) {
        preferences.edit()
                .putString(resources.getString(R.string.pref_key_sort), sort)
                .apply();
    }

    public int getDefaultColor() {
        return preferences.getInt(resources.getString(R.string.pref_key_color), resources.getInteger(R.integer.pref_color_default));
    }

    public void setDefaultColor(@ColorDef int color) {
        preferences.edit()
                .putInt(resources.getString(R.string.pref_key_color), color)
                .apply();
    }

    public boolean getPauseSave() {
        final boolean def = resources.getBoolean(R.bool.pref_pause_save_default);

        return preferences.getBoolean(resources.getString(R.string.pref_key_pause_save), def);
    }

    public boolean getAutoSave() {
        final boolean def = resources.getBoolean(R.bool.pref_auto_save_default);

        return preferences.getBoolean(resources.getString(R.string.pref_key_auto_save), def);
    }

    public void setSaveTime(int saveTime) {
        preferences.edit()
                .putInt(resources.getString(R.string.pref_key_save_time), saveTime)
                .apply();
    }

    public int getSaveTime() {
        return preferences.getInt(resources.getString(R.string.pref_key_save_time), resources.getInteger(R.integer.pref_save_time_default));
    }

    public void setTheme(@ThemeDef int theme) {
        preferences.edit()
                .putInt(resources.getString(R.string.pref_key_theme), theme)
                .apply();
    }

    public int getTheme() {
        return preferences.getInt(resources.getString(R.string.pref_key_theme), resources.getInteger(R.integer.pref_theme_default));
    }

}