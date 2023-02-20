package sgtmelon.scriptum.infrastructure.screen.note.roll

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.postValueWithChange
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.cleanup.extension.removeAtOrNull
import sgtmelon.scriptum.cleanup.extension.validIndexOfFirst
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.data.noteHistory.model.HistoryChange
import sgtmelon.scriptum.domain.model.result.HistoryResult
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationsDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetHistoryResultUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.SaveNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateRollCheckUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateRollVisibleUseCase
import sgtmelon.scriptum.domain.useCase.note.cacheNote.CacheRollNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateRollNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.getNote.GetRollNoteUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.UpdateListState
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.parent.list.CustomListNotifyViewModel
import sgtmelon.scriptum.infrastructure.utils.extensions.note.copy
import sgtmelon.scriptum.infrastructure.utils.extensions.note.hideChecked
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onItemCheck
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onSave

class RollNoteViewModelImpl(
    private val callback: RollNoteFragment,
    init: NoteInit,
    history: NoteHistory,
    colorConverter: ColorConverter,
    createNote: CreateRollNoteUseCase,
    getNote: GetRollNoteUseCase,
    cacheNote: CacheRollNoteUseCase,
    private val saveNote: SaveNoteUseCase,
    convertNote: ConvertNoteUseCase,
    updateNote: UpdateNoteUseCase,
    deleteNote: DeleteNoteUseCase,
    restoreNote: RestoreNoteUseCase,
    clearNote: ClearNoteUseCase,
    private val updateVisible: UpdateRollVisibleUseCase,
    private val updateCheck: UpdateRollCheckUseCase,
    setNotification: SetNotificationUseCase,
    deleteNotification: DeleteNotificationUseCase,
    getNotificationDateList: GetNotificationsDateListUseCase,
    getRankId: GetRankIdUseCase,
    getRankDialogNames: GetRankDialogNamesUseCase,
    getHistoryResult: GetHistoryResultUseCase
) : ParentNoteViewModelImpl<NoteItem.Roll>(
    colorConverter, init, history, createNote, getNote, cacheNote,
    convertNote, updateNote, deleteNote, restoreNote, clearNote,
    setNotification, deleteNotification, getNotificationDateList,
    getRankId, getRankDialogNames, getHistoryResult
), CustomListNotifyViewModel<RollItem>,
    RollNoteViewModel {

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)
    override val itemList: MutableLiveData<List<RollItem>> = MutableLiveData()
    override val _itemList: MutableList<RollItem> = mutableListOf()
    override var updateList: UpdateListState = UpdateListState.Notify
        get() {
            val value = field
            updateList = UpdateListState.Notify
            return value
        }

    override suspend fun initAfterDataReady(item: NoteItem.Roll) = postNotifyItemList(item)

    /** [updateList] needed for custom updates. */
    private fun postNotifyItemList(item: NoteItem.Roll, updateList: UpdateListState? = null) {
        val list = getCurrentItemList(item)
        _itemList.clearAdd(list)

        if (updateList != null) {
            this.updateList = updateList
        }

        itemList.postValue(list)
        notifyShowList()
    }

    private fun getCurrentItemList(item: NoteItem.Roll): List<RollItem> = with(item) {
        return if (isVisible) list else list.hideChecked()
    }

    override fun restoreData(): Boolean {
        val item = noteItem.value ?: return false
        /** Save [NoteItem.Roll.isVisible], because it should be the same after restore. */
        val restoreItem = cacheNote.item?.copy(isVisible = item.isVisible) ?: return false

        if (id.value == Default.ID || item.id == Default.ID) return false

        isEdit.postValue(false)
        noteItem.postValue(restoreItem)
        // TODO тут было жёсткое обновление (notifyDataSetChanged), надо проверить как будет работать
        postNotifyItemList(restoreItem)
        color.postValue(restoreItem.color)

        history.reset()

        return true
    }

    // TODO history staff here

    /** Don't need update [color] because it's happen in [changeColor] function. */
    override fun save(changeMode: Boolean): Boolean {
        val item = noteItem.value ?: return false

        if (isReadMode || !item.isSaveEnabled) return false

        item.onSave()
        noteItem.postValue(item)

        /** Need update adapter after remove rows with empty text, position indexing. */
        postNotifyItemList(item, UpdateListState.Set)

        if (changeMode) {
            isEdit.postValue(false)
            history.reset()
        }

        viewModelScope.launch {
            val isCreate = noteState.value == NoteState.CREATE
            /** [saveNote] updates [NoteItem.id], if it was in [NoteState.CREATE] */
            runBack { saveNote(item, isCreate) }
            cacheNote(item)

            if (isCreate) {
                noteState.postValue(NoteState.EXIST)
                id.postValue(item.id)

                /**
                 * Need if [noteItem] isVisible changes wasn't set inside [changeVisible]
                 * because of note wasn't exist.
                 */
                runBack { updateVisible(item) }
            }

            /** Need update data after [saveNote] there was some changes. */
            postNotifyItemList(item, UpdateListState.Set)
        }

        return true
    }

    override fun changeVisible() {
        val item = noteItem.value ?: return
        item.isVisible = !item.isVisible

        noteItem.postValue(item)
        postNotifyItemList(item)

        /**
         * Foreign key can't be created without note [id]. Insert will happen inside [save].
         * That's why call update only for created notes.
         */
        if (noteState.value != NoteState.CREATE) {
            viewModelScope.launchBack { updateVisible(item) }
        }
    }

    override fun changeItemCheck(position: Int) {
        if (isEditMode) return

        val correctPosition = getCorrectPosition(position) ?: return
        val item = noteItem.value ?: return

        item.onItemCheck(correctPosition)

        noteItem.postValue(item)
        cacheNote(item)

        val updateList = if (item.isVisible) {
            UpdateListState.Change(position)
        } else {
            UpdateListState.Remove(position)
        }
        postNotifyItemList(item, updateList)

        viewModelScope.launchBack {
            updateCheck(item, correctPosition)
        }
    }

    /** All item positions updates after call [save], because it's hard to control in Edit. */
    override fun addItem(toBottom: Boolean, text: String) {
        val item = noteItem.value ?: return

        val position = if (toBottom) item.list.size else 0
        val hidePosition = if (toBottom) _itemList.size else 0
        val rollItem = RollItem(position = position, text = text)

        item.list.add(position, rollItem)
        _itemList.add(hidePosition, rollItem)

        /** Post to [noteItem] because list size was changed and need to update a progressBar. */
        noteItem.postValue(item)
        updateList = UpdateListState.chooseInsert(_itemList.size, hidePosition)
        itemList.postValue(_itemList)
        notifyShowList()

        history.add(HistoryAction.Roll.List.Add(position, rollItem))
        historyAvailable.postValue(history.available)
    }

    //region Cleanup

    @Deprecated("Use new realization")
    private lateinit var deprecatedNoteItem: NoteItem.Roll

    // Menu click

    override fun selectHistoryResult(result: HistoryResult) {
        when (result) {
            is HistoryResult.Rank -> onHistoryRank(result)
            is HistoryResult.Color -> onHistoryColor(result)
            is HistoryResult.Roll -> TODO()
            else -> return
        }

        //        when (action) {
        //            is HistoryResult.Name -> onUndoRedoName(action, isUndo)
        //            is HistoryResult.Rank -> onUndoRedoRank(action, isUndo)
        //            is HistoryResult.Color -> onUndoRedoColor(action, isUndo)
        //            is HistoryResult.Roll.Enter -> onHistoryRoll(action, isUndo)
        //            is HistoryResult.Roll.List.Add -> onHistoryAdd(action, isUndo)
        //            is HistoryResult.Roll.List.Remove -> onHistoryRemove(action, isUndo)
        //            is HistoryResult.Roll.Move -> onHistoryMove(action, isUndo)
        //            else -> Unit
        //        }
    }

    private fun onHistoryRoll(action: HistoryAction.Roll.Enter, isUndo: Boolean) {
        val rollItem = deprecatedNoteItem.list.getOrNull(action.p) ?: return

        /** Need update data anyway! Even if this item in list is currently hided. */
        rollItem.text = action.value[isUndo]

        val adapterList = getCurrentItemList(noteItem.value ?: return)
        val adapterPosition = adapterList.validIndexOfFirst(rollItem) ?: return

        if (deprecatedNoteItem.isVisible || !rollItem.isCheck) {
            callback.notifyItemChanged(adapterList, adapterPosition, action.cursor[isUndo])
        }
    }

    private fun onHistoryAdd(action: HistoryAction.Roll.List.Add, isUndo: Boolean) {
        if (isUndo) {
            onRemoveItem(action)
        } else {
            onInsertItem(action, isUndo = false)
        }
    }

    private fun onHistoryRemove(action: HistoryAction.Roll.List.Remove, isUndo: Boolean) {
        if (isUndo) {
            onInsertItem(action, isUndo = true)
        } else {
            onRemoveItem(action)
        }
    }

    private fun onRemoveItem(action: HistoryAction.Roll.List) {
        val rollItem = deprecatedNoteItem.list.getOrNull(action.p) ?: return
        val adapterPosition = getCurrentItemList(noteItem.value ?: return).validIndexOfFirst(rollItem)

        /** Need update data anyway! Even if this item in list is currently hided. *
         * Also need remove item at the end. Because [getCurrentItemList] return list without
         * that item and you will get not valid index of item.
         */
        deprecatedNoteItem.list.removeAtOrNull(action.p) ?: return

        if (adapterPosition == null) return

        if (deprecatedNoteItem.isVisible || !rollItem.isCheck) {
            /**
             * Need get new [getCurrentItemList] for clear effect, cause we remove one item from it.
             */
            callback.notifyItemRemoved(getCurrentItemList(noteItem.value ?: return), adapterPosition)
        }
    }

    private fun onInsertItem(action: HistoryAction.Roll.List, isUndo: Boolean) {
        val rollItem = action.item

        /** Need update data anyway! Even if this item in list is currently hided. */
        deprecatedNoteItem.list.add(action.p, rollItem)

        val list = getCurrentItemList(noteItem.value ?: return)
        val position = getInsertPosition(action) ?: return
        val cursor = rollItem.text.length

        callback.notifyItemInserted(list, position, cursor)
    }

    private fun getInsertPosition(action: HistoryAction.Roll.List): Int? = when {
        deprecatedNoteItem.isVisible -> action.p
        !action.item.isCheck -> deprecatedNoteItem.list.subList(0, action.p).hideChecked().size
        else -> null
    }

    // TODO record exception
    private fun onHistoryMove(action: HistoryAction.Roll.Move, isUndo: Boolean) {
        val from = action.value[!isUndo]
        val to = action.value[isUndo]

        val rollItem = deprecatedNoteItem.list.getOrNull(from) ?: return

        /** Need update data anyway! Even if this item in list is currently hided. */
        val shiftFrom = getCurrentItemList(noteItem.value ?: return).validIndexOfFirst(rollItem)
        deprecatedNoteItem.list.move(from, to)
        val shiftTo = getCurrentItemList(noteItem.value ?: return).validIndexOfFirst(rollItem)

        if (shiftFrom == null || shiftTo == null) return

        if (deprecatedNoteItem.isVisible || !rollItem.isCheck) {
            callback.notifyItemMoved(getCurrentItemList(noteItem.value ?: return), shiftFrom, shiftTo)
        }
    }


    //endregion

    // Touch staff

    /** All item positions updates after call [save], because it's hard to control in Edit. */
    override fun swipeItem(position: Int) {
        val correctPosition = getCorrectPosition(position) ?: return

        val item = _itemList.removeAtOrNull(correctPosition) ?: return
        updateList = UpdateListState.Remove(correctPosition)
        itemList.postValue(_itemList)
        notifyShowList()

        noteItem.postValueWithChange { it.list.removeAtOrNull(correctPosition) }

        history.add(HistoryAction.Roll.List.Remove(correctPosition, item))
        historyAvailable.postValue(history.available)
    }

    /** All item positions updates after call [save], because it's hard to control in Edit. */
    override fun moveItem(from: Int, to: Int) {
        val correctFrom = getCorrectPosition(from)
        val correctTo = getCorrectPosition(to)

        if (correctFrom == null || correctTo == null) return

        /**
         * Important: don't use [MutableLiveData.postValue] here with [itemList], because it
         * leads to UI glitches during item drag/move.
         */
        _itemList.move(correctFrom, correctTo)
        updateList = UpdateListState.Move(correctFrom, correctTo)
        itemList.value = _itemList
    }

    /** All item positions updates after call [save], because it's hard to control in Edit. */
    override fun moveItemResult(from: Int, to: Int) {
        val correctFrom = getCorrectPosition(from)
        val correctTo = getCorrectPosition(to)

        if (correctFrom == null || correctTo == null) return

        noteItem.value?.list?.move(correctFrom, correctTo)

        history.add(HistoryAction.Roll.Move(HistoryChange(correctFrom, correctTo)))
        historyAvailable.postValue(history.available)
    }

    // Write holder functions

    // TODO Have same functions in the test screen.
    /**
     * Convert not pure position [adapterPosition] to correct one (absolute position in list
     * without hided items).
     */
    override fun getCorrectPosition(adapterPosition: Int): Int? {
        val item = noteItem.value ?: return null

        return if (item.isVisible) {
            adapterPosition
        } else {
            val currentItem = _itemList.getOrNull(adapterPosition) ?: return null
            return item.list.validIndexOfFirst(currentItem)
        }
    }

    override fun onRollHistoryAdd(action: HistoryAction) = history.add(action)

    override fun onRollEnterChanged(position: Int, text: String) {
        val correctPosition = getCorrectPosition(position) ?: return
        val list = noteItem.value?.list ?: return

        list.getOrNull(correctPosition)?.text = text
        _itemList.getOrNull(position)?.text = text

        updateList = UpdateListState.Set
        itemList.postValue(_itemList)

        historyAvailable.postValue(history.available)
    }

    //endregion

}