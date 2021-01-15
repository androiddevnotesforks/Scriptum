package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.app.Application
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.NoteData.Default
import sgtmelon.scriptum.domain.model.item.InputItem
import sgtmelon.scriptum.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IRollNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.note.IRollNoteViewModel

/**
 * ViewModel for [IRollNoteFragment].
 */
class RollNoteViewModel(application: Application) :
    ParentNoteViewModel<NoteItem.Roll, IRollNoteFragment, IRollNoteInteractor>(application),
    IRollNoteViewModel {

    /**
     * Variable for detect first screen run. After rotations it will be false.
     */
    @RunPrivate var isFirstRun = true

    override fun cacheData() {
        restoreItem = noteItem.deepCopy()
    }

    override fun setupBeforeInitialize() {
        val theme = interactor.theme
        callback?.apply {
            setupBinding(theme)
            setupToolbar(theme, color)
            setupEnter(inputControl)
            setupRecycler(inputControl, isFirstRun)

            showToolbarVisibleIcon(isShow = false)
        }

        if (isFirstRun) isFirstRun = false
    }

    override suspend fun tryInitializeNote(): Boolean {
        /**
         * If first open.
         */
        if (!isNoteInitialized()) {
            val name = parentCallback?.getString(R.string.dialog_item_rank) ?: return false
            rankDialogItemArray = runBack { interactor.getRankDialogItemArray(name) }

            if (id == Default.ID) {
                val defaultColor = interactor.defaultColor

                noteItem = NoteItem.Roll.getCreate(defaultColor)
                cacheData()

                noteState = NoteState(isCreate = true)
            } else {
                runBack { interactor.getItem(id) }?.let {
                    noteItem = it
                    restoreItem = it.deepCopy()
                } ?: run {
                    parentCallback?.finish()
                    return false
                }

                noteState = NoteState(isBin = noteItem.isBin)
            }
        }

        return true
    }

    override suspend fun setupAfterInitialize() {
        callback?.setupDialog(rankDialogItemArray)
        callback?.setupProgress()

        mayAnimateIcon = false
        setupEditMode(noteState.isEdit)
        mayAnimateIcon = true

        callback?.apply {
            showToolbarVisibleIcon(isShow = true)
            setToolbarVisibleIcon(noteItem.isVisible, needAnim = false)
            notifyDataSetChanged(getList(noteItem))
        }

        onUpdateInfo()

        callback?.onBindingLoad(isRankEmpty = rankDialogItemArray.size == 1)
    }

    override fun onRestoreData(): Boolean {
        if (id == Default.ID) return false

        /**
         * Get color before restore data.
         */
        val colorFrom = noteItem.color
        noteItem = restoreItem.deepCopy()
        val colorTo = noteItem.color

        callback?.notifyDataSetChanged(getList(noteItem))

        setupEditMode(isEdit = false)
        onUpdateInfo()

        callback?.tintToolbar(colorFrom, colorTo)
        parentCallback?.onUpdateNoteColor(colorTo)
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
        if (!noteState.isCreate) {
            viewModelScope.launchBack { interactor.setVisible(noteItem, !noteState.isEdit) }
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

        val enterText = callback?.getEnterText()?.clearSpace() ?: ""

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
        if (callback?.isDialogOpen == true || !noteState.isEdit) return

        val enterText = callback?.getEnterText()?.clearSpace() ?: ""

        if (enterText.isEmpty()) return

        callback?.clearEnterText()

        val p = if (simpleClick) noteItem.list.size else 0
        val rollItem = RollItem(position = p, text = enterText)

        inputControl.onRollAdd(p, rollItem.toJson())
        noteItem.list.add(p, rollItem)

        callback?.apply {
            onBindingInput(noteItem, inputControl.access)
            scrollToItem(simpleClick, p, getList(noteItem))
        }
    }

    override fun onClickItemCheck(p: Int) {
        if (noteState.isEdit) return

        val correctPosition = getCorrectPosition(p, noteItem)
        noteItem.onItemCheck(correctPosition)
        cacheData()

        if (noteItem.isVisible) {
            callback?.notifyItemChanged(getList(noteItem), p)
        } else {
            callback?.notifyItemRemoved(getList(noteItem), p)
        }

        with(noteItem) { callback?.updateProgress(getCheck(), list.size) }

        viewModelScope.launchBack { interactor.updateRollCheck(noteItem, correctPosition) }
    }

    override fun onLongClickItemCheck() {
        if (noteState.isEdit) return

        val isCheck = noteItem.onItemLongCheck()
        cacheData()

        callback?.apply {
            changeCheckToggle(state = true)
            notifyDataRangeChanged(getList(noteItem))
            changeCheckToggle(state = false)

            with(noteItem) { updateProgress(getCheck(), list.size) }
        }

        if (!noteItem.isVisible) notifyListByVisible()

        viewModelScope.launchBack { interactor.updateRollCheck(noteItem, isCheck) }
    }

    //region Menu click

    override fun onMenuUndoRedoSelect(item: InputItem, isUndo: Boolean) {
        inputControl.isEnabled = false

        when (item.tag) {
            InputAction.RANK -> onMenuUndoRedoRank(item, isUndo)
            InputAction.COLOR -> onMenuUndoRedoColor(item, isUndo)
            InputAction.NAME -> onMenuUndoRedoName(item, isUndo)
            InputAction.ROLL -> onMenuUndoRedoRoll(item, isUndo)
            InputAction.ROLL_ADD, InputAction.ROLL_REMOVE -> {
                val isAddUndo = isUndo && item.tag == InputAction.ROLL_ADD
                val isRemoveRedo = !isUndo && item.tag == InputAction.ROLL_REMOVE

                if (isAddUndo || isRemoveRedo) {
                    onMenuUndoRedoAdd(item)
                } else {
                    onMenuUndoRedoRemove(item, isUndo)
                }
            }
            InputAction.ROLL_MOVE -> onMenuUndoRedoMove(item, isUndo)
        }

        inputControl.isEnabled = true
    }

    @RunPrivate fun onMenuUndoRedoRoll(item: InputItem, isUndo: Boolean) {
        val rollItem = noteItem.list.getOrNull(item.p) ?: return
        val position = getList(noteItem).validIndexOf(rollItem) ?: return

        rollItem.text = item[isUndo]

        if (noteItem.isVisible || (!noteItem.isVisible && !rollItem.isCheck)) {
            callback?.notifyItemChanged(getList(noteItem), position, item.cursor[isUndo])
        }
    }



    @RunPrivate fun onMenuUndoRedoAdd(item: InputItem) {
        val rollItem = noteItem.list.getOrNull(item.p) ?: return
        val position = getList(noteItem).validIndexOf(rollItem) ?: return

        noteItem.list.validRemoveAt(item.p) ?: return

        if (noteItem.isVisible || (!noteItem.isVisible && !rollItem.isCheck)) {
            callback?.notifyItemRemoved(getList(noteItem), position)
        }
    }

    @RunPrivate fun onMenuUndoRedoRemove(item: InputItem, isUndo: Boolean) {
        val rollItem = RollItem[item[isUndo]] ?: return

        noteItem.list.add(item.p, rollItem)

        if (noteItem.isVisible) {
            callback?.notifyItemInserted(getList(noteItem), item.p, rollItem.text.length)
        } else if (!rollItem.isCheck) {
            fun getShiftPosition(p: Int): Int {
                return p - noteItem.list.subList(0, p).let { it.size - it.hide().size }
            }

            val position = getShiftPosition(item.p)
            callback?.notifyItemInserted(getList(noteItem), position, rollItem.text.length)
        }
    }

    @RunPrivate fun onMenuUndoRedoMove(item: InputItem, isUndo: Boolean) {
        val from = item[!isUndo].toInt()
        val to = item[isUndo].toInt()

        val rollItem = noteItem.list.getOrNull(from) ?: return

        val shiftFrom = getList(noteItem).validIndexOf(rollItem) ?: return
        noteItem.list.move(from, to)
        val shiftTo = getList(noteItem).validIndexOf(rollItem) ?: return

        if (noteItem.isVisible || (!noteItem.isVisible && !rollItem.isCheck)) {
            callback?.notifyItemMoved(getList(noteItem), shiftFrom, shiftTo)
        }
    }

    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (changeMode && callback?.isDialogOpen == true) return false

        if (!noteState.isEdit || !noteItem.isSaveEnabled()) return false

        noteItem.onSave()

        /**
         * Need update adapter after remove rows with empty text.
         */
        callback?.setList(getList(noteItem))

        if (changeMode) {
            callback?.hideKeyboard()
            setupEditMode(isEdit = false)
            inputControl.reset()
        } else if (noteState.isCreate) {
            /**
             * Change toolbar icon from arrow to cancel.
             */
            callback?.setToolbarBackIcon(isCancel = true, needAnim = true)
        }

        parentCallback?.onUpdateNoteColor(noteItem.color)

        viewModelScope.launch {
            runBack { interactor.saveNote(noteItem, noteState.isCreate) }
            cacheData()

            if (noteState.isCreate) {
                noteState.isCreate = NoteState.ND_CREATE

                id = noteItem.id
                parentCallback?.onUpdateNoteId(id)

                /**
                 * Need if [noteItem] isVisible changes wasn't set inside [onClickVisible] because of
                 * not created note.
                 */
                runBack { interactor.setVisible(noteItem, updateBind = true) }
            }

            callback?.setList(getList(noteItem))
        }

        return true
    }


    override fun setupEditMode(isEdit: Boolean) {
        inputControl.isEnabled = false

        noteState.isEdit = isEdit
        callback?.apply {
            setToolbarBackIcon(
                isCancel = isEdit && !noteState.isCreate,
                needAnim = !noteState.isCreate && mayAnimateIcon
            )

            onBindingEdit(noteItem, isEdit)
            onBindingInput(noteItem, inputControl.access)
            viewModelScope.launchBack { updateNoteState(noteState) }

            if (isEdit) {
                focusOnEdit(noteState.isCreate)
            } else {
                updateProgress(noteItem.getCheck(), noteItem.list.size)
            }
        }

        saveControl.needSave = true
        saveControl.setSaveEvent(isEdit)

        inputControl.isEnabled = true
    }

    //endregion

    override fun onInputRollChange(p: Int, text: String) {
        val correctPosition = getCorrectPosition(p, noteItem)
        noteItem.list.getOrNull(correctPosition)?.text = text

        callback?.apply {
            setList(getList(noteItem))
            onBindingInput(noteItem, inputControl.access)
        }
    }

    override fun onRollActionNext() {
        callback?.onFocusEnter()
    }

    //region Touch callbacks

    override fun onTouchAction(inAction: Boolean) {
        callback?.setTouchAction(inAction)
    }

    override fun onTouchGetDrag(): Boolean {
        val value = noteState.isEdit

        if (value) callback?.hideKeyboard()

        return value
    }

    override fun onTouchGetSwipe(): Boolean = noteState.isEdit

    /**
     * All item positions updates after call [onMenuSave], because it's hard
     * to control in Edit.
     */
    override fun onTouchSwiped(p: Int) {
        val correctPosition = getCorrectPosition(p, noteItem)
        val item = noteItem.list.validRemoveAt(correctPosition) ?: return

        inputControl.onRollRemove(correctPosition, item.toJson())

        callback?.apply {
            onBindingInput(noteItem, inputControl.access)
            notifyItemRemoved(getList(noteItem), p)
        }
    }

    /**
     * All item positions updates after call [onMenuSave], because it's hard
     * to control in Edit.
     */
    override fun onTouchMove(from: Int, to: Int): Boolean {
        val correctFrom = getCorrectPosition(from, noteItem)
        val correctTo = getCorrectPosition(to, noteItem)

        noteItem.list.move(correctFrom, correctTo)

        callback?.notifyItemMoved(getList(noteItem), from, to)
        callback?.hideKeyboard()

        return true
    }

    override fun onTouchMoveResult(from: Int, to: Int) {
        val correctFrom = getCorrectPosition(from, noteItem)
        val correctTo = getCorrectPosition(to, noteItem)

        inputControl.onRollMove(correctFrom, correctTo)

        callback?.onBindingInput(noteItem, inputControl.access)
    }

    //endregion

    /**
     * If have hide items when need correct position.
     *
     * @Test - Have duplicate in test screen.
     */
    @RunPrivate
    fun getCorrectPosition(p: Int, noteItem: NoteItem.Roll): Int {
        return if (noteItem.isVisible) p else noteItem.list.let { it.indexOf(it.hide()[p]) }
    }

    /**
     * Use only for different notify functions. Don't use for change data.
     */
    @RunPrivate
    fun getList(noteItem: NoteItem.Roll): MutableList<RollItem> {
        return noteItem.list.let { if (noteItem.isVisible) it else it.hide() }
    }

    /**
     * Make good animation for items, remove or insert one by one.
     */
    @RunPrivate
    fun notifyListByVisible() {
        val list = ArrayList(noteItem.list)

        if (list.size == 0) return

        if (noteItem.isVisible) {
            if (!list.any { !it.isCheck }) {
                callback?.animateInfoVisible(isVisible = false)
            }

            for (item in list.filter { it.isCheck }) {
                val index = list.validIndexOf(item) ?: continue
                callback?.notifyItemInserted(list, index)
            }
        } else {
            while (list.any { it.isCheck }) {
                list.validIndexOf { it.isCheck }?.also {
                    list.validRemoveAt(it) ?: return@also
                    callback?.notifyItemRemoved(list, it)
                }
            }
        }
    }
}