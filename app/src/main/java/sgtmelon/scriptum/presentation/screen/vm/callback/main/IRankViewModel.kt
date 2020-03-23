package sgtmelon.scriptum.presentation.screen.vm.callback.main

import sgtmelon.scriptum.control.touch.RankTouchControl
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.main.RankViewModel

/**
 * Interface for communication [RankFragment] with [RankViewModel].
 */
interface IRankViewModel : IParentViewModel, RankTouchControl.Callback {

    fun onUpdateData()

    fun onUpdateToolbar()

    fun onShowRenameDialog(p: Int)

    fun onResultRenameDialog(p: Int, name: String)


    fun onClickEnterCancel()

    fun onEditorClick(i: Int): Boolean

    fun onClickEnterAdd(simpleClick: Boolean)

    fun onClickVisible(p: Int)

    fun onLongClickVisible(p: Int)

    fun onClickCancel(p: Int)

}