package sgtmelon.scriptum.data.provider

import android.content.Context
import android.content.res.Resources
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.provider.PreferenceProvider.Def
import sgtmelon.scriptum.data.provider.PreferenceProvider.Key
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo

/**
 * Provider of [Key] and [Def] values for [PreferenceRepo]
 */
@Suppress("PropertyName")
class PreferenceProvider {

    class Key(context: Context) {
        private val resources: Resources = context.resources

        val FIRST_START: String = resources.getString(R.string.pref_key_first_start)
        val THEME: String = resources.getString(R.string.pref_key_app_theme)

        val SORT: String = resources.getString(R.string.pref_key_note_sort)
        val DEFAULT_COLOR: String = resources.getString(R.string.pref_key_note_color)
        val PAUSE_SAVE_ON: String = resources.getString(R.string.pref_key_note_pause)
        val AUTO_SAVE_ON: String = resources.getString(R.string.pref_key_note_auto)
        val SAVE_PERIOD: String = resources.getString(R.string.pref_key_note_time)

        val REPEAT: String = resources.getString(R.string.pref_key_alarm_repeat)
        val SIGNAL: String = resources.getString(R.string.pref_key_alarm_signal)
        val MELODY_URI: String = resources.getString(R.string.pref_key_alarm_melody)
        val VOLUME: String = resources.getString(R.string.pref_key_alarm_volume)
        val VOLUME_INCREASE: String = resources.getString(R.string.pref_key_alarm_increase)
    }

    class Def(context: Context) {
        private val resources: Resources = context.resources

        val FIRST_START = resources.getBoolean(R.bool.pref_first_start)
        val THEME = resources.getInteger(R.integer.pref_app_theme)

        val SORT = resources.getInteger(R.integer.pref_note_sort)
        val DEFAULT_COLOR = resources.getInteger(R.integer.pref_note_color)
        val PAUSE_SAVE_ON = resources.getBoolean(R.bool.pref_note_save_pause)
        val AUTO_SAVE_ON = resources.getBoolean(R.bool.pref_note_save_auto)
        val SAVE_PERIOD = resources.getInteger(R.integer.pref_note_save_time)

        val REPEAT = resources.getInteger(R.integer.pref_alarm_repeat)
        val SIGNAL = resources.getInteger(R.integer.pref_alarm_signal)
        val MELODY_URI: String = resources.getString(R.string.pref_alarm_melody)
        val VOLUME = resources.getInteger(R.integer.pref_alarm_volume)
        val VOLUME_INCREASE = resources.getBoolean(R.bool.pref_alarm_increase)
    }

}