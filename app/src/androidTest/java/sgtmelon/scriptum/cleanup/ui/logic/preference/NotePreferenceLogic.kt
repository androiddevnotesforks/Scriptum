package sgtmelon.scriptum.cleanup.ui.logic.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.cleanup.ui.logic.parent.ParentPreferenceLogic
import sgtmelon.scriptum.cleanup.ui.screen.preference.NotePreferenceScreen

/**
 * Logic for [NotePreferenceScreen].
 */
class NotePreferenceLogic : ParentPreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> {
        return listOf(
            PreferenceItem.Header(R.string.pref_header_common),
            PreferenceItem.Summary.Text(
                R.string.pref_title_note_sort,
                provider.getSort(preferencesRepo.sort)
            ),
            PreferenceItem.Summary.Text(
                R.string.pref_title_note_color,
                provider.getColor(preferencesRepo.defaultColor)
            ),
            PreferenceItem.Header(R.string.pref_header_save),
            PreferenceItem.Switch(
                R.string.pref_title_note_save_pause,
                R.string.pref_summary_note_save_pause,
                preferences.isPauseSaveOn
            ),
            PreferenceItem.Switch(
                R.string.pref_title_note_save_auto,
                R.string.pref_summary_note_save_auto,
                preferences.isAutoSaveOn
            ),
            PreferenceItem.Summary.Text(
                R.string.pref_title_note_save_period,
                provider.getSavePeriod(preferencesRepo.savePeriod),
                preferences.isAutoSaveOn
            )
        )
    }
}