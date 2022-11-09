package sgtmelon.scriptum.infrastructure.screen.main.rank

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.presentation.control.touch.RankTouchControl
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.main.rank.state.UpdateListState

interface RankViewModel : UnbindNoteReceiver.Callback,
    RankTouchControl.Callback {

    val showList: LiveData<ShowListState>

    val itemList: LiveData<List<RankItem>>

    val updateList: UpdateListState

    val showSnackbar: LiveData<Boolean>

    fun updateData()

    // TODO add features

    fun changeRankVisibility(p: Int): Flow<Unit>

    fun getRenameData(p: Int): Flow<Pair<String, List<String>>>

    fun removeRank(p: Int): Flow<Unit>

    fun undoRemove(): Flow<Unit>

    fun clearUndoStack()


    //    fun onSaveData(bundle: Bundle)
    //
    //    fun onUpdateData()
    //
    //    fun onUpdateToolbar()
    //
    //    fun onShowRenameDialog(p: Int)
    //
    //    fun onResultRenameDialog(p: Int, name: String)
    //
    //
    //    fun onClickEnterCancel()
    //
    //    fun onEditorClick(i: Int): Boolean
    //
    //    fun onClickEnterAdd(addToBottom: Boolean)
    //
    //    fun onClickVisible(p: Int)
    //
    //    fun onClickCancel(p: Int)
    //
    //    fun onItemAnimationFinished()
    //
    //
    //    fun onSnackbarAction()
    //
    //    fun onSnackbarDismiss()

}