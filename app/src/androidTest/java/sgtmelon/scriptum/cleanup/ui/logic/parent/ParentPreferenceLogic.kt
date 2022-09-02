package sgtmelon.scriptum.cleanup.ui.logic.parent

import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.infrastructure.provider.SummaryDataSourceImpl

/**
 * Parent class for preference screen/test logic.
 */
abstract class ParentPreferenceLogic : ParentLogic() {

    protected val provider = SummaryDataSourceImpl(context.resources)

    abstract fun getScreenList(): List<PreferenceItem>
}