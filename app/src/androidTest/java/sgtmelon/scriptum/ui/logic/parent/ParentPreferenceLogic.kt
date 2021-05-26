package sgtmelon.scriptum.ui.logic.parent

import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.presentation.provider.SummaryProvider

/**
 *
 */
abstract class ParentPreferenceLogic : ParentLogic() {

    protected val provider = SummaryProvider(context.resources)

    abstract fun getScreenList(): List<PreferenceItem>

}