package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.removeExtraSpace
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.hide
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.cleanup.extension.removeAtOrNull
import sgtmelon.scriptum.cleanup.extension.validIndexOfFirst
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.IRollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.IRollNoteViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.SaveNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateRollCheckUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateRollVisibleUseCase
import sgtmelon.scriptum.domain.useCase.note.getNote.GetRollNoteUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.note.NoteConnector
import sgtmelon.scriptum.infrastructure.utils.extensions.isFalse
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.test.prod.RunPrivate

/**
 * ViewModel for [IRollNoteFragment].
 */
class RollNoteViewModel(
    isEdit: Boolean,
    noteState: NoteState,
    id: Long,
    color: Color,

    // TODO cleanup
    callback: IRollNoteFragment,
    parentCallback: NoteConnector?,
    colorConverter: ColorConverter,
    preferencesRepo: PreferencesRepo,
    private val getNote: GetRollNoteUseCase,
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
    getNotificationDateList: GetNotificationDateListUseCase,
    getRankId: GetRankIdUseCase,
    private val getRankDialogNames: GetRankDialogNamesUseCase
) : ParentNoteViewModel<NoteItem.Roll, IRollNoteFragment>(
    isEdit, noteState, id, color,

    // TODO cleanup
    callback, parentCallback, colorConverter, preferencesRepo, convertNote,
    updateNote, deleteNote, restoreNote, clearNote, setNotification, deleteNotification,
    getNotificationDateList, getRankId
), IRollNoteViewModel {

    /**
     * Variable for detect first screen run. After rotations it will be false.
     */
    @RunPrivate var isFirstRun = true

    override fun cacheData() {
        restoreItem = noteItem.deepCopy()
    }

    override fun setupBeforeInitialize() {
        callback?.apply {
            setupBinding()
            color.value?.let { setupToolbar(it) }
            setupEnter(inputControl)
            setupRecycler(inputControl, isFirstRun)

            showToolbarVisibleIcon(isShow = false)
        }

        if (isFirstRun) isFirstRun = false
    }

    override suspend fun tryInitializeNote(): Boolean {
        /** If first open. */
        if (!isNoteInitialized()) {
            rankDialogItemArray = runBack { getRankDialogNames() }

            val id = id.value
            if (id == null || id == Default.ID) {
                val defaultColor = preferencesRepo.defaultColor
                noteItem = NoteItem.Roll.getCreate(defaultColor)
                cacheData()
            } else {
                runBack { getNote(id) }?.let {
                    noteItem = it
                    restoreItem = it.deepCopy()

                    callback?.sendNotifyNotesBroadcast()
                } ?: run {
                    callback?.finish()
                    return false
                }

            }
        }

        return true
    }

    override suspend fun setupAfterInitialize() {
        callback?.setupDialog(rankDialogItemArray)

        mayAnimateIcon = false
        // TODO may this is not needed?
        setupEditMode(isEdit.value.isTrue())
        mayAnimateIcon = true

        callback?.apply {
            showToolbarVisibleIcon(isShow = true)
            setToolbarVisibleIcon(noteItem.isVisible, needAnim = false)
            notifyDataSetChanged(getAdapterList())
        }

        onUpdateInfo()

        callback?.onBindingLoad(isRankEmpty = rankDialogItemArray.size == 1)
    }

    override fun onRestoreData(): Boolean {
        if (id.value == Default.ID || noteItem.id == Default.ID) return false

        /**
         * Get color before restore data. Also get [NoteItem.Roll.isVisible] before
         * restore, because it should be the same after restore.
         */
        val colorFrom = noteItem.color
        val isVisible = noteItem.isVisible
        noteItem = restoreItem.deepCopy(isVisible = isVisible)
        val colorTo = noteItem.color

        callback?.notifyDataSetChanged(getAdapterList())

        setupEditMode(isEdit = false)
        onUpdateInfo()

        callback?.tintToolbar(colorFrom, colorTo)
        color.postValue(colorTo)
        inputControl.reset()

        return true
    }


    override fun onClickVisible() {
        noteItem.isVisible = !noteItem.isVisible

        callback?.setToolbarVisibleIcon(noteItem.isVisible, needAnim = true)

        notifyListByVisible()

        /**
         * Foreign key can't be created without note [id].
         * Insert will happen inside [onMenuSave].
         */
        if (noteState.value != NoteState.CREATE) {
            viewModelScope.launch {
                runBack { updateVisible(noteItem) }

                if (isEdit.value.isFalse()) {
                    callback?.sendNotifyNotesBroadcast()
                }
            }
        }
    }

    /**
     * Function for update empty info text and visible. This func also calls from UI then item
     * animation finished (e.g. after swipe).
     *
     * Important thing for update visible: you must call func after adapter notify.
     */
    override fun onUpdateInfo() {
        val isListEmpty = noteItem.list.size == 0
        val isListHide = !noteItem.isVisible && noteItem.list.hide().size == 0

        if (isListEmpty || isListHide) {
            callback?.onBindingInfo(isListEmpty, isListHide)
        }

        callback?.animateInfoVisible()
    }

    override fun onEditorClick(i: Int): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        val enterText = callback?.getEnterText()?.removeExtraSpace() ?: ""

        if (enterText.isEmpty()) {
            onMenuSave(changeMode = true)
        } else {
            onClickAdd(simpleClick = true)
        }

        return true
    }

    /**
     * All item positions updates after call [onMenuSave], because it's hard
     * to control in Edit.
     */
    override fun onClickAdd(simpleClick: Boolean) {
        if (callback?.isDialogOpen == true || isEdit.value.isFalse()) return

        val enterText = callback?.getEnterText()?.removeExtraSpace() ?: ""

        if (enterText.isEmpty()) return

        callback?.clearEnterText()

        val p = if (simpleClick) noteItem.list.size else 0
        val rollItem = RollItem(position = p, text = enterText)

        inputControl.onRollAdd(p, rollItem.toJson())
        noteItem.list.add(p, rollItem)

        callback?.apply {
            onBindingInput(noteItem, inputControl.access)
            scrollToItem(simpleClick, p, getAdapterList())
        }
    }

    override fun onClickItemCheck(p: Int) {
        if (isEdit.value.isTrue()) return

        val absolutePosition = getAbsolutePosition(p) ?: return
        noteItem.onItemCheck(absolutePosition)
        cacheData()

        if (noteItem.isVisible) {
            callback?.notifyItemChanged(getAdapterList(), p)
        } else {
            callback?.notifyItemRemoved(getAdapterList(), p)
        }

        with(noteItem) { callback?.updateProgress(getCheck(), list.size) }

        viewModelScope.launch {
            runBack { updateCheck(noteItem, absolutePosition) }

            callback?.sendNotifyNotesBroadcast()
        }
    }

    //region Menu click

    // TODO move undo/redo staff inside use case or something like this
    override fun onMenuUndoRedoSelect(item: InputItem, isUndo: Boolean) {
        inputControl.isEnabled = false

        when (item.tag) {
            InputAction.RANK -> onMenuUndoRedoRank(item, isUndo)
            InputAction.COLOR -> onMenuUndoRedoColor(item, isUndo)
            InputAction.NAME -> onMenuUndoRedoName(item, isUndo)
            InputAction.ROLL -> onMenuUndoRedoRoll(item, isUndo)
            InputAction.ROLL_ADD -> onMenuUndoRedoAdd(item, isUndo)
            InputAction.ROLL_REMOVE -> onMenuUndoRedoRemove(item, isUndo)
            InputAction.ROLL_MOVE -> onMenuUndoRedoMove(item, isUndo)
        }

        inputControl.isEnabled = true
    }

    @RunPrivate fun onMenuUndoRedoRoll(item: InputItem, isUndo: Boolean) {
        val rollItem = noteItem.list.getOrNull(item.p) ?: return

        /**
         * Need update data anyway! Even if this item in list is currently hided.
         */
        rollItem.text = item[isUndo]

        val adapterList = getAdapterList()
        val adapterPosition = adapterList.validIndexOfFirst(rollItem) ?: return

        if (noteItem.isVisible || (!noteItem.isVisible && !rollItem.isCheck)) {
            callback?.notifyItemChanged(adapterList, adapterPosition, item.cursor[isUndo])
        }
    }

    @RunPrivate fun onMenuUndoRedoAdd(item: InputItem, isUndo: Boolean) {
        if (isUndo) {
            onRemoveItem(item)
        } else {
            onInsertItem(item, isUndo = false)
        }
    }

    @RunPrivate fun onMenuUndoRedoRemove(item: InputItem, isUndo: Boolean) {
        if (isUndo) {
            onInsertItem(item, isUndo = true)
        } else {
            onRemoveItem(item)
        }
    }

    @RunPrivate fun onRemoveItem(item: InputItem) {
        val rollItem = noteItem.list.getOrNull(item.p) ?: return
        val adapterPosition = getAdapterList().validIndexOfFirst(rollItem)

        /**
         * Need update data anyway! Even if this item in list is currently hided.
         *
         * Also need remove item at the end. Because [getAdapterList] return list without
         * that item and you will get not valid index of item.
         */
        noteItem.list.removeAtOrNull(item.p) ?: return

        if (adapterPosition == null) return

        if (noteItem.isVisible || (!noteItem.isVisible && !rollItem.isCheck)) {
            /**
             * Need get new [getAdapterList] for clear effect, cause we remove one item from it.
             */
            callback?.notifyItemRemoved(getAdapterList(), adapterPosition)
        }
    }

    @RunPrivate fun onInsertItem(item: InputItem, isUndo: Boolean) {
        val rollItem = RollItem[item[isUndo]] ?: return

        /**
         * Need update data anyway! Even if this item in list is currently hided.
         */
        noteItem.list.add(item.p, rollItem)

        val list = getAdapterList()
        val position = getInsertPosition(item, rollItem) ?: return
        val cursor = rollItem.text.length

        callback?.notifyItemInserted(list, position, cursor)
    }

    @RunPrivate fun getInsertPosition(
        item: InputItem,
        rollItem: RollItem
    ): Int? = when {
        noteItem.isVisible -> item.p
        !rollItem.isCheck -> noteItem.list.subList(0, item.p).hide().size
        else -> null
    }

    // TODO record exception
    @RunPrivate fun onMenuUndoRedoMove(item: InputItem, isUndo: Boolean) {
        val from = item[!isUndo].toIntOrNull() ?: return
        val to = item[isUndo].toIntOrNull() ?: return

        val rollItem = noteItem.list.getOrNull(from) ?: return

        /**
         * Need update data anyway! Even if this item in list is currently hided.
         */
        val shiftFrom = getAdapterList().validIndexOfFirst(rollItem)
        noteItem.list.move(from, to)
        val shiftTo = getAdapterList().validIndexOfFirst(rollItem)

        if (shiftFrom == null || shiftTo == null) return

        if (noteItem.isVisible || (!noteItem.isVisible && !rollItem.isCheck)) {
            callback?.notifyItemMoved(getAdapterList(), shiftFrom, shiftTo)
        }
    }

    /**
     * Don't need update [color] because it's happen in [onResultColorDialog] function.
     */
    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (changeMode && callback?.isDialogOpen == true) return false

        if (isEdit.value.isFalse() || !noteItem.isSaveEnabled()) return false

        noteItem.onSave()

        /**
         * Need update adapter after remove rows with empty text.
         */
        callback?.setList(getAdapterList())

        if (changeMode) {
            callback?.hideKeyboard()
            setupEditMode(isEdit = false)
            inputControl.reset()
        } else if (noteState.value == NoteState.CREATE) {
            /** Change toolbar icon from arrow to cancel for auto save case. */
            callback?.setToolbarBackIcon(isCancel = true, needAnim = true)
        }

        viewModelScope.launch { saveBackgroundWork() }

        return true
    }

    override suspend fun saveBackgroundWork() {
        val isCreate = noteState.value == NoteState.CREATE
        runBack { saveNote(noteItem, isCreate) }
        cacheData()

        if (isCreate) {
            noteState.postValue(NoteState.EXIST)
            id.postValue(noteItem.id)

            /**
             * Need if [noteItem] isVisible changes wasn't set inside [onClickVisible] because of
             * not created note.
             */
            runBack { updateVisible(noteItem) }
        }

        callback?.setList(getAdapterList())
        callback?.sendNotifyNotesBroadcast()
    }

    override fun setupEditMode(isEdit: Boolean) {
        inputControl.isEnabled = false

        this.isEdit.postValue(isEdit)

        callback?.apply {
            val noteState = noteState.value
            val notCreate = noteState != NoteState.CREATE
            setToolbarBackIcon(
                isCancel = notCreate && isEdit,
                needAnim = notCreate && mayAnimateIcon
            )

            onBindingEdit(noteItem, isEdit)
            onBindingInput(noteItem, inputControl.access)
            viewModelScope.launchBack { updateNoteState(isEdit, noteState) }

            if (isEdit) {
                focusOnEdit(isCreate = noteState == NoteState.CREATE)
            } else {
                updateProgress(noteItem.getCheck(), noteItem.list.size)
            }
        }

        saveControl.isNeedSave = true
        saveControl.changeAutoSaveWork(isEdit)

        inputControl.isEnabled = true
    }

    //endregion

    override fun onInputRollChange(p: Int, text: String) {
        val absolutePosition = getAbsolutePosition(p) ?: return
        noteItem.list.getOrNull(absolutePosition)?.text = text

        callback?.apply {
            setList(getAdapterList())
            onBindingInput(noteItem, inputControl.access)
        }
    }

    /**
     * Convert not pure position [adapterPosition] to absolute position in list (without hide).
     *
     * @Test - Have duplicate in test screen.
     */
    override fun getAbsolutePosition(adapterPosition: Int): Int? {
        return if (noteItem.isVisible) {
            adapterPosition
        } else {
            val list = noteItem.list
            val hideItem = list.hide().getOrNull(adapterPosition) ?: return null

            return list.validIndexOfFirst(hideItem)
        }
    }

    override fun onRollActionNext() {
        callback?.onFocusEnter()
    }

    //region Touch callbacks

    override fun onTouchAction(inAction: Boolean) {
        callback?.setTouchAction(inAction)
    }

    override fun onTouchGetDrag(isDragAvailable: Boolean): Boolean {
        return isEdit.value.isTrue() && isDragAvailable
    }

    override fun onTouchGetSwipe(): Boolean = isEdit.value.isTrue()

    override fun onTouchDragStart() {
        callback?.hideKeyboard()
    }

    /**
     * All item positions updates after call [onMenuSave], because it's hard
     * to control in Edit.
     */
    override fun onTouchSwiped(p: Int) {
        val absolutePosition = getAbsolutePosition(p) ?: return
        val item = noteItem.list.removeAtOrNull(absolutePosition) ?: return

        inputControl.onRollRemove(absolutePosition, item.toJson())

        callback?.apply {
            onBindingInput(noteItem, inputControl.access)
            notifyItemRemoved(getAdapterList(), p)
        }
    }

    /**
     * All item positions updates after call [onMenuSave], because it's hard
     * to control in Edit.
     */
    override fun onTouchMove(from: Int, to: Int): Boolean {
        val absoluteFrom = getAbsolutePosition(from) ?: return true
        val absoluteTo = getAbsolutePosition(to) ?: return true

        noteItem.list.move(absoluteFrom, absoluteTo)

        callback?.notifyItemMoved(getAdapterList(), from, to)
        callback?.hideKeyboard()

        return true
    }

    override fun onTouchClear(position: Int) {
        val absolute = getAbsolutePosition(position) ?: return

        callback?.notifyItemChanged(getAdapterList(), absolute)
    }

    override fun onTouchMoveResult(from: Int, to: Int) {
        val absoluteFrom = getAbsolutePosition(from) ?: return
        val absoluteTo = getAbsolutePosition(to) ?: return

        inputControl.onRollMove(absoluteFrom, absoluteTo)

        callback?.onBindingInput(noteItem, inputControl.access)
    }

    //endregion

    /**
     * Use only for different notify functions. Don't use for change data.
     *
     * @return - list which uses for screen adapter.
     */
    @RunPrivate fun getAdapterList(): MutableList<RollItem> {
        val list = noteItem.list

        return if (noteItem.isVisible) list else list.hide()
    }

    /**
     * Make good animation for items, remove or insert one by one.
     */
    @RunPrivate fun notifyListByVisible() {
        val list = ArrayList(noteItem.list)

        if (list.size == 0) return

        if (noteItem.isVisible) {
            notifyVisibleList(list)
        } else {
            notifyInvisibleList(list)
        }
    }

    @RunPrivate fun notifyVisibleList(list: MutableList<RollItem>) {
        val filterList = list.filter { it.isCheck }

        /**
         * If all items are checked (and hided).
         */
        if (filterList.size == list.size) {
            callback?.animateInfoVisible(isVisible = false)

            for (i in list.indices) {
                callback?.notifyItemInserted(list, i)
            }
        } else {
            for (item in filterList) {
                val index = list.validIndexOfFirst(item) ?: continue
                callback?.notifyItemInserted(list, index)
            }
        }
    }

    @RunPrivate fun notifyInvisibleList(list: MutableList<RollItem>) {
        for (item in list.filter { it.isCheck }) {
            val index = list.validIndexOfFirst(item) ?: continue

            list.removeAtOrNull(index)
            callback?.notifyItemRemoved(list, index)
        }
    }
}