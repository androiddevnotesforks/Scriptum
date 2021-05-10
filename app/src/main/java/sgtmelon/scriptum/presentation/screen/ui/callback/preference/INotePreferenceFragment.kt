package sgtmelon.scriptum.presentation.screen.ui.callback.preference

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.INotePreferenceViewModel

/**
 * Interface for communication [INotePreferenceViewModel] with [NotePreferenceFragment].
 */
interface INotePreferenceFragment : SystemReceiver.Bridge.Bind {

    fun setup()

    fun updateSortSummary(summary: String?)

    fun showSortDialog(@Sort value: Int)

    fun updateColorSummary(summary: String?)

    fun showColorDialog(@Color color: Int)

    fun updateSavePeriodSummary(summary: String?)

    fun showSaveTimeDialog(value: Int)

}