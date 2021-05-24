package sgtmelon.scriptum.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.ui.dialog.preference.RepeatDialogUi

/**
 * Class for UI control of [AlarmPreferenceFragment].
 */
class AlarmPreferenceScreen : ParentPreferenceScreen(R.string.pref_title_alarm) {

    // TODO finish
    override fun getScreenList(): List<PreferenceItem> {
        return listOf(
            PreferenceItem.Header(R.string.pref_header_common),
            PreferenceItem.Summary(R.string.pref_title_alarm_repeat, provider.repeat[preferenceRepo.repeat])
            //            PreferenceItem.Summary(R.string.pref_title_alarm_signal, provider.signal[preferenceRepo.signal])
            //            PreferenceItem.Header(R.string.pref_header_melody_options),
            //            PreferenceItem.Summary(R.string.pref_title_alarm_signal, provider.signal[preferenceRepo.signal]),
            //            PreferenceItem.Switch(R.string.pref_title_note_save_auto, R.string.pref_summary_note_save_auto, preferenceRepo.autoSaveOn),
            //            PreferenceItem.Summary(R.string.pref_title_note_save_period, provider.savePeriod[preferenceRepo.savePeriod], preferenceRepo.autoSaveOn)
        )
    }

    fun openRepeatDialog(func: RepeatDialogUi.() -> Unit = {}) {
        getItem(p = 1).Summary().onItemClick()
        RepeatDialogUi(func)
    }

    companion object {
        operator fun invoke(func: AlarmPreferenceScreen.() -> Unit): AlarmPreferenceScreen {
            return AlarmPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}