package sgtmelon.scriptum.data.provider

import android.content.res.Resources
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.provider.PreferenceProvider.Def
import sgtmelon.scriptum.data.provider.PreferenceProvider.Key
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo

/**
 * Provider of [Key] and [Def] values for [PreferenceRepo].
 */
class PreferenceProvider {

    class Key(resources: Resources) {
        val firstStart: String = resources.getString(R.string.pref_key_first_start)
        val theme: String = resources.getString(R.string.pref_key_app_theme)

        val importSkip: String = resources.getString(R.string.pref_key_backup_import_skip)

        val sort: String = resources.getString(R.string.pref_key_note_sort)
        val defaultColor: String = resources.getString(R.string.pref_key_note_color)
        val pauseSaveOn: String = resources.getString(R.string.pref_key_note_pause)
        val autoSaveOn: String = resources.getString(R.string.pref_key_note_auto)
        val savePeriod: String = resources.getString(R.string.pref_key_note_time)

        val repeat: String = resources.getString(R.string.pref_key_alarm_repeat)
        val signal: String = resources.getString(R.string.pref_key_alarm_signal)
        val melodyUri: String = resources.getString(R.string.pref_key_alarm_melody)
        val volume: String = resources.getString(R.string.pref_key_alarm_volume)
        val volumeIncrease: String = resources.getString(R.string.pref_key_alarm_increase)
    }

    class Def(resources: Resources) {
        val firstStart: Boolean = resources.getBoolean(R.bool.pref_first_start)
        val theme: Int = resources.getInteger(R.integer.pref_app_theme)

        val importSkip: Boolean = resources.getBoolean(R.bool.pref_backup_import_skip)

        val sort: Int = resources.getInteger(R.integer.pref_note_sort)
        val defaultColor: Int = resources.getInteger(R.integer.pref_note_color)
        val pauseSaveOn: Boolean = resources.getBoolean(R.bool.pref_note_save_pause)
        val autoSaveOn: Boolean = resources.getBoolean(R.bool.pref_note_save_auto)
        val savePeriod: Int = resources.getInteger(R.integer.pref_note_save_time)

        val repeat: Int = resources.getInteger(R.integer.pref_alarm_repeat)
        val signal: Int = resources.getInteger(R.integer.pref_alarm_signal)
        val melodyUri: String = resources.getString(R.string.pref_alarm_melody)
        val volume: Int = resources.getInteger(R.integer.pref_alarm_volume)
        val volumeIncrease: Boolean = resources.getBoolean(R.bool.pref_alarm_increase)
    }

}