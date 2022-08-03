package sgtmelon.scriptum.cleanup.ui.logic.parent

import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.infrastructure.provider.SummaryProviderImpl

/**
 * Parent class for preference screen/test logic.
 */
abstract class ParentPreferenceLogic : ParentLogic() {

    protected val provider = SummaryProviderImpl(context.resources)

    abstract fun getScreenList(): List<PreferenceItem>
}