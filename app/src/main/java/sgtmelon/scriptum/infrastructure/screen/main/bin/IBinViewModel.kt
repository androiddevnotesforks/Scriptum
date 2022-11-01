package sgtmelon.scriptum.infrastructure.screen.main.bin

import sgtmelon.scriptum.cleanup.domain.model.annotation.Options
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel

/**
 * Interface for communication [IBinFragment] with [BinViewModelImpl].
 */
interface IBinViewModel : IParentViewModel {

    fun onUpdateData()

    fun onClickClearBin()

    @Deprecated("Move preparation before show dialog inside some delegator, which will call from UI")
    fun onShowOptionsDialog(item: NoteItem, p: Int)

    fun onResultOptionsDialog(p: Int, @Options.Bin which: Int)

}