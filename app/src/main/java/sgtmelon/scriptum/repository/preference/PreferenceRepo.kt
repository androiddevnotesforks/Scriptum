package sgtmelon.scriptum.repository.preference

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.key.DbField

/**
 * Репозиторий для работы с [SharedPreferences]
 *
 * @author SerjantArbuz
 */
class PreferenceRepo(context: Context) : IPreferenceRepo {

    private val resources: Resources = context.resources

    private val preference: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    override fun isFirstStart(): Boolean {
        val defaultValue = resources.getBoolean(R.bool.pref_first_start_default)
        val value = preference.getBoolean(resources.getString(R.string.pref_first_start), defaultValue)

        if (value) setFirstStart(false)

        return value
    }

    override fun setFirstStart(value: Boolean) =
            preference.edit().putBoolean(resources.getString(R.string.pref_first_start), value).apply()

    //region Preference Screen

    override fun getTheme() = preference.getInt(resources.getString(R.string.pref_key_theme), resources.getInteger(R.integer.pref_theme_default))

    override fun setTheme(value: Int) = preference.edit().putInt(resources.getString(R.string.pref_key_theme), value).apply()

    override fun getSort() =
            preference.getString(resources.getString(R.string.pref_key_sort), Sort.def) ?: Sort.def

    override fun setSort(value: String) = preference.edit().putString(resources.getString(R.string.pref_key_sort), value).apply()

    override fun getDefaultColor() = preference.getInt(resources.getString(R.string.pref_key_color), resources.getInteger(R.integer.pref_color_default))

    override fun setDefaultColor(value: Int) = preference.edit().putInt(resources.getString(R.string.pref_key_color), value).apply()

    override fun isPauseSaveOn(): Boolean {
        val defaultValue = resources.getBoolean(R.bool.pref_pause_save_default)
        return preference.getBoolean(resources.getString(R.string.pref_key_pause_save), defaultValue)
    }

    override fun isAutoSaveOn(): Boolean {
        val defaultValue = resources.getBoolean(R.bool.pref_auto_save_default)
        return preference.getBoolean(resources.getString(R.string.pref_key_auto_save), defaultValue)
    }

    override fun getSavePeriod() = preference.getInt(resources.getString(R.string.pref_key_save_time), resources.getInteger(R.integer.pref_save_time_default))

    override fun setSavePeriod(value: Int) =
            preference.edit().putInt(resources.getString(R.string.pref_key_save_time), value).apply()

    //endregion

    /**
     * Формирование поискового запроса относительно настроек
     */
    override fun getSortNoteOrder() = StringBuilder().apply {
        val keysArr = getSort().split(Sort.divider.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (aKey in keysArr) {
            val key = aKey.toInt()
            append(DbField.Note.orders[key])

            if (key != Sort.create && key != Sort.change) {
                append(Sort.divider)
            } else {
                break
            }
        }
    }.toString()

    override fun getSortSummary() = StringBuilder().apply {
        val keysArr = getSort().split(Sort.divider.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val keysName = resources.getStringArray(R.array.pref_sort_text)

        for (i in keysArr.indices) {
            val key = keysArr[i].toInt()

            append(keysName[key].apply {
                if (i != 0) replace(resources.getString(R.string.pref_sort_summary_start), "").replaceFirst(" ".toRegex(), "")
            })

            if (key != Sort.create && key != Sort.change) append(Sort.divider) else break
        }
    }.toString()

    override fun getData() = StringBuilder().apply {
        append("Preference:\n\n")
        append("Sort: ${getSort()}\n")
        append("DefaultColor: ${getDefaultColor()}\n")
        append("PauseSave: ${isPauseSaveOn()}\n")
        append("AutoSave: ${isAutoSaveOn()}\n")
        append("SaveTime: ${getSavePeriod()}\n")
        append("Theme: ${getTheme()}\n")
    }.toString()

    companion object {
        fun getInstance(context: Context): IPreferenceRepo = PreferenceRepo(context)
    }

}