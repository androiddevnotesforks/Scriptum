package sgtmelon.scriptum.screen.vm.callback.main

import sgtmelon.scriptum.control.touch.RankTouchControl
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interface for communication [RankFragment] with [RankViewModel].
 */
interface IRankViewModel : IParentViewModel, RankTouchControl.Callback {

    fun onUpdateData()

    fun onUpdateToolbar()

    fun onShowRenameDialog(p: Int)

    fun onRenameDialog(p: Int, name: String)


    fun onClickEnterCancel(): String

    fun onEditorClick(i: Int): Boolean

    fun onClickEnterAdd(simpleClick: Boolean)

    fun onClickVisible(p: Int)

    fun onLongClickVisible(p: Int)

    fun onClickCancel(p: Int)

}