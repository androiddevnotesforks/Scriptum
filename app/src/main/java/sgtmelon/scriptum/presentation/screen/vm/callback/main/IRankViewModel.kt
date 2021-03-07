package sgtmelon.scriptum.presentation.screen.vm.callback.main

import android.os.Bundle
import sgtmelon.scriptum.presentation.control.touch.RankTouchControl
import sgtmelon.scriptum.presentation.receiver.MainReceiver
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.main.RankViewModel

/**
 * Interface for communication [IRankFragment] with [RankViewModel].
 */
interface IRankViewModel : IParentViewModel,
        MainReceiver.BindCallback,
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

    fun onLongClickVisible(p: Int)

    fun onClickCancel(p: Int)

    fun onItemAnimationFinished()


    fun onSnackbarAction()

    fun onSnackbarDismiss()

}