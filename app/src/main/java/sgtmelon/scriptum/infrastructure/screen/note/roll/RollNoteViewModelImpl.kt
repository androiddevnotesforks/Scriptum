package sgtmelon.scriptum.infrastructure.screen.note.roll

import androidx.annotation.MainThread
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
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.database.DbData.Note.Default
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListStorageImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.note.deepCopy
import sgtmelon.scriptum.infrastructure.utils.extensions.note.hideChecked
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onItemCheck
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onSave
import sgtmelon.scriptum.infrastructure.utils.extensions.note.visibleList

/**
 * Inside [ListStorageImpl.data] and [ListStorageImpl.localData] will be stored current list
 * with some filters (e.g. not show done items). So keep in mind, that need to use adapter
 * position inside this two lists.
 *
 * In [NoteItem.Roll.list] will be stored list without filters, use pure position.
 */
class RollNoteViewModelImpl(
    init: NoteInit,
    history: NoteHistory,
    colorConverter: ColorConverter,
    override val list: ListStorageImpl<RollItem>,
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
    colorConverter, init, history, cacheNote,
    convertNote, updateNote, deleteNote, restoreNote, clearNote,
    setNotification, deleteNotification, getNotificationDateList,
    getRankId, getRankDialogNames, getHistoryResult
), RollNoteViewModel {

    override fun afterDataInit(item: NoteItem.Roll) {
        super.afterDataInit(item)
        postNotifyItemList(item)
    }

    /** [updateList] needed for custom updates. */
    private fun postNotifyItemList(item: NoteItem.Roll, updateList: UpdateListState? = null) {
        list.change(updateList) {
            it.clearAdd(item.visibleList)
        }
    }

    override fun restoreData(): Boolean {
        val item = noteItem.value ?: return false
        /** Save [NoteItem.Roll.isVisible], because it should be the same after restore. */
        val restoreItem = cacheNote.item?.deepCopy(isVisible = item.isVisible) ?: return false

        if (item.id == Default.ID) return false

        isEdit.postValue(false)
        noteItem.postValue(restoreItem)
        postNotifyItemList(restoreItem)
        color.postValue(restoreItem.color)

        history.reset()

        return true
    }

    //region History staff

    override fun selectHistoryResult(result: HistoryResult) {
        when (result) {
            is HistoryResult.Name -> return /** [noteItem] will be updated through UI. */
            is HistoryResult.Rank -> onHistoryRank(result)
            is HistoryResult.Color -> onHistoryColor(result)
            is HistoryResult.Roll.Enter -> onHistoryRoll(result) /** Cursor will be set in UI. */
            is HistoryResult.Roll.Add -> onHistoryAdd(result) /** Cursor will be set in UI. */
            is HistoryResult.Roll.Remove -> onHistoryRemove(result)
            is HistoryResult.Roll.Move -> onHistoryMove(result)
            else -> return /** For another note types. */
        }
    }

    private fun onHistoryRoll(result: HistoryResult.Roll.Enter) {
        val item = noteItem.value ?: return
        noteItem.postValueWithChange { it.list.getOrNull(result.p)?.text = result.value }
        postNotifyItemList(item)
    }

    private fun onHistoryAdd(result: HistoryResult.Roll.Add) {
        val item = noteItem.value ?: return
        noteItem.postValueWithChange { it.list.add(result.p, result.item) }
        postNotifyItemList(item)
    }

    private fun onHistoryRemove(result: HistoryResult.Roll.Remove) {
        val item = noteItem.value ?: return
        noteItem.postValueWithChange { it.list.removeAtOrNull(result.p) }
        postNotifyItemList(item)
    }

    private fun onHistoryMove(result: HistoryResult.Roll.Move) {
        val item = noteItem.value ?: return
        /** It's okay not to notify about items move. */
        item.list.move(result.from, result.to)
        postNotifyItemList(item)
    }

    //endregion

    /** Don't need update [color] because it's happen in [changeColor] function. */
    override fun save(changeMode: Boolean): Boolean {
        val item = noteItem.value ?: return false

        if (isReadMode || !item.isSaveEnabled) return false

        /**
         * If we don't [changeMode] - need save empty items (without text) for future use.
         * Them will be cleared only in [changeMode] case.
         */
        item.onSave(clearEmpty = changeMode)
        noteItem.postValue(item)

        /** Need update adapter after remove rows with empty text, position indexing. */
        postNotifyItemList(item, UpdateListState.Set)

        if (changeMode) {
            isEdit.postValue(false)
            history.reset()
            historyAvailable.postValue(history.available)
        } else {
            /**
             * In case if we not [changeMode], after [saveNote] all related [RollItem]s with this
             * [noteItem] will be deleted from DataBase, if [NoteItem.Roll.list] don't contains
             * them [RollItem.id].
             *
             * What's why need reset [RollItem.id] to default value in history list (for swiped
             * items). It will be inserted back in case if we will use undo/redo.
             */
            history.list.forEach { if (it is HistoryAction.Roll.List.Remove) it.item.id = null }
        }

        viewModelScope.launch {
            val isCreate = noteState.value == NoteState.CREATE
            /** [saveNote] updates [NoteItem.id], if it was in [NoteState.CREATE] */
            runBack { saveNote(item, isCreate) }
            cacheNote(item)

            if (isCreate) {
                noteState.postValue(NoteState.EXIST)

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
         * Foreign key can't be created without [NoteItem.id]. Insert will happen inside [save].
         * That's why call update only for created notes.
         */
        if (noteState.value != NoteState.CREATE) {
            viewModelScope.launchBack { updateVisible(item) }
        }
    }

    override fun changeItemCheck(position: Int) {
        if (isEditMode) return

        val absolutePosition = getAbsolutePosition(position) ?: return
        val item = noteItem.value ?: return

        item.onItemCheck(absolutePosition)

        noteItem.postValue(item)
        cacheNote(item)

        val updateList = if (item.isVisible) {
            UpdateListState.Change(position)
        } else {
            UpdateListState.Remove(position)
        }
        postNotifyItemList(item, updateList)

        viewModelScope.launchBack {
            updateCheck(item, absolutePosition)
        }
    }

    /**
     * All item [RollItem.position] updates after call [save], because it's hard to control
     * during [isEditMode].
     */
    override fun addItem(toBottom: Boolean, text: String) {
        val item = noteItem.value ?: return

        /** List size after adding item, will be last index. */
        val position = if (toBottom) item.list.size else 0
        val rollItem = RollItem(position = position, text = text)

        /** Post to [noteItem] because list size was changed and need to update a progressBar. */
        item.list.add(position, rollItem)
        noteItem.postValue(item)

        list.change {
            /** List size after adding item, will be last index. */
            val hidePosition = if (toBottom) it.size else 0
            it.add(hidePosition, rollItem)

            list.update = UpdateListState.chooseInsert(it.size, hidePosition)
        }

        history.add(HistoryAction.Roll.List.Add(position, rollItem))
        historyAvailable.postValue(history.available)
    }

    // Touch staff

    /**
     * All item [RollItem.position] updates after call [save], because it's hard to control
     * during [isEditMode].
     */
    @MainThread
    override fun swipeItem(position: Int) {
        val absolutePosition = getAbsolutePosition(position) ?: return

        val item = list.swipe(position) ?: return

        noteItem.postValueWithChange { it.list.removeAtOrNull(absolutePosition) }

        history.add(HistoryAction.Roll.List.Remove(absolutePosition, item))
        historyAvailable.postValue(history.available)
    }

    /**
     * All item [RollItem.position] updates after call [save], because it's hard to control
     * during [isEditMode].
     */
    @MainThread
    override fun moveItem(from: Int, to: Int) = list.move(from, to)

    /**
     * All item [RollItem.position] updates after call [save], because it's hard to control
     * during [isEditMode].
     */
    @MainThread
    override fun moveItemResult(from: Int, to: Int) {
        val absoluteFrom = getAbsolutePosition(from)
        val absoluteTo = getAbsolutePosition(to)

        if (absoluteFrom == null || absoluteTo == null) return

        /** It's okay not to notify about items move. */
        noteItem.value?.list?.move(absoluteFrom, absoluteTo)

        history.add(HistoryAction.Roll.Move(HistoryChange(absoluteFrom, absoluteTo)))
        historyAvailable.postValue(history.available)
    }

    // Write holder functions

    // TODO Have same functions in the test screen.
    /**
     * Convert not pure adapter [position] to correct one (absolute position in list
     * without hided items).
     */
    override fun getAbsolutePosition(position: Int): Int? {
        val item = noteItem.value ?: return null

        return if (item.isVisible) {
            position
        } else {
            /**
             * Need every time filter checked, because [list] in some cases not
             * representative (some items was moved) and it may produce position bugs.
             *
             * For example, in [moveItemResult] changes was applied (in [moveItem]) before
             * [getAbsolutePosition] call.
             */
            val currentItem = item.list.hideChecked().getOrNull(position) ?: return null
            return item.list.validIndexOfFirst(currentItem)
        }
    }

    override fun onRollHistoryAdd(action: HistoryAction) = history.add(action)

    override fun onRollEnterChanged(position: Int, text: String) {
        val absolutePosition = getAbsolutePosition(position) ?: return

        noteItem.postValueWithChange { it.list.getOrNull(absolutePosition)?.text = text }
        list.change(UpdateListState.Set) { it.getOrNull(position)?.text = text }

        historyAvailable.postValue(history.available)
    }
}