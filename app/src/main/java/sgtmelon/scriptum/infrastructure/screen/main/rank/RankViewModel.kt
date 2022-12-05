package sgtmelon.scriptum.infrastructure.screen.main.rank

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.UpdateListState
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver

interface RankViewModel : UnbindNoteReceiver.Callback {

    val showList: LiveData<ShowListState>

    val itemList: LiveData<List<RankItem>>

    val updateList: UpdateListState

    val showSnackbar: LiveData<Boolean>

    fun updateData()

    fun getToolbarEnable(name: String): Pair<Boolean, Boolean>

    fun addRank(enter: String, toBottom: Boolean): Flow<AddState>

    fun moveRank(from: Int, to: Int)

    fun moveRankResult()

    fun changeRankVisibility(p: Int): Flow<Unit>

    fun getRenameData(p: Int): Flow<Pair<String, List<String>>>

    fun renameRank(p: Int, name: String): Flow<Unit>

    fun removeRank(p: Int): Flow<Unit>

    fun undoRemove(): Flow<Unit>

    fun clearUndoStack()
}