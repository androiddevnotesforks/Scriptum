package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference

import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.INotePreferenceViewModel
import sgtmelon.scriptum.infrastructure.model.key.Sort

/**
 * Interface for communication [INotePreferenceViewModel] with [NotePreferenceFragment].
 */
interface INotePreferenceFragment : SystemReceiver.Bridge.Bind {

    fun setup()

    fun updateSortSummary(summary: String)

    fun showSortDialog(value: Sort)

    fun updateColorSummary(summary: String?)

    fun showColorDialog(@Color color: Int)

    fun updateSavePeriodSummary(summary: String?)

    fun showSaveTimeDialog(value: Int)

}