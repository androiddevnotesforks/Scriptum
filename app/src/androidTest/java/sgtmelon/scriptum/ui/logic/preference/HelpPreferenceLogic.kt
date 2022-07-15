package sgtmelon.scriptum.ui.logic.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.data.item.PreferenceItem
import sgtmelon.scriptum.ui.logic.parent.ParentPreferenceLogic
import sgtmelon.scriptum.ui.screen.preference.help.HelpPreferenceScreen

/**
 * Logic for [HelpPreferenceScreen].
 */
class HelpPreferenceLogic : ParentPreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> = listOf(
        PreferenceItem.Summary.Id(
            R.string.pref_title_help_notification_disappear,
            R.string.pref_summary_help_notification_disappear
        ),
        PreferenceItem.Header(R.string.pref_header_other),
        PreferenceItem.Simple(R.string.pref_title_help_privacy_policy)
    )
}