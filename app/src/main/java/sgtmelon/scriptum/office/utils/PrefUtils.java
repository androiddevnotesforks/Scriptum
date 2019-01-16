package sgtmelon.scriptum.office.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.util.List;

import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.item.SortItem;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.SortDef;
import sgtmelon.scriptum.office.annot.def.ThemeDef;

/**
 * Класс для работы с настройками приложения, а так же @Singleton для SharedPreferences
 */
public final class PrefUtils { // TODO: 15.01.2019 переделать без передачи Context / Аннотировать NonNull/Nullable

    private static SharedPreferences preferences;

    private static SharedPreferences getInstance(Context context) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            synchronized (PrefUtils.class) {
                if (preferences == null) {
                    preferences = PreferenceManager.getDefaultSharedPreferences(context);
                }
            }
        }

        return preferences;
    }

    /**
     * @param context - Для получения настроек
     * @return - Формирование поискового запроса относительно настроек
     */
    public static String getSortNoteOrder(Context context) {
        final String keysStr = getSort(context);
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
    public static String getSortByList(List<SortItem> listSort) {
        final StringBuilder order = new StringBuilder();
        for (int i = 0; i < listSort.size(); i++) {
            order.append(Integer.toString(listSort.get(i).getKey()));

            if (i != listSort.size() - 1) {
                order.append(SortDef.divider);
            }
        }
        return order.toString();
    }

    public static String getSortSummary(Context context, String keys) {
        final StringBuilder order = new StringBuilder();

        final String[] keysArr = keys.split(SortDef.divider);
        final String[] keysName = context.getResources().getStringArray(R.array.pref_sort_text);

        for (int i = 0; i < keysArr.length; i++) {
            final int key = Integer.parseInt(keysArr[i]);

            String summary = keysName[key];
            if (i != 0) {
                summary = summary.replace(context.getString(R.string.pref_sort_summary_start), "").replaceFirst(" ", "");
            }

            order.append(summary);
            if (key != SortDef.create && key != SortDef.change) {
                order.append(SortDef.divider);
            } else break;

        }

        return order.toString();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean getSortEqual(String keys1, String keys2) {
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

    public static void listAllPref(Context context, TextView textView) {
        textView.append("\n\nSort:");
        textView.append("\nNt: " + getSort(context));

        textView.append("\n\nNotes:");
        textView.append("\nColorDef: " + getDefaultColor(context));
        textView.append("\nPause:\t" + getPauseSave(context));
        textView.append("\nSave:\t" + getAutoSave(context));
        textView.append("\nSTime:\t" + getSaveTime(context));
        textView.append("\nTheme:\t" + getTheme(context));
    }

    public static boolean getFirstStart(Context context) {
        return getInstance(context).getBoolean(context.getString(R.string.pref_first_start),
                context.getResources().getBoolean(R.bool.pref_first_start_default)
        );
    }

    public static void setFirstStart(Context context, boolean isFirst) {
        getInstance(context).edit()
                .putBoolean(context.getString(R.string.pref_first_start), isFirst)
                .apply();
    }

    public static String getSort(Context context) {
        return getInstance(context).getString(context.getString(R.string.pref_key_sort), SortDef.def);
    }

    public static void setSort(Context context, String sort) {
        getInstance(context).edit()
                .putString(context.getString(R.string.pref_key_sort), sort)
                .apply();
    }

    public static int getDefaultColor(Context context) {
        return getInstance(context).getInt(context.getString(R.string.pref_key_color), context.getResources().getInteger(R.integer.pref_color_default));
    }

    public static void setDefaultColor(Context context, @ColorDef int color) {
        getInstance(context).edit()
                .putInt(context.getString(R.string.pref_key_color), color)
                .apply();
    }

    public static boolean getPauseSave(Context context) {
        final boolean def = context.getResources().getBoolean(R.bool.pref_pause_save_default);
        return getInstance(context).getBoolean(context.getString(R.string.pref_key_pause_save), def);
    }

    public static boolean getAutoSave(Context context) {
        final boolean def = context.getResources().getBoolean(R.bool.pref_auto_save_default);
        return getInstance(context).getBoolean(context.getString(R.string.pref_key_auto_save), def);
    }

    public static void setSaveTime(Context context, int saveTime) {
        getInstance(context).edit()
                .putInt(context.getString(R.string.pref_key_save_time), saveTime)
                .apply();
    }

    public static int getSaveTime(Context context) {
        return getInstance(context).getInt(context.getString(R.string.pref_key_save_time), context.getResources().getInteger(R.integer.pref_save_time_default));
    }

    public static void setTheme(Context context, @ThemeDef int theme) {
        getInstance(context).edit()
                .putInt(context.getString(R.string.pref_key_theme), theme)
                .apply();
    }

    public static int getTheme(Context context) {
        return getInstance(context).getInt(context.getString(R.string.pref_key_theme), context.getResources().getInteger(R.integer.pref_theme_default));
    }

}