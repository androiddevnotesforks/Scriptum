package sgtmelon.scriptum.cleanup.ui.logic.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.cleanup.ui.screen.preference.help.HelpPreferenceScreen
import sgtmelon.scriptum.parent.ui.parts.preferences.PreferenceLogic

/**
 * Logic for [HelpPreferenceScreen].
 */
class HelpPreferenceLogic : PreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> = listOf(
        PreferenceItem.Summary.Id(
            R.string.pref_title_help_disappear,
            R.string.pref_summary_help_disappear
        ),
        PreferenceItem.Header(R.string.pref_header_other),
        PreferenceItem.Simple(R.string.pref_title_policy)
    )
}