package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main

import sgtmelon.scriptum.cleanup.domain.model.annotation.Options
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.IBinFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.BinViewModel

/**
 * Interface for communication [IBinFragment] with [BinViewModel].
 */
interface IBinViewModel : IParentViewModel {

    fun onUpdateData()

    fun onClickClearBin()

    fun onClickNote(p: Int)

    fun onShowOptionsDialog(p: Int)

    fun onResultOptionsDialog(p: Int, @Options.Bin which: Int)

}