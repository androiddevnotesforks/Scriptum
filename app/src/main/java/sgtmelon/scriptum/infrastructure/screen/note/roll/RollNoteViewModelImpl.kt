package sgtmelon.scriptum.infrastructure.screen.note.roll

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.cleanup.extension.removeAtOrNull
import sgtmelon.scriptum.cleanup.extension.validIndexOfFirst
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
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
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.note.copy
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
    private val callback: RollNoteFragment,
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
    getHistoryResult: GetHistoryResultUseCase
) : ParentNoteViewModelImpl<NoteItem.Roll>(
    init, history, createNote, getNote, cacheNote,

    // TODO cleanup
    colorConverter, preferencesRepo, convertNote,
    updateNote, deleteNote, restoreNote, clearNote, setNotification, deleteNotification,
    getNotificationDateList, getRankId, getRankDialogNames, getHistoryResult
), RollNoteViewModel {

    override suspend fun setupAfterInitialize() {
        //        mayAnimateIcon = false
        // TODO may this is not needed?
        setupEditMode(isEditMode)
        //        mayAnimateIcon = true

        callback.apply {
            // TODO post noteItem to change visible icon
            //            setToolbarVisibleIcon(deprecatedNoteItem.isVisible, needAnim = false)
            notifyDataSetChanged(getAdapterList())
        }

        onUpdateInfo()
    }

    //region Cleanup

    override fun restoreData(): Boolean {
        if (id.value == Default.ID || deprecatedNoteItem.id == Default.ID) return false

        /**
         * Get [NoteItem.Roll.isVisible] before restore, because it should be the same
         * after restore.
         */
        val restoreItem = cacheNote.item
        if (restoreItem != null) {
            deprecatedNoteItem = restoreItem.copy(isVisible = deprecatedNoteItem.isVisible)
        }
        val colorTo = deprecatedNoteItem.color

        callback.notifyDataSetChanged(getAdapterList())

        setupEditMode(isEdit = false)
        onUpdateInfo()

        color.postValue(colorTo)
        history.reset()

        return true
    }


    override fun changeVisible() {
        deprecatedNoteItem.isVisible = !deprecatedNoteItem.isVisible

        // TODO post noteItem to change visible icon
        //        callback.setToolbarVisibleIcon(deprecatedNoteItem.isVisible, needAnim = true)

        notifyListByVisible()

        /**
         * Foreign key can't be created without note [id].
         * Insert will happen inside [save].
         */
        if (noteState.value != NoteState.CREATE) {
            viewModelScope.launch {
                runBack { updateVisible(deprecatedNoteItem) }

                if (isReadMode) {
//                    callback.sendNotifyNotesBroadcast()
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

    override fun changeItemCheck(p: Int) {
        if (isEditMode) return

        val absolutePosition = getAbsolutePosition(p) ?: return
        deprecatedNoteItem.onItemCheck(absolutePosition)
        cacheNote(deprecatedNoteItem)

        if (deprecatedNoteItem.isVisible) {
            callback.notifyItemChanged(getAdapterList(), p)
        } else {
            callback.notifyItemRemoved(getAdapterList(), p)
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
        TODO()
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
            //            callback.sendNotifyNotesBroadcast()
        }

        return true
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

    override fun onRollHistoryAdd(action: HistoryAction) = history.add(action)

    override fun onRollEnterChanged(p: Int, text: String) {
        val absolutePosition = getAbsolutePosition(p) ?: return
        deprecatedNoteItem.list.getOrNull(absolutePosition)?.text = text

        callback.apply {
            setList(getAdapterList())
            historyAvailable.postValue(history.available)
        }
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