package sgtmelon.scriptum.cleanup.ui.logic.preference

import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.cleanup.ui.logic.parent.ParentPreferenceLogic
import sgtmelon.scriptum.cleanup.ui.screen.preference.DeveloperScreen

/**
 * Logic for [DeveloperScreen].
 */
class DeveloperLogic : ParentPreferenceLogic() {

    override fun getScreenList(): List<PreferenceItem> {
        return emptyList()
    }
}