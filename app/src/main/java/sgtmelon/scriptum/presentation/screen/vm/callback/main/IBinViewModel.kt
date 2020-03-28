package sgtmelon.scriptum.presentation.screen.vm.callback.main

import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.main.BinViewModel

/**
 * Interface for communication [BinFragment] with [BinViewModel]
 */
interface IBinViewModel : IParentViewModel {

    fun onUpdateData()

    fun onClickClearBin()

    fun onClickNote(p: Int)

    fun onShowOptionsDialog(p: Int)

    fun onResultOptionsDialog(p: Int, which: Int)

}