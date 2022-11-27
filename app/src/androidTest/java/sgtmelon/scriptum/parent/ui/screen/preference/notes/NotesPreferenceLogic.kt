package sgtmelon.scriptum.parent.ui.screen.preference.notes

import sgtmelon.scriptum.R
import sgtmelon.scriptum.parent.ui.model.PreferenceItem
import sgtmelon.scriptum.parent.ui.parts.preferences.PreferenceLogic

/**
 * Logic for [NotesPreferenceScreen].
 */
class NotesPreferenceLogic : PreferenceLogic() {

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

    /**
     * Needed for describe order of items.
     */
    enum class Part {
        COMMON_HEADER, SORT_ITEM, COLOR_ITEM,
        SAVE_HEADER, ON_PAUSE_ITEM, ON_AUTO_ItEM, SAVE_PERIOD_ITEM
    }
}