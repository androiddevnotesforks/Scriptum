package sgtmelon.scriptum.presentation.provider


import android.content.res.Resources
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.vm.impl.PreferenceViewModel

/**
 * Provider of summary arrays for [PreferenceViewModel]
 */
class SummaryProvider(private val resources: Resources?) {

    val sort: Array<String>? = resources?.getStringArray(R.array.pref_text_note_sort)
    val color: Array<String>? = resources?.getStringArray(R.array.pref_text_note_color)
    val savePeriod: Array<String>? = resources?.getStringArray(R.array.pref_text_note_save_period)

    val theme: Array<String>? = resources?.getStringArray(R.array.pref_text_app_theme)
    val repeat: Array<String>? = resources?.getStringArray(R.array.pref_text_alarm_repeat)
    val signal: Array<String>? = resources?.getStringArray(R.array.pref_text_alarm_signal)

    fun getVolume(value: Int): String? {
        return resources?.getString(R.string.pref_summary_alarm_volume, value)
    }

}