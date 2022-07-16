package sgtmelon.scriptum.infrastructure.preferences.provider

import android.content.SharedPreferences
import android.content.res.Resources
import sgtmelon.scriptum.R

/**
 * Provide default values for returning data from [SharedPreferences].
 */
class PreferencesDefProvider(resources: Resources) {

    val isFirstStart: Boolean = resources.getBoolean(R.bool.pref_first_start)
    val theme: Int = resources.getInteger(R.integer.pref_app_theme)

    val isBackupSkipImports: Boolean = resources.getBoolean(R.bool.pref_backup_import_skip)

    val sort: Int = resources.getInteger(R.integer.pref_note_sort)
    val defaultColor: Int = resources.getInteger(R.integer.pref_note_color)
    val isPauseSaveOn: Boolean = resources.getBoolean(R.bool.pref_note_save_pause)
    val isAutoSaveOn: Boolean = resources.getBoolean(R.bool.pref_note_save_auto)
    val savePeriod: Int = resources.getInteger(R.integer.pref_note_save_time)

    val repeat: Int = resources.getInteger(R.integer.pref_alarm_repeat)
    val signal: String = resources.getString(R.string.pref_alarm_signal)
    val melodyUri: String = resources.getString(R.string.pref_alarm_melody)
    val volume: Int = resources.getInteger(R.integer.pref_alarm_volume)
    val isVolumeIncrease: Boolean = resources.getBoolean(R.bool.pref_alarm_increase)

    val isDeveloper: Boolean = resources.getBoolean(R.bool.pref_other_develop)
}