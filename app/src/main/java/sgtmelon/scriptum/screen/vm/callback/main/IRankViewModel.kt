package sgtmelon.scriptum.screen.vm.callback.main

import sgtmelon.scriptum.control.touch.RankTouchControl
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Интерфейс для общения [RankFragment] с [RankViewModel]
 *
 * @author SerjantArbuz
 */
interface IRankViewModel : IParentViewModel, RankTouchControl.Result {

    fun onUpdateData()

    fun onUpdateToolbar()

    fun onShowRenameDialog(p: Int)

    fun onRenameDialog(p: Int, name: String)

    fun onClickCancel(): String

    fun onEditorClick(i: Int): Boolean

    fun onClickAdd(simpleClick: Boolean)

    fun onClickVisible(p: Int)

    fun onLongClickVisible(p: Int)

    fun onClickCancel(p: Int)

}