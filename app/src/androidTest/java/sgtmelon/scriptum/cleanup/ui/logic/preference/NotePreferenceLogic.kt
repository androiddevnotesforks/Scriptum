package sgtmelon.scriptum.cleanup.ui.logic.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.cleanup.ui.screen.preference.NotePreferenceScreen
import sgtmelon.scriptum.parent.ui.parts.preferences.PreferenceLogic

/**
 * Logic for [NotePreferenceScreen].
 */
class NotePreferenceLogic : PreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> {
        return listOf(
            PreferenceItem.Header(R.string.pref_header_common),
            PreferenceItem.Summary.Text(
                R.string.pref_title_note_sort,
                summary.getSort(preferencesRepo.sort)
            ),
            PreferenceItem.Summary.Text(
                R.string.pref_title_note_color,
                summary.getColor(preferencesRepo.defaultColor)
            ),
            PreferenceItem.Header(R.string.pref_header_save),
            PreferenceItem.Switch(
                R.string.pref_title_note_pause_save,
                R.string.pref_summary_note_save_pause,
                preferencesRepo.saveState.isPauseSaveOn
            ),
            PreferenceItem.Switch(
                R.string.pref_title_note_auto_save,
                R.string.pref_summary_note_save_auto,
                preferencesRepo.saveState.isAutoSaveOn
            ),
            PreferenceItem.Summary.Text(
                R.string.pref_title_note_save_period,
                summary.getSavePeriod(preferencesRepo.savePeriod),
                preferencesRepo.saveState.isAutoSaveOn
            )
        )
    }
}