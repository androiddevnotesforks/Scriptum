package sgtmelon.scriptum.cleanup.ui.logic.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.cleanup.ui.logic.parent.PreferenceLogic
import sgtmelon.scriptum.cleanup.ui.screen.preference.MenuPreferenceScreen

/**
 * Logic for [MenuPreferenceScreen].
 */
class MenuPreferenceLogic : PreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf(
            PreferenceItem.Header(R.string.pref_header_app),
            PreferenceItem.Summary.Text(
                R.string.pref_title_app_theme,
                provider.getTheme(preferencesRepo.theme)
            ),
            PreferenceItem.Simple(R.string.pref_title_backup),
            PreferenceItem.Simple(R.string.pref_title_note),
            PreferenceItem.Simple(R.string.pref_title_alarm),
            PreferenceItem.Header(R.string.pref_header_other),
            PreferenceItem.Simple(R.string.pref_title_rate),
            PreferenceItem.Simple(R.string.pref_title_help),
            PreferenceItem.Simple(R.string.pref_title_about)
        )

        if (preferencesRepo.isDeveloper) {
            list.add(PreferenceItem.Simple(R.string.pref_title_developer))
        }

        return list
    }
}