package sgtmelon.scriptum.office.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

}