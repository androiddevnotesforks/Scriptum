package sgtmelon.scriptum.office.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import android.widget.TextView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.DbAnn
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.office.annot.def.SortDef
import sgtmelon.scriptum.office.annot.def.ThemeDef

/**
 * Класс для работы с настройками приложения, а так же @Singleton для SharedPreferences
 */
class PrefUtils(context: Context) {

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
                val key = Integer.parseInt(aKey)
                order.append(DbAnn.Note.orders[key])

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
        set(isFirst) = preferences.edit().putBoolean(resources.getString(R.string.pref_first_start), isFirst).apply()

    var sort: String
        get() = preferences.getString(resources.getString(R.string.pref_key_sort), SortDef.def)
                ?: SortDef.def
        set(sort) = preferences.edit().putString(resources.getString(R.string.pref_key_sort), sort).apply()

    var defaultColor: Int
        get() = preferences.getInt(resources.getString(R.string.pref_key_color), resources.getInteger(R.integer.pref_color_default))
        set(@ColorDef color) = preferences.edit().putInt(resources.getString(R.string.pref_key_color), color).apply()

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
        set(saveTime) = preferences.edit().putInt(resources.getString(R.string.pref_key_save_time), saveTime).apply()

    var theme: Int
        get() = preferences.getInt(resources.getString(R.string.pref_key_theme), resources.getInteger(R.integer.pref_theme_default))
        set(@ThemeDef theme) = preferences.edit().putInt(resources.getString(R.string.pref_key_theme), theme).apply()

    fun getSortSummary(): String {
        val order = StringBuilder()

        val keysArr = sort.split(SortDef.divider.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val keysName = resources.getStringArray(R.array.pref_sort_text)

        for (i in keysArr.indices) {
            val key = Integer.parseInt(keysArr[i])

            var summary = keysName[key]
            if (i != 0) {
                summary = summary.replace(resources.getString(R.string.pref_sort_summary_start), "").replaceFirst(" ".toRegex(), "")
            }

            order.append(summary)
            if (key != SortDef.create && key != SortDef.change) {
                order.append(SortDef.divider)
            } else
                break

        }

        return order.toString()
    }

    fun listAllPref(textView: TextView) {
        textView.append("\n\nSort:")
        textView.append("\nNt: $sort")

        textView.append("\n\nNotes:")
        textView.append("\nColorDef: $defaultColor")
        textView.append("\nPause: $pauseSave")
        textView.append("\nSave: $autoSave")
        textView.append("\nSTime: $saveTime")
        textView.append("\nTheme: $theme")
    }

}