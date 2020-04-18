package sgtmelon.scriptum.presentation.provider

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.vm.impl.PreferenceViewModel

/**
 * Provider of summary arrays for [PreferenceViewModel]
 */
class SummaryProvider(private val context: Context?) {

    val sort: Array<String>? = context?.resources?.getStringArray(R.array.pref_text_note_sort)
    val color: Array<String>? = context?.resources?.getStringArray(R.array.pref_text_note_color)
    val savePeriod: Array<String>? = context?.resources?.getStringArray(R.array.pref_text_note_save_period)

    val theme: Array<String>? = context?.resources?.getStringArray(R.array.pref_text_app_theme)
    val repeat: Array<String>? = context?.resources?.getStringArray(R.array.pref_text_alarm_repeat)
    val signal: Array<String>? = context?.resources?.getStringArray(R.array.pref_text_alarm_signal)

    fun getVolume(value: Int): String? {
        return context?.resources?.getString(R.string.pref_summary_alarm_volume, value)
    }

}