package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main

import android.os.Bundle
import sgtmelon.scriptum.cleanup.presentation.control.touch.RankTouchControl
import sgtmelon.scriptum.cleanup.presentation.receiver.screen.MainScreenReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.RankViewModel

/**
 * Interface for communication [IRankFragment] with [RankViewModel].
 */
interface IRankViewModel : IParentViewModel,
    MainScreenReceiver.BindCallback,
    RankTouchControl.Callback {

    fun onSaveData(bundle: Bundle)

    fun onUpdateData()

    fun onUpdateToolbar()

    fun onShowRenameDialog(p: Int)

    fun onResultRenameDialog(p: Int, name: String)


    fun onClickEnterCancel()

    fun onEditorClick(i: Int): Boolean

    fun onClickEnterAdd(simpleClick: Boolean)

    fun onClickVisible(p: Int)

    fun onClickCancel(p: Int)

    fun onItemAnimationFinished()


    fun onSnackbarAction()

    fun onSnackbarDismiss()

}