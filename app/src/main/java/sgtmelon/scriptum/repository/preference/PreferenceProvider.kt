package sgtmelon.scriptum.repository.preference

import android.content.Context
import android.content.res.Resources
import sgtmelon.scriptum.R

class PreferenceProvider {

    class Key(context: Context) {
        private val resources: Resources = context.resources

        val firstStart: String = resources.getString(R.string.key_first_start)
        val theme: String = resources.getString(R.string.key_app_theme)
        val repeat: String = resources.getString(R.string.key_alarm_repeat)
        val signal: String = resources.getString(R.string.key_alarm_signal)
        val melodyUri: String = resources.getString(R.string.key_alarm_melody)
        val volume: String = resources.getString(R.string.key_alarm_volume)
        val volumeIncrease: String = resources.getString(R.string.key_alarm_increase)
        val sort: String = resources.getString(R.string.key_note_sort)
        val defaultColor: String = resources.getString(R.string.key_note_color)
        val pauseSaveOn: String = resources.getString(R.string.key_save_pause)
        val autoSaveOn: String = resources.getString(R.string.key_save_auto)
        val savePeriod: String = resources.getString(R.string.key_save_time)
    }

    class Def(context: Context) {
        private val resources: Resources = context.resources

        val firstStart = resources.getBoolean(R.bool.value_first_start)
        val theme = resources.getInteger(R.integer.value_app_theme)
        val repeat = resources.getInteger(R.integer.value_alarm_repeat)
        val signal = resources.getInteger(R.integer.value_alarm_signal)
        val melodyUri: String = resources.getString(R.string.value_alarm_melody)
        val volume = resources.getInteger(R.integer.value_alarm_volume)
        val volumeIncrease = resources.getBoolean(R.bool.value_alarm_increase)
        val sort = resources.getInteger(R.integer.value_note_sort)
        val defaultColor = resources.getInteger(R.integer.value_note_color)
        val pauseSaveOn = resources.getBoolean(R.bool.value_save_pause)
        val autoSaveOn = resources.getBoolean(R.bool.value_save_auto)
        val savePeriod = resources.getInteger(R.integer.value_save_time)
    }

}