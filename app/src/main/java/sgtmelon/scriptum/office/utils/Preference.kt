package sgtmelon.scriptum.office.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.key.DbField
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.office.annot.def.SortDef
import sgtmelon.scriptum.office.annot.def.ThemeDef

/**
 * Класс для работы с настройками приложения, а так же @Singleton для SharedPreferences
 */
class Preference(context: Context) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val resources: Resources = context.resources

    /**
     * @return - Формирование поискового запроса относительно настроек
     */
    val sortNoteOrder: String
        get() {
            val keysStr = sort
            val keysArr = keysStr.split(SortDef.divider.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val order = StringBuilder()
            for (aKey in keysArr) {
                val key = aKey.toInt()
                order.append(DbField.Note.orders[key])

                if (key != SortDef.create && key != SortDef.change) {
                    order.append(SortDef.divider)
                } else {
                    break
                }
            }

            return order.toString()
        }

    var firstStart: Boolean
        get() {
            val def = resources.getBoolean(R.bool.pref_first_start_default)
            val value = preferences.getBoolean(resources.getString(R.string.pref_first_start), def)

            if (value) firstStart = false

            return value
        }
        set(value) = preferences.edit().putBoolean(resources.getString(R.string.pref_first_start), value).apply()

    var sort: String
        get() = preferences.getString(resources.getString(R.string.pref_key_sort), SortDef.def)
                ?: SortDef.def
        set(value) = preferences.edit().putString(resources.getString(R.string.pref_key_sort), value).apply()

    var defaultColor: Int
        get() = preferences.getInt(resources.getString(R.string.pref_key_color), resources.getInteger(R.integer.pref_color_default))
        set(@ColorDef value) = preferences.edit().putInt(resources.getString(R.string.pref_key_color), value).apply()

    val pauseSave: Boolean
        get() {
            val def = resources.getBoolean(R.bool.pref_pause_save_default)
            return preferences.getBoolean(resources.getString(R.string.pref_key_pause_save), def)
        }

    val autoSave: Boolean
        get() {
            val def = resources.getBoolean(R.bool.pref_auto_save_default)
            return preferences.getBoolean(resources.getString(R.string.pref_key_auto_save), def)
        }

    var saveTime: Int
        get() = preferences.getInt(resources.getString(R.string.pref_key_save_time), resources.getInteger(R.integer.pref_save_time_default))
        set(value) = preferences.edit().putInt(resources.getString(R.string.pref_key_save_time), value).apply()

    var theme: Int
        get() = preferences.getInt(resources.getString(R.string.pref_key_theme), resources.getInteger(R.integer.pref_theme_default))
        set(@ThemeDef value) = preferences.edit().putInt(resources.getString(R.string.pref_key_theme), value).apply()

    fun getSortSummary() = StringBuilder().apply {
        val keysArr = sort.split(SortDef.divider.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val keysName = resources.getStringArray(R.array.pref_sort_text)

        for (i in keysArr.indices) {
            val key = keysArr[i].toInt()

            val summary = keysName[key].apply {
                if (i != 0) {
                    replace(resources.getString(R.string.pref_sort_summary_start), "").replaceFirst(" ".toRegex(), "")
                }
            }

            append(summary)

            if (key != SortDef.create && key != SortDef.change) append(SortDef.divider) else break
        }
    }.toString()

    fun getData() = StringBuilder().apply {
        append("Preference:")

        append("\n\nSort:")
        append("\nNt: $sort")

        append("\n\nNotes:")
        append("\nColorDef: $defaultColor")
        append("\nPause: $pauseSave")
        append("\nSave: $autoSave")
        append("\nSTime: $saveTime")
        append("\nTheme: $theme")
    }.toString()

}