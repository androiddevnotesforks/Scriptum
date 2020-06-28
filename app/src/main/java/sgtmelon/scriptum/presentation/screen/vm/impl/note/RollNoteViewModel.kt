package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.app.Application
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.annotation.test.RunNone
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.NoteData.Default
import sgtmelon.scriptum.domain.model.data.NoteData.Intent
import sgtmelon.scriptum.domain.model.item.InputItem
import sgtmelon.scriptum.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.screen.ui.callback.note.roll.IRollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.note.IRollNoteViewModel

/**
 * ViewModel for [RollNoteFragment].
 */
class RollNoteViewModel(application: Application) :
        ParentNoteViewModel<NoteItem.Roll, IRollNoteFragment, IRollNoteInteractor>(application),
        IRollNoteViewModel {

    /**
     * Variable for control visible button state.
     */
    @RunPrivate var isVisible = true
        set(value) {
            field = value
            isVisibleTest = true
        }

    override fun cacheData() {
        restoreItem = noteItem.deepCopy()
    }

    override fun onSetup(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID
        color = bundle?.getInt(Intent.COLOR, Default.COLOR) ?: Default.COLOR

        if (color == Default.COLOR) {
            color = interactor.defaultColor
        }

        val theme = interactor.theme
        callback?.apply {
            setupBinding(theme)
            setupToolbar(theme, color)
            setupEnter(inputControl)
            setupRecycler(inputControl)

            showToolbarVisibleIcon(isShow = false)
        }

        viewModelScope.launch {
            /**
             * If first open.
             */
            if (!isNoteInitialized()) {
                val name = parentCallback?.getString(R.string.dialog_item_rank) ?: return@launch
                rankDialogItemArray = interactor.getRankDialogItemArray(name)

                if (id == Default.ID) {
                    val defaultColor = interactor.defaultColor

                    noteItem = NoteItem.Roll.getCreate(defaultColor)
                    cacheData()

                    noteState = NoteState(isCreate = true)
                } else {
                    interactor.getItem(id)?.let {
                        noteItem = it
                        restoreItem = it.deepCopy()
                    } ?: run {
                        parentCallback?.finish()
                        return@launch
                    }

                    noteState = NoteState(isBin = noteItem.isBin)

                    /**
                     * Foreign key can't be created without note [id].
                     * Insert will happen inside [onMenuSave].
                     */
                    isVisible = interactor.getVisible(noteItem.id)
                }
            }

            callback?.setupDialog(rankDialogItemArray)
            callback?.setupProgress()

            iconState.notAnimate { setupEditMode(noteState.isEdit) }

            callback?.showToolbarVisibleIcon(isShow = true)
            callback?.setToolbarVisibleIcon(isVisible, needAnim = false)

            callback?.notifyDataSetChanged(getList(noteItem))
            onUpdateInfo()

            callback?.onBindingLoad(isRankEmpty = rankDialogItemArray.size == 1)
        }
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
        isVisible = !isVisible

        callback?.setToolbarVisibleIcon(isVisible, needAnim = true)

        notifyListByVisible()

        /**
         * Foreign key can't be created without note [id].
         * Insert will happen inside [onMenuSave].
         */
        if (!noteState.isCreate) {
            viewModelScope.launch { interactor.setVisible(noteItem.id, isVisible) }
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
        val isListHide = !isVisible && hide(noteItem.list).size == 0

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

        if (isVisible) {
            callback?.notifyItemChanged(getList(noteItem), p)
        } else {
            callback?.notifyItemRemoved(getList(noteItem), p)
        }

        with(noteItem) { callback?.updateProgress(getCheck(), list.size) }

        viewModelScope.launch { interactor.updateRollCheck(noteItem, correctPosition) }
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

        if (!isVisible) notifyListByVisible()

        viewModelScope.launch { interactor.updateRollCheck(noteItem, isCheck) }
    }

    //region Menu click

    override fun onMenuUndoRedo(isUndo: Boolean) {
        if (callback?.isDialogOpen == true || !noteState.isEdit) return

        val item = if (isUndo) inputControl.undo() else inputControl.redo()

        if (item != null) inputControl.makeNotEnabled {
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
        }

        callback?.onBindingInput(noteItem, inputControl.access)
    }

    private fun onMenuUndoRedoRoll(item: InputItem, isUndo: Boolean) {
        val rollItem = noteItem.list.getOrNull(item.p) ?: return
        val position = getList(noteItem).indexOfOrNull(rollItem) ?: return

        rollItem.text = item[isUndo]

        if (isVisible || (!isVisible && !rollItem.isCheck)) {
            callback?.notifyItemChanged(getList(noteItem), position, item.cursor[isUndo])
        }
    }

    private fun onMenuUndoRedoAdd(item: InputItem) {
        val rollItem = noteItem.list.getOrNull(item.p) ?: return
        val position = getList(noteItem).indexOfOrNull(rollItem) ?: return

        noteItem.list.removeAtOrNull(item.p) ?: return

        if (isVisible || (!isVisible && !rollItem.isCheck)) {
            callback?.notifyItemRemoved(getList(noteItem), position)
        }
    }

    /**
     * TODO add scroll to item on insert?
     */
    private fun onMenuUndoRedoRemove(item: InputItem, isUndo: Boolean) {
        val rollItem = RollItem[item[isUndo]] ?: return

        noteItem.list.add(item.p, rollItem)

        if (isVisible) {
            callback?.notifyItemInserted(getList(noteItem), item.p, rollItem.text.length)
        } else if (!rollItem.isCheck) {
            fun getShiftPosition(p: Int): Int {
                return p - noteItem.list.subList(0, p).let { it.size - hide(it).size }
            }

            val position = getShiftPosition(item.p)
            callback?.notifyItemInserted(getList(noteItem), position, rollItem.text.length)
        }
    }

    private fun onMenuUndoRedoMove(item: InputItem, isUndo: Boolean) {
        val from = item[!isUndo].toInt()
        val to = item[isUndo].toInt()

        val rollItem = noteItem.list.getOrNull(from) ?: return

        val shiftFrom = getList(noteItem).indexOf(rollItem)
        noteItem.list.move(from, to)
        val shiftTo = getList(noteItem).indexOf(rollItem)

        if (isVisible || (!isVisible && !rollItem.isCheck)) {
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
            interactor.saveNote(noteItem, noteState.isCreate)
            cacheData()

            if (noteState.isCreate) {
                noteState.isCreate = NoteState.ND_CREATE

                id = noteItem.id
                parentCallback?.onUpdateNoteId(id)

                /**
                 * Need if [isVisible] changes wasn't set inside [onClickVisible] because of
                 * not created note.
                 */
                interactor.setVisible(id, isVisible)
            }

            callback?.setList(getList(noteItem))
        }

        return true
    }


    override fun setupEditMode(isEdit: Boolean) = inputControl.makeNotEnabled {
        noteState.isEdit = isEdit

        callback?.apply {
            setToolbarBackIcon(
                    isCancel = isEdit && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            onBindingEdit(noteItem, isEdit)
            onBindingInput(noteItem, inputControl.access)
            updateNoteState(noteState)

            if (isEdit) {
                focusOnEdit(noteState.isCreate)
            } else {
                updateProgress(noteItem.getCheck(), noteItem.list.size)
            }
        }

        saveControl.needSave = true
        saveControl.setSaveEvent(isEdit)
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

    override fun onTouchGetDrag(): Boolean = noteState.isEdit

    override fun onTouchGetSwipe(): Boolean = noteState.isEdit

    /**
     * All item positions updates after call [onMenuSave], because it's hard
     * to control in Edit.
     */
    override fun onTouchSwiped(p: Int) {
        val correctPosition = getCorrectPosition(p, noteItem)
        val item = noteItem.list.removeAtOrNull(correctPosition) ?: return

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
        return if (isVisible) p else noteItem.list.let { it.indexOf(hide(it)[p]) }
    }

    /**
     * @Test - Have duplicate in test screen.
     */
    @RunPrivate
    fun hide(list: MutableList<RollItem>): MutableList<RollItem> {
        return ArrayList(list.filter { !it.isCheck })
    }

    /**
     * Use only for different notify functions. Don't use for change data.
     */
    @RunPrivate
    fun getList(noteItem: NoteItem.Roll): MutableList<RollItem> {
        return noteItem.list.let { if (isVisible) it else hide(it) }
    }

    /**
     * Make good animation for items, remove or insert one by one.
     */
    @RunPrivate
    fun notifyListByVisible() {
        viewModelScope.launch {
            val list = ArrayList(noteItem.list)

            if (list.size == 0) return@launch

            if (isVisible) {
                if (!list.any { !it.isCheck }) {
                    callback?.animateInfoVisible(isVisible = false)
                }

                list.filter { it.isCheck }.forEach { item ->
                    list.indexOfOrNull(item)?.also { callback?.notifyItemInserted(list, it) }
                }
            } else {
                while (list.any { it.isCheck }) {
                    list.indexOfFirstOrNull { it.isCheck }?.also {
                        list.removeAtOrNull(it) ?: return@also
                        callback?.notifyItemRemoved(list, it)
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Variable only for UI tests.
         */
        @RunNone var isVisibleTest = true
    }

}