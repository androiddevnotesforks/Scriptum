package sgtmelon.scriptum.repository.preference

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import sgtmelon.scriptum.R

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
        val defaultValue = resources.getBoolean(R.bool.value_first_start)
        val value = preference.getBoolean(resources.getString(R.string.key_first_start), defaultValue)

        if (value) setFirstStart(false)

        return value
    }

    override fun setFirstStart(value: Boolean) =
            preference.edit().putBoolean(resources.getString(R.string.key_first_start), value).apply()

    //region Preference Screen

    override fun getTheme() = preference.getInt(resources.getString(R.string.key_app_theme), resources.getInteger(R.integer.value_app_theme))

    override fun setTheme(value: Int) = preference.edit().putInt(resources.getString(R.string.key_app_theme), value).apply()

    override fun getRepeat() = preference.getInt(resources.getString(R.string.key_alarm_repeat), resources.getInteger(R.integer.value_alarm_repeat))

    override fun setRepeat(value: Int) =
            preference.edit().putInt(resources.getString(R.string.key_alarm_repeat), value).apply()

    override fun getSort() = preference.getInt(resources.getString(R.string.key_note_sort), resources.getInteger(R.integer.value_note_sort))

    override fun setSort(value: Int) = preference.edit().putInt(resources.getString(R.string.key_note_sort), value).apply()

    override fun getDefaultColor() = preference.getInt(resources.getString(R.string.key_note_color), resources.getInteger(R.integer.value_note_color))

    override fun setDefaultColor(value: Int) = preference.edit().putInt(resources.getString(R.string.key_note_color), value).apply()

    override fun isPauseSaveOn(): Boolean {
        val defaultValue = resources.getBoolean(R.bool.value_save_pause)
        return preference.getBoolean(resources.getString(R.string.key_save_pause), defaultValue)
    }

    override fun isAutoSaveOn(): Boolean {
        val defaultValue = resources.getBoolean(R.bool.value_save_auto)
        return preference.getBoolean(resources.getString(R.string.key_save_auto), defaultValue)
    }

    override fun getSavePeriod() = preference.getInt(resources.getString(R.string.key_save_time), resources.getInteger(R.integer.value_save_time))

    override fun setSavePeriod(value: Int) =
            preference.edit().putInt(resources.getString(R.string.key_save_time), value).apply()

    //endregion

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