package sgtmelon.scriptum.office.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.util.List;

import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.item.SortItem;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.SortDef;

/**
 * Класс для работы с настройками приложения, а так же @Singleton для SharedPreferences
 */
public final class PrefUtils {

    private static SharedPreferences preferences;

    public static SharedPreferences getInstance(Context context){
        if(preferences == null){
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            synchronized (PrefUtils.class){
                if (preferences == null){
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
        final SharedPreferences pref = getInstance(context);

        final String keysStr = pref.getString(context.getString(R.string.pref_key_sort), SortDef.def);
        final String[] keysArr = keysStr.split(SortDef.divider);

        final StringBuilder order = new StringBuilder();
        for (String aKey : keysArr) {
            final int key = Integer.parseInt(aKey);
            order.append(DbAnn.orders[key]);

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
        final SharedPreferences pref = getInstance(context);

        textView.append("\n\nSort:");
        textView.append("\nNt: " + pref.getString(context.getString(R.string.pref_key_sort), SortDef.def));

        textView.append("\n\nNotes:");
        textView.append("\nColorDef: " + pref.getInt(context.getString(R.string.pref_key_color), context.getResources().getInteger(R.integer.pref_color_default)));
        textView.append("\nPause:\t" + pref.getBoolean(context.getString(R.string.pref_key_pause_save), context.getResources().getBoolean(R.bool.pref_pause_save_default)));
        textView.append("\nSave:\t" + pref.getBoolean(context.getString(R.string.pref_key_auto_save), context.getResources().getBoolean(R.bool.pref_auto_save_default)));
        textView.append("\nSTime:\t" + pref.getInt(context.getString(R.string.pref_key_save_time), context.getResources().getInteger(R.integer.pref_save_time_default)));
        textView.append("\nTheme:\t" + pref.getInt(context.getString(R.string.pref_key_theme), context.getResources().getInteger(R.integer.pref_theme_default)));
    }

    public static int getTheme(Context context) {
        final SharedPreferences pref = getInstance(context);
        return pref.getInt(context.getString(R.string.pref_key_theme), context.getResources().getInteger(R.integer.pref_theme_default));
    }

}