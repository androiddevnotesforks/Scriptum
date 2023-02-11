package sgtmelon.scriptum.infrastructure.screen.note.roll

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.launchBack
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

    private fun postNotifyItemList(item: NoteItem.Roll) {
        val list = getCurrentItemList(item)
        _itemList.clearAdd(list)
        itemList.postValue(list)
        notifyShowList()
    }

    private fun getCurrentItemList(item: NoteItem.Roll): List<RollItem> {
        return if (item.isVisible) item.list else item.list.hideChecked()
    }

    override fun restoreData(): Boolean {
        val item = noteItem.value ?: return false
        /** Save [NoteItem.Roll.isVisible], because it should be the same after restore. */
        val restoreItem = cacheNote.item?.copy(isVisible = item.isVisible) ?: return false

        if (id.value == Default.ID || item.id == Default.ID) return false

        isEdit.postValue(false)
        noteItem.postValue(restoreItem)
        postNotifyItemList(restoreItem)
        color.postValue(restoreItem.color)

        history.reset()

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





    // TODO Plan:
    // 1. Add CustomListNotifyViewModel here
    // 2. Add this into UI
    // 3. Change callback calls (for notify items) with CustomListNotifyViewModel realization (see Rank/NotificationViewModel)
    // 4.

    //region Cleanup

    @Deprecated("Use new realization")
    private lateinit var deprecatedNoteItem: NoteItem.Roll


    /**
     * All item positions updates after call [save], because it's hard
     * to control in Edit.
     */
    override fun addItem(toBottom: Boolean, text: String) {
        val p = if (toBottom) deprecatedNoteItem.list.size else 0
        val rollItem = RollItem(position = p, text = text)

        history.add(HistoryAction.Roll.List.Add(p, rollItem))
        deprecatedNoteItem.list.add(p, rollItem)

        callback.apply {
            historyAvailable.postValue(history.available)
            scrollToItem(toBottom, p, getAdapterList())
        }
    }

    override fun changeItemCheck(position: Int) {
        if (isEditMode) return

        val absolutePosition = getCorrectPosition(position) ?: return
        deprecatedNoteItem.onItemCheck(absolutePosition)
        cacheNote(deprecatedNoteItem)

        if (deprecatedNoteItem.isVisible) {
            callback.notifyItemChanged(getAdapterList(), position)
        } else {
            callback.notifyItemRemoved(getAdapterList(), position)
        }

        // TODO post to noteItem
        //        with(deprecatedNoteItem.list) { callback.updateProgress(getCheckCount(), size) }

        viewModelScope.launch {
            runBack { updateCheck(deprecatedNoteItem, absolutePosition) }

            //            callback.sendNotifyNotesBroadcast()
        }
    }

    //region Menu click

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

        val adapterList = getAdapterList()
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
        val adapterPosition = getAdapterList().validIndexOfFirst(rollItem)

        /** Need update data anyway! Even if this item in list is currently hided. *
         * Also need remove item at the end. Because [getAdapterList] return list without
         * that item and you will get not valid index of item.
         */
        deprecatedNoteItem.list.removeAtOrNull(action.p) ?: return

        if (adapterPosition == null) return

        if (deprecatedNoteItem.isVisible || !rollItem.isCheck) {
            /**
             * Need get new [getAdapterList] for clear effect, cause we remove one item from it.
             */
            callback.notifyItemRemoved(getAdapterList(), adapterPosition)
        }
    }

    private fun onInsertItem(action: HistoryAction.Roll.List, isUndo: Boolean) {
        val rollItem = action.item

        /** Need update data anyway! Even if this item in list is currently hided. */
        deprecatedNoteItem.list.add(action.p, rollItem)

        val list = getAdapterList()
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
        val shiftFrom = getAdapterList().validIndexOfFirst(rollItem)
        deprecatedNoteItem.list.move(from, to)
        val shiftTo = getAdapterList().validIndexOfFirst(rollItem)

        if (shiftFrom == null || shiftTo == null) return

        if (deprecatedNoteItem.isVisible || !rollItem.isCheck) {
            callback.notifyItemMoved(getAdapterList(), shiftFrom, shiftTo)
        }
    }

    /**
     * Don't need update [color] because it's happen in [changeColor] function.
     */
    override fun save(changeMode: Boolean): Boolean {
        if (isReadMode || !deprecatedNoteItem.isSaveEnabled) return false

        deprecatedNoteItem.onSave()

        // TODO post note item

        /** Need update adapter after remove rows with empty text. */
        callback.setList(getAdapterList())

        if (changeMode) {
            isEdit.postValue(false)
            history.reset()
        }

        viewModelScope.launch {
            val isCreate = noteState.value == NoteState.CREATE
            /** [saveNote] updates [NoteItem.id], if it was in [NoteState.CREATE] */
            runBack { saveNote(deprecatedNoteItem, isCreate) }
            cacheNote(deprecatedNoteItem)

            if (isCreate) {
                noteState.postValue(NoteState.EXIST)
                id.postValue(deprecatedNoteItem.id)

                /**
                 * Need if [deprecatedNoteItem] isVisible changes wasn't set inside [changeVisible] because of
                 * not created note.
                 */
                runBack { updateVisible(deprecatedNoteItem) }
            }

            callback.setList(getAdapterList())
            //            callback.sendNotifyNotesBroadcast()
        }

        return true
    }

    //endregion

    // TODO Have same functions in the test screen.
    /**
     * Convert not pure position [adapterPosition] to correct (absolute) position in list
     * (without hided items).
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

    // TODO replace with getCurrentItemList()
    /**
     * Use only for different notify functions. Don't use for change data.
     *
     * @return - list which uses for screen adapter.
     */
    private fun getAdapterList(): List<RollItem> {
        val list = deprecatedNoteItem.list

        return if (deprecatedNoteItem.isVisible) list else list.hideChecked()
    }

    // Touch staff

    /** All item positions updates after call [save], because it's hard to control in Edit. */
    override fun swipeItem(position: Int) {
        val absolutePosition = getCorrectPosition(position) ?: return
        val item = deprecatedNoteItem.list.removeAtOrNull(absolutePosition) ?: return

        history.add(HistoryAction.Roll.List.Remove(absolutePosition, item))
        historyAvailable.postValue(history.available)

        callback.notifyItemRemoved(getAdapterList(), position)
    }

    /** All item positions updates after call [save], because it's hard to control in Edit. */
    override fun moveItem(from: Int, to: Int) {
        val correctFrom = getCorrectPosition(from)
        val correctTo = getCorrectPosition(to)

        if (correctFrom == null || correctTo == null) return

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

    override fun releaseItem(position: Int) {
        val absolute = getCorrectPosition(position) ?: return
        callback.notifyItemChanged(getAdapterList(), absolute)
    }

    //endregion

}