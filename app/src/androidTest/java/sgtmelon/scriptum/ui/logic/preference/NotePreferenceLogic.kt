package sgtmelon.scriptum.ui.logic.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.ui.logic.parent.ParentPreferenceLogic
import sgtmelon.scriptum.ui.screen.preference.NotePreferenceScreen

/**
 * Logic for [NotePreferenceScreen].
 */
class NotePreferenceLogic : ParentPreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> {
        return listOf(
            PreferenceItem.Header(R.string.pref_header_common),
            PreferenceItem.Summary.Text(
                R.string.pref_title_note_sort,
                provider.sort[appPreferences.sort]
            ),
            PreferenceItem.Summary.Text(
                R.string.pref_title_note_color,
                provider.color[appPreferences.defaultColor]
            ),
            PreferenceItem.Header(R.string.pref_header_save),
            PreferenceItem.Switch(
                R.string.pref_title_note_save_pause,
                R.string.pref_summary_note_save_pause,
                appPreferences.pauseSaveOn
            ),
            PreferenceItem.Switch(
                R.string.pref_title_note_save_auto,
                R.string.pref_summary_note_save_auto,
                appPreferences.autoSaveOn
            ),
            PreferenceItem.Summary.Text(
                R.string.pref_title_note_save_period,
                provider.savePeriod[appPreferences.savePeriod],
                appPreferences.autoSaveOn
            )
        )
    }
}