package sgtmelon.scriptum.parent.ui.parts.preferences

import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.infrastructure.system.dataSource.SummaryDataSourceImpl
import sgtmelon.scriptum.parent.ui.parts.UiPart

/**
 * Parent class for preference screen/test logic.
 */
abstract class PreferenceLogic : UiPart() {

    protected val summary = SummaryDataSourceImpl(context.resources)

    abstract fun getScreenList(): List<PreferenceItem>
}