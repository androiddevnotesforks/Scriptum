package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.app.Application
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.data.room.converter.model.StringConverter
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.data.NoteData.Default
import sgtmelon.scriptum.domain.model.data.NoteData.Intent
import sgtmelon.scriptum.domain.model.item.InputItem
import sgtmelon.scriptum.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.domain.model.state.IconState
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.control.note.save.ISaveControl
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.presentation.screen.ui.callback.note.roll.IRollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.ParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.note.IRollNoteViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

/**
 * ViewModel for [RollNoteFragment].
 */
class RollNoteViewModel(application: Application) : ParentViewModel<IRollNoteFragment>(application),
        IRollNoteViewModel {

    private var parentCallback: INoteChild? = null

    fun setParentCallback(callback: INoteChild?) {
        parentCallback = callback
    }

    private lateinit var interactor: IRollNoteInteractor
    private lateinit var bindInteractor: IBindInteractor

    fun setInteractor(interactor: IRollNoteInteractor, bindInteractor: IBindInteractor) {
        this.interactor = interactor
        this.bindInteractor = bindInteractor
    }


    private val saveControl: ISaveControl by lazy {
        SaveControl(context, interactor.getSaveModel(), callback = this)
    }

    private val inputControl = InputControl()

    private var id: Long = Default.ID
    private var color: Int = Default.COLOR

    private lateinit var noteItem: NoteItem
    private lateinit var restoreItem: NoteItem

    private var noteState = NoteState()

    /**
     * App doesn't have ranks if size == 1.
     */
    private var rankDialogItemArray: Array<String> = arrayOf()

    private val iconState = IconState()

    /**
     * TODO remove
     */
    private var isVisible = true

    override fun onSetup(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID
        color = bundle?.getInt(Intent.COLOR, Default.COLOR) ?: Default.COLOR

        if (color == Default.COLOR) {
            color = interactor.defaultColor
        }

        callback?.apply {
            setupBinding(interactor.theme)
            setupToolbar(interactor.theme, color)
            setupEnter(inputControl)
            setupRecycler(inputControl)

            showToolbarVisibleIcon(isShow = false)
        }

        viewModelScope.launch {
            /**
             * If first open.
             */
            if (!::noteItem.isInitialized) {
                rankDialogItemArray = interactor.getRankDialogItemArray()

                if (id == Default.ID) {
                    noteItem = NoteItem.getCreate(interactor.defaultColor, NoteType.ROLL)
                    restoreItem = noteItem.deepCopy()

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
                }

                /**
                 * TODO remove
                 */
                isVisible = Random.nextBoolean()
            }

            callback?.setupDialog(rankDialogItemArray)
            callback?.setupProgress()

            iconState.notAnimate { setupEditMode(noteState.isEdit) }

            callback?.showToolbarVisibleIcon(isShow = true)
            callback?.setToolbarVisibleIcon(isVisible, needAnim = false)

            callback?.notifyDataSetChanged(getList())
            callback?.onBindingLoad(rankEmpty = rankDialogItemArray.size == 1)
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        interactor.onDestroy()
        parentCallback = null
        saveControl.setSaveEvent(isWork = false)
    }


    override fun onSaveData(bundle: Bundle) {
        bundle.apply {
            putLong(Intent.ID, id)
            putInt(Intent.COLOR, color)
        }
    }

    override fun onResume() {
        if (noteState.isEdit) {
            saveControl.setSaveEvent(isWork = true)
        }
    }

    override fun onPause() {
        if (noteState.isEdit) {
            saveControl.onPauseSave()
            saveControl.setSaveEvent(isWork = false)
        }
    }


    override fun onClickBackArrow() {
        if (!noteState.isCreate && noteState.isEdit && id != Default.ID) {
            callback?.hideKeyboard()
            onRestoreData()
        } else {
            saveControl.needSave = false
            parentCallback?.finish()
        }
    }

    /**
     * FALSE - will call super.onBackPress()
     */
    override fun onPressBack(): Boolean {
        if (!noteState.isEdit) return false

        /**
         * If note can't be saved and activity will be closed.
         */
        saveControl.needSave = false

        return if (!onMenuSave(changeMode = true)) {
            if (!noteState.isCreate) onRestoreData() else false
        } else {
            true
        }
    }

    private fun onRestoreData(): Boolean {
        if (id == Default.ID) return false

        /**
         * Get color before restore data.
         */
        val colorFrom = noteItem.color
        noteItem = restoreItem.deepCopy()

        callback?.notifyDataSetChanged(getList())
        setupEditMode(isEdit = false)
        callback?.tintToolbar(colorFrom, noteItem.color)

        parentCallback?.onUpdateNoteColor(noteItem.color)

        inputControl.reset()

        return true
    }


    /**
     * TODO
     */
    override fun onClickVisible() {
        isVisible = !isVisible

        callback?.setToolbarVisibleIcon(isVisible, needAnim = true)

        notifyListByVisible()
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
        if (callback?.openState?.value == true || !noteState.isEdit) return

        val enterText = callback?.getEnterText()?.clearSpace() ?: ""

        if (enterText.isEmpty()) return

        callback?.clearEnterText()

        val p = if (simpleClick) noteItem.rollList.size else 0
        val rollItem = RollItem(position = p, text = enterText)

        inputControl.onRollAdd(p, rollItem.toJson())
        noteItem.rollList.add(p, rollItem)

        callback?.apply {
            onBindingInput(noteItem, inputControl.access)
            scrollToItem(simpleClick, p, getList())
        }
    }

    override fun onClickItemCheck(p: Int) {
        if (noteState.isEdit) return

        val correctPosition = getCorrectPosition(p)
        noteItem.onItemCheck(correctPosition)

        /**
         * If not update [restoreItem] it will cause bug with restore.
         */
        restoreItem = noteItem.deepCopy()

        if (isVisible) {
            callback?.notifyItemChanged(getList(), p)
        } else {
            callback?.notifyItemRemoved(getList(), p)
        }

        callback?.updateProgress(noteItem.getCheck(), noteItem.rollList.size)

        viewModelScope.launch { interactor.updateRollCheck(noteItem, correctPosition) }
    }

    override fun onLongClickItemCheck() {
        if (noteState.isEdit) return

        val check = noteItem.onItemLongCheck()

        /**
         * If not update [restoreItem] it will cause bug with restore.
         */
        restoreItem = noteItem.deepCopy()

        callback?.apply {
            changeCheckToggle(state = true)
            notifyDataRangeChanged(getList())
            changeCheckToggle(state = false)

            updateProgress(noteItem.getCheck(), noteItem.rollList.size)
        }

        notifyListByVisible()

        viewModelScope.launch { interactor.updateRollCheck(noteItem, check) }
    }

    //region Results of dialogs

    override fun onResultColorDialog(check: Int) {
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback?.apply {
            onBindingInput(noteItem, inputControl.access)
            tintToolbar(check)
        }
    }

    override fun onResultRankDialog(check: Int) {
        viewModelScope.launch {
            val rankId = interactor.getRankId(check)

            inputControl.onRankChange(noteItem.rankId, noteItem.rankPs, rankId, check)

            noteItem.apply {
                this.rankId = rankId
                this.rankPs = check
            }

            callback?.apply {
                onBindingInput(noteItem, inputControl.access)
                onBingingNote(noteItem)
            }
        }
    }

    override fun onResultDateDialog(calendar: Calendar) {
        viewModelScope.launch { callback?.showTimeDialog(calendar, interactor.getDateList()) }
    }

    override fun onResultDateDialogClear() {
        viewModelScope.launch {
            interactor.clearDate(noteItem)
            bindInteractor.notifyInfoBind(callback)
        }

        noteItem.clearAlarm()

        callback?.onBingingNote(noteItem)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.beforeNow()) return

        viewModelScope.launch {
            interactor.setDate(noteItem, calendar)
            restoreItem = noteItem.deepCopy()

            callback?.onBingingNote(noteItem)

            bindInteractor.notifyInfoBind(callback)
        }
    }

    override fun onResultConvertDialog() {
        viewModelScope.launch {
            interactor.convert(noteItem)
            parentCallback?.onConvertNote()
        }
    }

    //endregion

    /**
     * Calls on note notification cancel from status bar for update bind indicator.
     */
    override fun onReceiveUnbindNote(id: Long) {
        if (this.id != id) return

        noteItem.apply { isStatus = false }
        restoreItem.apply { isStatus = false }

        callback?.onBingingNote(noteItem)
    }

    //region Menu click

    override fun onMenuRestore() {
        viewModelScope.launch {
            interactor.restoreNote(noteItem)
            parentCallback?.finish()
        }
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteItem.restore()

        iconState.notAnimate { setupEditMode(isEdit = false) }

        viewModelScope.launch { interactor.updateNote(noteItem, updateBind = false) }
    }

    override fun onMenuClear() {
        viewModelScope.launch {
            interactor.clearNote(noteItem)
            parentCallback?.finish()
        }
    }


    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    private fun onMenuUndoRedo(isUndo: Boolean) {
        if (callback?.openState?.value == true || !noteState.isEdit) return

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

    private fun onMenuUndoRedoRank(item: InputItem, isUndo: Boolean) {
        val list = StringConverter().toList(item[isUndo])

        noteItem.apply {
            rankId = list[0]
            rankPs = list[1].toInt()
        }
    }

    private fun onMenuUndoRedoColor(item: InputItem, isUndo: Boolean) {
        val colorFrom = noteItem.color
        val colorTo = item[isUndo].toInt()

        noteItem.color = colorTo

        callback?.tintToolbar(colorFrom, colorTo)
    }

    private fun onMenuUndoRedoName(item: InputItem, isUndo: Boolean) {
        val text = item[isUndo]
        val cursor = item.cursor[isUndo]

        callback?.changeName(text, cursor)
    }

    private fun onMenuUndoRedoRoll(item: InputItem, isUndo: Boolean) {
        val rollItem = noteItem.rollList.getOrNull(item.p) ?: return
        val position = getList().correctIndexOf(rollItem) ?: return

        rollItem.text = item[isUndo]

        if (isVisible || (!isVisible && !rollItem.isCheck)) {
            callback?.notifyItemChanged(getList(), position, item.cursor[isUndo])
        }
    }

    private fun onMenuUndoRedoAdd(item: InputItem) {
        val rollItem = noteItem.rollList.getOrNull(item.p) ?: return
        val position = getList().correctIndexOf(rollItem) ?: return

        noteItem.rollList.removeAtOrNull(item.p) ?: return

        if (isVisible || (!isVisible && !rollItem.isCheck)) {
            callback?.notifyItemRemoved(getList(), position)
        }
    }

    private fun onMenuUndoRedoRemove(item: InputItem, isUndo: Boolean) {
        val rollItem = RollItem[item[isUndo]] ?: return

        noteItem.rollList.add(item.p, rollItem)

        if (isVisible) {
            callback?.notifyItemInserted(getList(), item.p, rollItem.text.length)
        } else if (!rollItem.isCheck) {
            fun getShiftPosition(p: Int): Int {
                return p - noteItem.rollList.subList(0, p).let { it.size - it.hide().size }
            }

            val position = getShiftPosition(item.p)
            callback?.notifyItemInserted(getList(), position, rollItem.text.length)
        }
    }

    private fun onMenuUndoRedoMove(item: InputItem, isUndo: Boolean) {
        val from = item[!isUndo].toInt()
        val to = item[isUndo].toInt()

        val rollItem = noteItem.rollList.getOrNull(from) ?: return

        val shiftFrom = getList().indexOf(rollItem)
        noteItem.rollList.move(from, to)
        val shiftTo = getList().indexOf(rollItem)

        if (isVisible || (!isVisible && !rollItem.isCheck)) {
            callback?.notifyItemMoved(getList(), shiftFrom, shiftTo)
        }
    }


    override fun onMenuRank() {
        if (!noteState.isEdit) return

        callback?.showRankDialog(check = noteItem.rankPs + 1)
    }

    override fun onMenuColor() {
        if (!noteState.isEdit) return

        callback?.showColorDialog(noteItem.color, interactor.theme)
    }

    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (changeMode && callback?.openState?.value == true) return false

        if (!noteState.isEdit || !noteItem.isSaveEnabled()) return false

        noteItem.onSave()

        /**
         * Need update adapter after remove rows with empty text.
         */
        callback?.setList(getList())

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
            restoreItem = noteItem.deepCopy()

            if (noteState.isCreate) {
                noteState.isCreate = NoteState.ND_CREATE

                id = noteItem.id
                parentCallback?.onUpdateNoteId(id)
            }

            callback?.setList(getList())
        }

        return true
    }


    override fun onMenuNotification() {
        if (noteState.isEdit) return

        callback?.showDateDialog(noteItem.alarmDate.getCalendar(), noteItem.haveAlarm())
    }

    override fun onMenuBind() {
        if (callback?.openState?.value == true || noteState.isEdit) return

        noteItem.switchStatus()

        /**
         * If not update [restoreItem] it will cause bug with restore.
         */
        restoreItem = noteItem.deepCopy()

        callback?.onBindingEdit(noteState.isEdit, noteItem)

        viewModelScope.launch { interactor.updateNote(noteItem, updateBind = true) }
    }

    override fun onMenuConvert() {
        if (noteState.isEdit) return

        callback?.showConvertDialog()
    }

    override fun onMenuDelete() {
        if (callback?.openState?.value == true || noteState.isEdit) return

        viewModelScope.launch {
            interactor.deleteNote(noteItem)
            parentCallback?.finish()

            bindInteractor.notifyInfoBind(callback)
        }
    }

    override fun onMenuEdit() {
        if (callback?.openState?.value == true || noteState.isEdit) return

        setupEditMode(isEdit = true)
    }

    private fun setupEditMode(isEdit: Boolean) = inputControl.makeNotEnabled {
        noteState.isEdit = isEdit

        callback?.apply {
            setToolbarBackIcon(
                    isCancel = isEdit && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            onBindingEdit(isEdit, noteItem)
            onBindingInput(noteItem, inputControl.access)
            updateNoteState(noteState)

            if (isEdit) {
                focusOnEdit(noteState.isCreate)
            } else {
                updateProgress(noteItem.getCheck(), noteItem.rollList.size)
            }
        }

        saveControl.needSave = true
        saveControl.setSaveEvent(isEdit)
    }

    //endregion

    override fun onResultSaveControl() {
        callback?.showSaveToast(onMenuSave(changeMode = false))
    }

    override fun onInputTextChange() {
        callback?.onBindingInput(noteItem, inputControl.access)
    }

    override fun onInputRollChange(p: Int, text: String) {
        val correctPosition = getCorrectPosition(p)
        noteItem.rollList.getOrNull(correctPosition)?.text = text

        callback?.apply {
            setList(getList())
            onBindingInput(noteItem, inputControl.access)
        }
    }

    override fun onRollActionNext() {
        callback?.onFocusEnter()
    }

    //region Touch callbacks

    override fun onTouchGetFlags(drag: Boolean) = ItemTouchHelper.Callback.makeMovementFlags(
            if (noteState.isEdit && drag) ItemTouchHelper.UP or ItemTouchHelper.DOWN else 0,
            if (noteState.isEdit) ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT else 0
    )

    /**
     * All item positions updates after call [onMenuSave], because it's hard
     * to control in Edit.
     */
    override fun onTouchSwipe(p: Int) {
        val correctPosition = getCorrectPosition(p)
        val item = noteItem.rollList.removeAtOrNull(correctPosition) ?: return

        inputControl.onRollRemove(correctPosition, item.toJson())

        callback?.apply {
            onBindingInput(noteItem, inputControl.access)
            notifyItemRemoved(getList(), p)
        }
    }

    /**
     * All item positions updates after call [onMenuSave], because it's hard
     * to control in Edit.
     */
    override fun onTouchMove(from: Int, to: Int): Boolean {
        val correctFrom = getCorrectPosition(from)
        val correctTo = getCorrectPosition(to)

        noteItem.rollList.move(correctFrom, correctTo)

        callback?.notifyItemMoved(getList(), from, to)
        return true
    }

    override fun onTouchMoveResult(from: Int, to: Int) {
        val correctFrom = getCorrectPosition(from)
        val correctTo = getCorrectPosition(to)

        inputControl.onRollMove(correctFrom, correctTo)

        callback?.onBindingInput(noteItem, inputControl.access)
    }

    //endregion

    /**
     * Use only for different notify functions. Don't use for change data.
     */
    private fun getList(): MutableList<RollItem> {
        return noteItem.rollList.let { if (isVisible) it else it.hide() }
    }

    /**
     * If have hide items when need correct position.
     */
    private fun getCorrectPosition(p: Int): Int {
        return if (isVisible) p else noteItem.rollList.let { it.indexOf(it.hide()[p]) }
    }

    private fun MutableList<RollItem>.hide(): MutableList<RollItem> {
        return ArrayList(filter { !it.isCheck })
    }

    /**
     * Make good animation for items, remove or insert one by one.
     */
    private fun notifyListByVisible() = viewModelScope.launch {
        val list = ArrayList(noteItem.rollList)

        if (isVisible) {
            list.filter { it.isCheck }.forEach { item ->
                list.correctIndexOf(item)?.also { callback?.notifyItemInserted(list, it) }
            }
        } else {
            while (list.any { it.isCheck }) {
                list.correctIndexOfFirst { it.isCheck }?.also {
                    list.removeAtOrNull(it) ?: return@also
                    callback?.notifyItemRemoved(list, it)
                }
            }
        }
    }

    companion object {
        @VisibleForTesting
        fun NoteItem.onSave() {
            rollList.apply {
                removeAll { it.text.clearSpace().isEmpty() }
                forEachIndexed { i, item ->
                    item.position = i
                    item.text = item.text.clearSpace()
                }
            }

            name = name.clearSpace()
            updateTime().updateComplete()
        }

        @VisibleForTesting
        fun NoteItem.onItemCheck(p: Int) {
            rollList[p].apply { isCheck = !isCheck }
            updateTime().updateComplete()
        }

        /**
         * If have some unchecked items - need turn them to true. Otherwise uncheck all items.
         */
        @VisibleForTesting
        fun NoteItem.onItemLongCheck(): Boolean {
            val check = rollList.any { !it.isCheck }

            updateTime().updateCheck(check)

            return check
        }
    }

}