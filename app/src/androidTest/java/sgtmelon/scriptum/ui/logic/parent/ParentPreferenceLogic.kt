package sgtmelon.scriptum.ui.logic.parent

import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.data.item.PreferenceItem

/**
 * Parent class for preference screen/test logic.
 */
abstract class ParentPreferenceLogic : ParentLogic() {

    protected val provider = SummaryProvider(context.resources)

    abstract fun getScreenList(): List<PreferenceItem>
}