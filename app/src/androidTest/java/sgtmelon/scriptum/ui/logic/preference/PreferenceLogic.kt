package sgtmelon.scriptum.ui.logic.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.ui.logic.parent.ParentPreferenceLogic
import sgtmelon.scriptum.ui.screen.preference.PreferenceScreen

/**
 * Logic for [PreferenceScreen].
 */
class PreferenceLogic : ParentPreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf(
            PreferenceItem.Header(R.string.pref_header_app),
            PreferenceItem.Summary.Text(
                R.string.pref_title_app_theme,
                provider.theme[preferenceRepo.theme]
            ),
            PreferenceItem.Simple(R.string.pref_title_backup),
            PreferenceItem.Simple(R.string.pref_title_note),
            PreferenceItem.Simple(R.string.pref_title_alarm),
            PreferenceItem.Header(R.string.pref_header_other),
            PreferenceItem.Simple(R.string.pref_title_other_rate),
            PreferenceItem.Simple(R.string.pref_title_other_help),
            PreferenceItem.Simple(R.string.pref_title_other_about)
        )

        if (preferenceRepo.isDeveloper) {
            list.add(PreferenceItem.Simple(R.string.pref_title_other_develop))
        }

        return list
    }
}