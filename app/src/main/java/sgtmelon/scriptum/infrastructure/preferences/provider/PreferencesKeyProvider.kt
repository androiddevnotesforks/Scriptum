package sgtmelon.scriptum.infrastructure.preferences.provider

import android.content.SharedPreferences
import android.content.res.Resources
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.preferences.PreferencesImpl

/**
 * Provide keys for getting data from [SharedPreferences].
 */
class PreferencesKeyProvider(resources: Resources) {

    val isFirstStart: String = resources.getString(R.string.pref_key_first_start)
    val theme: String = resources.getString(R.string.pref_key_app_theme)

    val isBackupSkipImports: String = resources.getString(R.string.pref_key_backup_skip)

    val sort: String = resources.getString(R.string.pref_key_note_sort)
    val defaultColor: String = resources.getString(R.string.pref_key_note_color)
    val isPauseSaveOn: String = resources.getString(R.string.pref_key_note_pause)
    val isAutoSaveOn: String = resources.getString(R.string.pref_key_note_auto)
    val savePeriod: String = resources.getString(R.string.pref_key_note_time)

    val repeat: String = resources.getString(R.string.pref_key_alarm_repeat)
    val signal: String = resources.getString(R.string.pref_key_alarm_signal)
    val melodyUri: String = resources.getString(R.string.pref_key_alarm_melody)
    val volume: String = resources.getString(R.string.pref_key_alarm_volume)
    val isVolumeIncrease: String = resources.getString(R.string.pref_key_alarm_increase)

    val isDeveloper: String = resources.getString(R.string.pref_key_other_develop)
}