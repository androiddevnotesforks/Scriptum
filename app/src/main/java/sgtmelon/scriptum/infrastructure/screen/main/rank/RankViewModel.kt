package sgtmelon.scriptum.infrastructure.screen.main.rank

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.parent.list.InfoListViewModel

interface RankViewModel : InfoListViewModel<RankItem>,
    UnbindNoteReceiver.Callback {

    val showSnackbar: LiveData<Boolean>

    fun updateData()

    fun getToolbarEnable(name: String): Pair<Boolean, Boolean>

    fun addItem(enter: String, toBottom: Boolean): Flow<AddState>

    fun moveItem(from: Int, to: Int)

    fun moveItemResult()

    fun changeVisibility(position: Int): Flow<Unit>

    fun getRenameData(position: Int): Flow<Pair<String, List<String>>>

    fun renameItem(position: Int, name: String): Flow<Unit>

    fun removeItem(position: Int): Flow<Unit>

    fun undoRemove(): Flow<Unit>

    fun clearUndoStack()
}