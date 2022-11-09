package sgtmelon.scriptum.infrastructure.screen.main.rank

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.math.max
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import sgtmelon.extensions.flowOnBack
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.runBack
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
import sgtmelon.test.idling.getIdling
import sgtmelon.test.prod.RunPrivate

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

            showList.postValue(ShowListState.Loading)
            val list = getList()
            _itemList.clearAdd(list)
            itemList.postValue(list)
            notifyShowList()

            getIdling().stop(IdlingTag.Notes.LOAD_DATA)
        }
    }

    //region Cleanup

    /**
     * Variable for control drag state. TRUE - if drag state, FALSE - otherwise.
     */
    @RunPrivate var inTouchAction = false

    override fun onUpdateToolbar() {
        val enterName = callback?.getEnterText() ?: return
        val clearName = enterName.clearSpace().uppercase()

        callback?.onBindingToolbar(
            isClearEnable = enterName.isNotEmpty(),
            isAddEnable = clearName.isNotEmpty() && !uniqueNameList.contains(clearName)
        )
    }

    override fun onResultRenameDialog(p: Int, name: String) {
        val item = _itemList.getOrNull(p)?.apply { this.name = name } ?: return

        viewModelScope.launchBack { updateRank(item) }

        onUpdateToolbar()
        callback?.notifyList(_itemList)
    }

    override fun onClickEnterCancel() {
        callback?.clearEnter()
    }

    override fun onEditorClick(i: Int): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        val name = callback?.getEnterText()?.clearSpace()?.uppercase()

        if (name.isNullOrEmpty() || uniqueNameList.contains(name)) return false

        onClickEnterAdd(addToBottom = true)

        return true
    }

    override fun onClickEnterAdd(addToBottom: Boolean) {
        val name = callback?.clearEnter()?.clearSpace()

        if (name.isNullOrEmpty() || uniqueNameList.contains(name.uppercase())) return

        callback?.hideKeyboard()
        callback?.dismissSnackbar()

        viewModelScope.launch {
            val item = runBack { insertRank(name) } ?: return@launch
            val p = if (addToBottom) _itemList.size else 0

            _itemList.add(p, item)

            runBack { updateRankPositions(_itemList, correctRankPositions(_itemList)) }

            callback?.scrollToItem(_itemList, p, addToBottom)
        }
    }

    override fun onItemAnimationFinished() {
        callback?.onBindingList()

        /**
         * Need prevent clear openState if item is currently dragging.
         */
        if (!inTouchAction) {
            callback?.openState?.clear()
        }
    }

    override fun onTouchAction(inAction: Boolean) {
        inTouchAction = inAction

        if (inAction) {
            callback?.dismissSnackbar()
        }

        callback?.openState?.isBlocked = inAction
    }

    override fun onTouchGetDrag(): Boolean {
        val value = callback?.openState?.isBlocked != true

        if (value) callback?.hideKeyboard()

        return value
    }

    override fun onTouchMove(from: Int, to: Int): Boolean {
        _itemList.move(from, to)

        callback?.notifyItemMoved(_itemList, from, to)
        callback?.hideKeyboard()

        return true
    }

    override fun onTouchMoveResult() {
        callback?.openState?.clear()

        val noteIdList = correctRankPositions(_itemList)
        callback?.setList(_itemList)

        viewModelScope.launchBack { updateRankPositions(_itemList, noteIdList) }
    }

    //endregion

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

        /**
         * If list size equals 1 -> need just show list without animation, because of
         * animation glitch.
         */
        updateList = if (_itemList.size == 1) {
            UpdateListState.NotifyHard
        } else {
            UpdateListState.Insert(position)
        }

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