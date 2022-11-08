package sgtmelon.scriptum.infrastructure.screen.main.rank

import android.os.Bundle
import sgtmelon.scriptum.cleanup.presentation.control.touch.RankTouchControl
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver

/**
 * Interface for communication [IRankFragment] with [RankViewModelImpl].
 */
interface RankViewModel : IParentViewModel,
    UnbindNoteReceiver.Callback,
    RankTouchControl.Callback {

    fun onSaveData(bundle: Bundle)

    fun onUpdateData()

    fun onUpdateToolbar()

    fun onShowRenameDialog(p: Int)

    fun onResultRenameDialog(p: Int, name: String)


    fun onClickEnterCancel()

    fun onEditorClick(i: Int): Boolean

    fun onClickEnterAdd(addToBottom: Boolean)

    fun onClickVisible(p: Int)

    fun onClickCancel(p: Int)

    fun onItemAnimationFinished()


    fun onSnackbarAction()

    fun onSnackbarDismiss()

}