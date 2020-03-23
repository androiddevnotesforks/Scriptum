package sgtmelon.scriptum.provider

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.vm.impl.PreferenceViewModel

/**
 * Provider of summary arrays for [PreferenceViewModel]
 */
class SummaryProvider(context: Context) {

    val sort: Array<String> = context.resources.getStringArray(R.array.pref_text_note_sort)
    val color: Array<String> = context.resources.getStringArray(R.array.pref_text_note_color)
    val saveTime: Array<String> = context.resources.getStringArray(R.array.pref_text_note_save_time)

    val theme: Array<String> = context.resources.getStringArray(R.array.pref_text_app_theme)
    val repeat: Array<String> = context.resources.getStringArray(R.array.pref_text_alarm_repeat)
    val signal: Array<String> = context.resources.getStringArray(R.array.pref_text_alarm_signal)

}