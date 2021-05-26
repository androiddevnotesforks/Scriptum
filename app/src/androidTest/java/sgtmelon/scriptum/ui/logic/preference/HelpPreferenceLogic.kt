package sgtmelon.scriptum.ui.logic.preference

import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.ui.logic.parent.ParentPreferenceLogic
import sgtmelon.scriptum.ui.screen.preference.HelpPreferenceScreen

/**
 * Logic for [HelpPreferenceScreen].
 */
class HelpPreferenceLogic : ParentPreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> {
        return emptyList()
    }
}