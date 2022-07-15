package sgtmelon.scriptum.ui.logic.preference

import sgtmelon.scriptum.cleanup.data.item.PreferenceItem
import sgtmelon.scriptum.ui.logic.parent.ParentPreferenceLogic
import sgtmelon.scriptum.ui.screen.preference.DeveloperScreen

/**
 * Logic for [DeveloperScreen].
 */
class DeveloperLogic : ParentPreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> {
        return emptyList()
    }
}