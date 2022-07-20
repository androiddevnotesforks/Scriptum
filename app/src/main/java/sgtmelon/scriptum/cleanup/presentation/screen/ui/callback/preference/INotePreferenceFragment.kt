package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference

import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.INotePreferenceViewModel
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.Sort

/**
 * Interface for communication [INotePreferenceViewModel] with [NotePreferenceFragment].
 */
interface INotePreferenceFragment : SystemReceiver.Bridge.Bind {

    fun setup()

    fun updateSortSummary(summary: String)

    fun showSortDialog(sort: Sort)

    fun updateColorSummary(summary: String)

    fun showColorDialog(color: Color)

    fun updateSavePeriodSummary(summary: String?)

    fun showSaveTimeDialog(savePeriod: SavePeriod)

}