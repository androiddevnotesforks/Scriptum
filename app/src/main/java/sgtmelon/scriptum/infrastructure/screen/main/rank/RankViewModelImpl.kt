package sgtmelon.scriptum.infrastructure.screen.main.rank

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowBack
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.removeExtraSpace
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.domain.useCase.rank.CorrectRankPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.DeleteRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankListUseCase
import sgtmelon.scriptum.domain.useCase.rank.InsertRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankUseCase
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListStorageImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAdd
import sgtmelon.scriptum.infrastructure.utils.extensions.recordException
import sgtmelon.scriptum.infrastructure.utils.extensions.removeAtOrNull
import kotlin.math.max

class RankViewModelImpl(
    override val list: ListStorageImpl<RankItem>,
    private val getList: GetRankListUseCase,
    private val insertRank: InsertRankUseCase,
    private val deleteRank: DeleteRankUseCase,
    private val updateRank: UpdateRankUseCase,
    private val correctRankPositions: CorrectRankPositionsUseCase,
    private val updateRankPositions: UpdateRankPositionsUseCase
) : ViewModel(),
    RankViewModel {

    private val uniqueNameList: List<String> get() = list.localData.map { it.name.uppercase() }

    override val showSnackbar: MutableLiveData<Boolean> = MutableLiveData(false)

    /** List which temporary save canceled items for snackbar work. */
    private val undoList: MutableList<Pair<Int, RankItem>> = mutableListOf()

    override fun updateData() {
        viewModelScope.launchBack {
            list.change { it.clearAdd(getList()) }
        }
    }

    override fun getToolbarEnable(name: String): Pair<Boolean, Boolean> {
        val clearName = name.removeExtraSpace().uppercase()

        val isClearEnable = name.isNotEmpty()
        val isAddEnable = clearName.isNotEmpty() && !uniqueNameList.contains(clearName)

        return isClearEnable to isAddEnable
    }

    private fun isValidName(name: String): Boolean {
        return name.isNotEmpty() && !uniqueNameList.contains(name.uppercase())
    }

    override fun addItem(enter: String, toBottom: Boolean): Flow<AddState> = flowBack {
        val name = enter.removeExtraSpace()

        if (!isValidName(name)) {
            emit(AddState.Deny)
            return@flowBack
        }

        emit(AddState.Prepare)

        val item = insertRank(name) ?: run {
            recordException("isValidName=${isValidName(name)} and can't insert rank by name")
            emit(AddState.Complete)
            return@flowBack
        }

        val noteIdList = list.change {
            val p = if (toBottom) it.size else 0
            it.add(p, item)
            list.update = UpdateListState.Insert(p)

            /** Inside will be updated data about positions. */
            return@change correctRankPositions(it)
        }

        updateRankPositions(list.localData, noteIdList)

        emit(AddState.Complete)
    }

    @MainThread
    override fun moveItem(from: Int, to: Int) = list.move(from, to)

    override fun moveItemResult() {
        /** Need just set for skip "diff notify" animation and crashes related with that. */
        val noteIdList = list.change(UpdateListState.Set) { correctRankPositions(it) }

        viewModelScope.launchBack {
            updateRankPositions(list.localData, noteIdList)
        }
    }

    override fun changeVisibility(position: Int): Flow<RankItem> = flowBack {
        val item = list.change(UpdateListState.Set) {
            val item = it.getOrNull(position) ?: return@flowBack
            item.isVisible = !item.isVisible
            return@change item
        }

        updateRank(item)
        emit(item)
    }

    override fun getRenameData(position: Int): Flow<Pair<String, List<String>>> = flowBack {
        val item = list.localData.getOrNull(position) ?: return@flowBack
        emit(value = item.name to uniqueNameList)
    }

    override fun renameItem(position: Int, name: String): Flow<Unit> = flowBack {
        list.change {
            val item = it.getOrNull(position) ?: return@flowBack
            item.name = name
            updateRank(item)
        }

        emit(Unit)
    }

    override fun removeItem(position: Int): Flow<Unit> = flowBack {
        val (item, noteIdList) = list.change(UpdateListState.Remove(position)) {
            val item = it.removeAtOrNull(position) ?: return@flowBack

            /** Inside will be updated data about positions. */
            return@change item to correctRankPositions(it)
        }

        /** Save item for snackbar undo action and display it. */
        undoList.add(Pair(position, item))
        showSnackbar.postValue(true)

        deleteRank(item)
        updateRankPositions(list.localData, noteIdList)

        emit(Unit)
    }

    override fun undoRemove(): Flow<Unit> = flowBack {
        if (undoList.isEmpty()) return@flowBack

        val pair = undoList.removeAtOrNull(index = undoList.lastIndex) ?: return@flowBack
        val item = pair.second

        /** Need set list value on mainThread for prevent postValue overriding. */
        list.changeNext {
            /** Check item position correct, just in case. */
            val isCorrect = pair.first in it.indices
            /** List size after adding item, will be last index. */
            val position = if (isCorrect) pair.first else it.size
            it.add(position, item)

            list.update = UpdateListState.Insert(position)
        }

        /** Show/hide snackbar for next item. */
        showSnackbar.postValue(undoList.isNotEmpty())

        /** After insert don't need update item in list (due to item already have id). */
        insertRank(item)

        /** Need set list value on mainThread for prevent postValue overriding. */
        val noteIdList = list.changeNext(UpdateListState.Set) {
            correctRankPositions(it)
        }

        updateRankPositions(list.localData, noteIdList)

        emit(Unit)
    }

    override fun clearUndoStack() {
        undoList.clear()
        showSnackbar.postValue(false)
    }

    override fun onReceiveUnbindNote(noteId: Long) {
        viewModelScope.launchBack {
            list.change {
                /** Notes may have only one category, that's why we search only one item. */
                val item = it.firstOrNull { item -> item.noteId.contains(noteId) } ?: return@change

                /** Decrement [RankItem.bindCount] without db call. */
                item.bindCount = max(a = 0, b = item.bindCount - 1)
            }
        }
    }
}