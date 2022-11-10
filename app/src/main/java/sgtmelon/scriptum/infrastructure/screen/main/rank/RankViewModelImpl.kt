package sgtmelon.scriptum.infrastructure.screen.main.rank

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.math.max
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowOnBack
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.runMain
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.clearSpace
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.cleanup.extension.removeAtOrNull
import sgtmelon.scriptum.domain.useCase.rank.CorrectRankPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.DeleteRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankListUseCase
import sgtmelon.scriptum.domain.useCase.rank.InsertRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankUseCase
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.UpdateListState
import sgtmelon.scriptum.infrastructure.utils.recordException
import sgtmelon.test.idling.getIdling

class RankViewModelImpl(
    private val getList: GetRankListUseCase,
    private val insertRank: InsertRankUseCase,
    private val deleteRank: DeleteRankUseCase,
    private val updateRank: UpdateRankUseCase,
    private val correctRankPositions: CorrectRankPositionsUseCase,
    private val updateRankPositions: UpdateRankPositionsUseCase
) : ViewModel(),
    RankViewModel {

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    private fun notifyShowList() {
        val state = showList.value ?: return
        val newState = if (_itemList.isEmpty()) ShowListState.Empty else ShowListState.List

        /** Skip same state. */
        if (state != newState) {
            showList.postValue(newState)
        }
    }

    override val itemList: MutableLiveData<List<RankItem>> = MutableLiveData()

    private val _itemList: MutableList<RankItem> = mutableListOf()
    private val uniqueNameList: List<String> get() = _itemList.map { it.name.uppercase() }

    // TODO make this variable common (see also in NotificationsViewModelImpl)
    /** Variable for specific list updates (when need update not all items). */
    override var updateList: UpdateListState = UpdateListState.Notify
        get() {
            val value = field
            updateList = UpdateListState.Notify
            return value
        }

    override val showSnackbar: MutableLiveData<Boolean> = MutableLiveData(false)

    /** List which temporary save canceled items for snackbar work. */
    private val undoList: MutableList<Pair<Int, RankItem>> = mutableListOf()

    override fun updateData() {
        viewModelScope.launchBack {
            getIdling().start(IdlingTag.Notes.LOAD_DATA)

            val list = getList()
            _itemList.clearAdd(list)
            itemList.postValue(list)
            notifyShowList()

            getIdling().stop(IdlingTag.Notes.LOAD_DATA)
        }
    }

    override fun getToolbarEnable(name: String): Pair<Boolean, Boolean> {
        val clearName = name.clearSpace().uppercase()

        val isClearEnable = name.isNotEmpty()
        val isAddEnable = clearName.isNotEmpty() && !uniqueNameList.contains(clearName)

        return isClearEnable to isAddEnable
    }

    private fun isValidName(name: String): Boolean {
        return name.isNotEmpty() && !uniqueNameList.contains(name.uppercase())
    }

    override fun addRank(enter: String, toBottom: Boolean): Flow<AddState> = flowOnBack {
        val name = enter.clearSpace()

        if (!isValidName(name)) {
            emit(AddState.Deny)
            return@flowOnBack
        }

        emit(AddState.Prepare)

        val item = insertRank(name) ?: run {
            recordException("isValidName=${isValidName(name)} and can't insert rank by name")
            emit(AddState.Complete)
            return@flowOnBack
        }

        val p = if (toBottom) _itemList.size else 0
        _itemList.add(p, item)
        updateRankPositions(_itemList, correctRankPositions(_itemList))

        updateList = UpdateListState.chooseInsert(_itemList.size, p)
        itemList.postValue(_itemList)
        notifyShowList()

        emit(AddState.Complete)
    }

    override fun moveRank(from: Int, to: Int) {
        _itemList.move(from, to)
        updateList = UpdateListState.Move(from, to)
        itemList.value = _itemList
    }

    override fun moveRankResult() {
        val noteIdList = correctRankPositions(_itemList)

        itemList.postValue(_itemList)

        viewModelScope.launchBack {
            updateRankPositions(_itemList, noteIdList)
        }
    }

    override fun changeRankVisibility(p: Int): Flow<Unit> = flowOnBack {
        val item = _itemList.getOrNull(p) ?: return@flowOnBack

        item.isVisible = !item.isVisible

        updateList = UpdateListState.Set
        itemList.postValue(_itemList)

        updateRank(item)

        emit(Unit)
    }

    override fun getRenameData(p: Int): Flow<Pair<String, List<String>>> = flowOnBack {
        val item = _itemList.getOrNull(p) ?: return@flowOnBack
        emit(value = item.name to uniqueNameList)
    }

    override fun renameRank(p: Int, name: String): Flow<Unit> = flowOnBack {
        val item = _itemList.getOrNull(p) ?: return@flowOnBack

        item.name = name
        updateRank(item)
        itemList.postValue(_itemList)

        emit(Unit)
    }

    override fun removeRank(p: Int): Flow<Unit> = flowOnBack {
        val item = _itemList.removeAtOrNull(p) ?: return@flowOnBack
        val noteIdList = correctRankPositions(_itemList)

        /** Save item for snackbar undo action. */
        undoList.add(Pair(p, item))
        showSnackbar.postValue(true)

        updateList = UpdateListState.Remove(p)
        itemList.postValue(_itemList)
        notifyShowList()

        deleteRank(item)
        updateRankPositions(_itemList, noteIdList)

        emit(Unit)
    }

    override fun undoRemove(): Flow<Unit> = flowOnBack {
        if (undoList.isEmpty()) return@flowOnBack

        val pair = undoList.removeAtOrNull(index = undoList.lastIndex) ?: return@flowOnBack
        val item = pair.second

        /**
         * Check item position correct, just in case.
         * List size after adding item, will be last index.
         */
        val isCorrect = pair.first in _itemList.indices
        val position = if (isCorrect) pair.first else _itemList.size
        _itemList.add(position, item)

        updateList = UpdateListState.chooseInsert(_itemList.size, position)

        /** Need set list value on mainThread for prevent postValue overriding. */
        runMain { itemList.value = _itemList }
        notifyShowList()

        /** Show/hide snackbar for next item. */
        showSnackbar.postValue(undoList.isNotEmpty())

        /**
         * After insert don't need update item in list (due to item already have id).
         */
        insertRank(item)
        updateRankPositions(_itemList, correctRankPositions(_itemList))

        updateList = UpdateListState.Set

        /** Need set list value on mainThread for prevent postValue overriding. */
        runMain { itemList.value = _itemList }

        emit(Unit)
    }

    override fun clearUndoStack() {
        undoList.clear()
        showSnackbar.postValue(false)
    }

    override fun onReceiveUnbindNote(noteId: Long) {
        viewModelScope.launchBack {
            for (item in _itemList) {
                if (!item.noteId.contains(noteId)) continue

                /** Decrement [RankItem.bindCount] without db call. */
                item.bindCount = max(a = 0, b = item.bindCount - 1)

                /** Notes may have only one category and it mean what we can stop "for". */
                break
            }

            itemList.postValue(_itemList)
        }
    }
}