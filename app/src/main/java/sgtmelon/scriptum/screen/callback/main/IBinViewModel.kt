package sgtmelon.scriptum.screen.callback.main

import sgtmelon.scriptum.screen.view.main.BinFragment
import sgtmelon.scriptum.screen.vm.main.BinViewModel

/**
 * Интерфейс для общения [BinFragment] с [BinViewModel]
 *
 * @author SerjantArbuz
 */
interface IBinViewModel {

    fun onSetup()

    fun onUpdateData()

    fun onClickClearBin()

    fun onClickNote(p: Int)

    fun onShowOptionsDialog(p: Int)

    fun onResultOptionsDialog(p: Int, which: Int)

}