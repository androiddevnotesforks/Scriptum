package sgtmelon.scriptum.source.ui.parts.preferences

import sgtmelon.scriptum.infrastructure.system.dataSource.SummaryDataSourceImpl
import sgtmelon.scriptum.source.ui.model.PreferenceItem
import sgtmelon.scriptum.source.ui.parts.UiPart

/**
 * Parent class for preference screen/test logic.
 */
abstract class PreferenceLogic : UiPart() {

    protected val summary = SummaryDataSourceImpl(context.resources)

    abstract fun getScreenList(): List<PreferenceItem>
}