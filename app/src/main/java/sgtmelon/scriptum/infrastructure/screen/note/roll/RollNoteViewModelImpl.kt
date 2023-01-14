package sgtmelon.scriptum.infrastructure.screen.note.roll

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.removeExtraSpace
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.cleanup.extension.removeAtOrNull
import sgtmelon.scriptum.cleanup.extension.validIndexOfFirst
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.data.noteHistory.HistoryAction
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationsDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
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
import sgtmelon.scriptum.infrastructure.screen.note.NoteConnector
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.isFalse
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.scriptum.infrastructure.utils.extensions.note.copy
import sgtmelon.scriptum.infrastructure.utils.extensions.note.getCheckCount
import sgtmelon.scriptum.infrastructure.utils.extensions.note.hideChecked
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onItemCheck
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onSave

class RollNoteViewModelImpl(
    init: NoteInit,
    history: NoteHistory,
    createNote: CreateRollNoteUseCase,
    getNote: GetRollNoteUseCase,
    cacheNote: CacheRollNoteUseCase,

    // TODO cleanup
    callback: RollNoteFragment,
    parentCallback: NoteConnector?,
    colorConverter: ColorConverter,
    preferencesRepo: PreferencesRepo,
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
    saveControl: SaveControl
) : ParentNoteViewModelImpl<NoteItem.Roll, RollNoteFragment>(
    init, history, createNote, getNote, cacheNote,

    // TODO cleanup
    callback, parentCallback, colorConverter, preferencesRepo, convertNote,
    updateNote, deleteNote, restoreNote, clearNote, setNotification, deleteNotification,
    getNotificationDateList, getRankId, getRankDialogNames, saveControl
), RollNoteViewModel {

    override suspend fun setupAfterInitialize() {
        mayAnimateIcon = false
        // TODO may this is not needed?
        setupEditMode(isEdit.value.isTrue())
        mayAnimateIcon = true

        callback.apply {
            setToolbarVisibleIcon(deprecatedNoteItem.isVisible, needAnim = false)
            notifyDataSetChanged(getAdapterList())
        }

        onUpdateInfo()
    }

    //region Cleanup

    override fun onRestoreData(): Boolean {
        if (id.value == Default.ID || deprecatedNoteItem.id == Default.ID) return false

        /**
         * Get color before restore data. Also get [NoteItem.Roll.isVisible] before
         * restore, because it should be the same after restore.
         */
        val colorFrom = deprecatedNoteItem.color
        val restoreItem = cacheNote.item
        if (restoreItem != null) {
            deprecatedNoteItem = restoreItem.copy(isVisible = deprecatedNoteItem.isVisible)
        }
        val colorTo = deprecatedNoteItem.color

        callback.notifyDataSetChanged(getAdapterList())

        setupEditMode(isEdit = false)
        onUpdateInfo()

        callback.tintToolbar(colorFrom, colorTo)
        color.postValue(colorTo)
        history.reset()

        return true
    }


    override fun changeVisible() {
        deprecatedNoteItem.isVisible = !deprecatedNoteItem.isVisible

        callback.setToolbarVisibleIcon(deprecatedNoteItem.isVisible, needAnim = true)

        notifyListByVisible()

        /**
         * Foreign key can't be created without note [id].
         * Insert will happen inside [save].
         */
        if (noteState.value != NoteState.CREATE) {
            viewModelScope.launch {
                runBack { updateVisible(deprecatedNoteItem) }

                if (isEdit.value.isFalse()) {
                    callback.sendNotifyNotesBroadcast()
                }
            }
        }
    }

    /**
     * TODO переделать как в других фрагментах со списками
     *
     * Function for update empty info text and visible. This func also calls from UI then item
     * animation finished (e.g. after swipe).
     *
     * Important thing for update visible: you must call func after adapter notify.
     */
    override fun onUpdateInfo() {
        val isListEmpty = deprecatedNoteItem.list.isEmpty()
        val isListHide =
            !deprecatedNoteItem.isVisible && deprecatedNoteItem.list.hideChecked().isEmpty()

        if (isListEmpty || isListHide) {
            callback.onBindingInfo(isListEmpty, isListHide)
        }

        callback.animateInfoVisible()
    }

    override fun onEditorClick(i: Int): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        val enterText = callback.getEnterText().removeExtraSpace()

        if (enterText.isEmpty()) {
            save(changeMode = true)
        } else {
            onClickAdd(toBottom = true)
        }

        return true
    }

    /**
     * All item positions updates after call [save], because it's hard
     * to control in Edit.
     */
    override fun onClickAdd(toBottom: Boolean) {
        if (callback.isDialogOpen || isEdit.value.isFalse()) return

        val enterText = callback.getEnterText().removeExtraSpace()

        if (enterText.isEmpty()) return

        callback.clearEnterText()

        val p = if (toBottom) deprecatedNoteItem.list.size else 0
        val rollItem = RollItem(position = p, text = enterText)

        history.add(HistoryAction.Roll.List.Add(p, rollItem))
        deprecatedNoteItem.list.add(p, rollItem)

        callback.apply {
            historyAvailable.postValue(history.available)
            scrollToItem(toBottom, p, getAdapterList())
        }
    }

    override fun onClickItemCheck(p: Int) {
        if (isEdit.value.isTrue()) return

        val absolutePosition = getAbsolutePosition(p) ?: return
        deprecatedNoteItem.onItemCheck(absolutePosition)
        cacheNote(deprecatedNoteItem)

        if (deprecatedNoteItem.isVisible) {
            callback.notifyItemChanged(getAdapterList(), p)
        } else {
            callback.notifyItemRemoved(getAdapterList(), p)
        }

        with(deprecatedNoteItem.list) { callback.updateProgress(getCheckCount(), size) }

        viewModelScope.launch {
            runBack { updateCheck(deprecatedNoteItem, absolutePosition) }

            callback.sendNotifyNotesBroadcast()
        }
    }

    //region Menu click

    // TODO move undo/redo staff inside use case or something like this
    override fun onMenuUndoRedoSelect(action: HistoryAction, isUndo: Boolean) {
        history.saveChanges = false

        when (action) {
            is HistoryAction.Name -> onMenuUndoRedoName(action, isUndo)
            is HistoryAction.Rank -> onMenuUndoRedoRank(action, isUndo)
            is HistoryAction.Color -> onMenuUndoRedoColor(action, isUndo)
            is HistoryAction.Roll.Enter -> onMenuUndoRedoRoll(action, isUndo)
            is HistoryAction.Roll.List.Add -> onMenuUndoRedoAdd(action, isUndo)
            is HistoryAction.Roll.List.Remove -> onMenuUndoRedoRemove(action, isUndo)
            is HistoryAction.Roll.Move -> onMenuUndoRedoMove(action, isUndo)
            else -> Unit
        }

        history.saveChanges = true
    }

    private fun onMenuUndoRedoRoll(action: HistoryAction.Roll.Enter, isUndo: Boolean) {
        val rollItem = deprecatedNoteItem.list.getOrNull(action.p) ?: return

        /** Need update data anyway! Even if this item in list is currently hided. */
        rollItem.text = action.value[isUndo]

        val adapterList = getAdapterList()
        val adapterPosition = adapterList.validIndexOfFirst(rollItem) ?: return

        if (deprecatedNoteItem.isVisible || !rollItem.isCheck) {
            callback.notifyItemChanged(adapterList, adapterPosition, action.cursor[isUndo])
        }
    }

    private fun onMenuUndoRedoAdd(action: HistoryAction.Roll.List.Add, isUndo: Boolean) {
        if (isUndo) {
            onRemoveItem(action)
        } else {
            onInsertItem(action, isUndo = false)
        }
    }

    private fun onMenuUndoRedoRemove(action: HistoryAction.Roll.List.Remove, isUndo: Boolean) {
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
    private fun onMenuUndoRedoMove(action: HistoryAction.Roll.Move, isUndo: Boolean) {
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
        if (changeMode && callback.isDialogOpen) return false

        if (isEdit.value.isFalse() || !deprecatedNoteItem.isSaveEnabled) return false

        deprecatedNoteItem.onSave()

        /** Need update adapter after remove rows with empty text. */
        callback.setList(getAdapterList())

        if (changeMode) {
            setupEditMode(isEdit = false)
            history.reset()
        }

        viewModelScope.launch {
            val isCreate = noteState.value == NoteState.CREATE
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
            callback.sendNotifyNotesBroadcast()
        }

        return true
    }

    // TODO may be post noteItem?
    override fun setupEditMode(isEdit: Boolean) {
        history.saveChanges = false

        this.isEdit.postValue(isEdit)
        historyAvailable.postValue(history.available)

        if (!isEdit) {
            with(deprecatedNoteItem.list) { callback.updateProgress(getCheckCount(), size) }
        }

        saveControl.isNeedSave = true
        saveControl.changeAutoSaveWork(isEdit)

        history.saveChanges = true
    }

    //endregion

    /**
     * Convert not pure position [adapterPosition] to absolute position in list (without hide).
     *
     * @Test - Have duplicate in test screen.
     */
    override fun getAbsolutePosition(adapterPosition: Int): Int? {
        return if (deprecatedNoteItem.isVisible) {
            adapterPosition
        } else {
            val list = deprecatedNoteItem.list
            val hideItem = list.hideChecked().getOrNull(adapterPosition) ?: return null

            return list.validIndexOfFirst(hideItem)
        }
    }

    override fun onRollHistoryEnabled(enabled: Boolean) {
        history.saveChanges = enabled
    }

    override fun onRollHistoryAdd(action: HistoryAction) = history.add(action)

    override fun onRollEnterChanged(p: Int, text: String) {
        val absolutePosition = getAbsolutePosition(p) ?: return
        deprecatedNoteItem.list.getOrNull(absolutePosition)?.text = text

        callback.apply {
            setList(getAdapterList())
            historyAvailable.postValue(history.available)
        }
    }

    override fun onRollActionNext() {
        callback.onFocusEnter()
    }

    /**
     * Use only for different notify functions. Don't use for change data.
     *
     * @return - list which uses for screen adapter.
     */
    private fun getAdapterList(): MutableList<RollItem> {
        val list = deprecatedNoteItem.list

        return if (deprecatedNoteItem.isVisible) list else list.hideChecked()
    }

    /**
     * Make good animation for items, remove or insert one by one.
     */
    private fun notifyListByVisible() {
        val list = ArrayList(deprecatedNoteItem.list)

        if (list.isEmpty()) return

        if (deprecatedNoteItem.isVisible) {
            notifyVisibleList(list)
        } else {
            notifyInvisibleList(list)
        }
    }

    private fun notifyVisibleList(list: MutableList<RollItem>) {
        val filterList = list.filter { it.isCheck }

        /**
         * If all items are checked (and hided).
         */
        if (filterList.size == list.size) {
            callback.animateInfoVisible(isVisible = false)

            for (i in list.indices) {
                callback.notifyItemInserted(list, i)
            }
        } else {
            for (item in filterList) {
                val index = list.validIndexOfFirst(item) ?: continue
                callback.notifyItemInserted(list, index)
            }
        }
    }

    private fun notifyInvisibleList(list: MutableList<RollItem>) {
        for (item in list.filter { it.isCheck }) {
            val index = list.validIndexOfFirst(item) ?: continue

            list.removeAtOrNull(index)
            callback.notifyItemRemoved(list, index)
        }
    }

    //endregion

}