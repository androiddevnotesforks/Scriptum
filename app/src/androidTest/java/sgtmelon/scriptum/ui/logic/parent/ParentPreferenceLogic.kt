package sgtmelon.scriptum.ui.logic.parent

import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.infrastructure.provider.SummaryProvider

/**
 * Parent class for preference screen/test logic.
 */
abstract class ParentPreferenceLogic : ParentLogic() {

    protected val provider = SummaryProvider(context.resources)

    abstract fun getScreenList(): List<PreferenceItem>
}