package sgtmelon.scriptum.screen.vm.callback.main

import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.main.BinViewModel

/**
 * Interface for communication [BinFragment] with [BinViewModel]
 *
 * @author SerjantArbuz
 */
interface IBinViewModel : IParentViewModel {

    fun onSetup()

    fun onUpdateData()

    fun onClickClearBin()

    fun onClickNote(p: Int)

    fun onShowOptionsDialog(p: Int)

    fun onResultOptionsDialog(p: Int, which: Int)

}