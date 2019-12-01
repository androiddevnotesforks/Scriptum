package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.extension.move
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.interactor.note.RollNoteInteractor
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.room.converter.StringConverter
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.ui.callback.note.roll.IRollNoteFragment
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.note.IRollNoteViewModel
import java.util.*

/**
 * ViewModel for [RollNoteFragment]
 */
class RollNoteViewModel(application: Application) : ParentViewModel<IRollNoteFragment>(application),
        IRollNoteViewModel {

    var parentCallback: INoteChild? = null

    private val iInteractor: IRollNoteInteractor by lazy { RollNoteInteractor(context, callback) }
    private val iBindInteractor: IBindInteractor by lazy { BindInteractor(context) }

    private val saveControl by lazy { SaveControl(context, iInteractor.getSaveModel(), callback = this) }
    private val inputControl = InputControl()

    private var id: Long = NoteData.Default.ID

    private lateinit var noteItem: NoteItem
    private var noteState: NoteState = NoteState()
    private var isRankEmpty: Boolean = true

    private val iconState = IconState()

    override fun onSetup(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        /**
         * If first open
         */
        if (!::noteItem.isInitialized) {
            isRankEmpty = iInteractor.isRankEmpty()

            if (id == NoteData.Default.ID) {
                noteItem = NoteItem.getCreate(iInteractor.defaultColor, NoteType.ROLL)
                noteState = NoteState(isCreate = true)
            } else {
                iInteractor.getModel(id, updateBind = true)?.let {
                    noteItem = it
                } ?: run {
                    parentCallback?.finish()
                    return
                }

                noteState = NoteState(isBin = noteItem.isBin)
            }
        }

        callback?.apply {
            setupBinding(iInteractor.theme, isRankEmpty)
            setupToolbar(iInteractor.theme, noteItem.color, noteState)
            setupDialog(iInteractor.getRankDialogItemArray())
            setupEnter(inputControl)
            setupRecycler(inputControl)
        }

        iconState.notAnimate { onMenuEdit(noteState.isEdit) }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        iInteractor.onDestroy()
        parentCallback = null
        saveControl.setSaveHandlerEvent(isStart = false)
    }


    override fun onSaveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onResume() {
        if (noteState.isEdit) {
            saveControl.setSaveHandlerEvent(isStart = true)
        }
    }

    override fun onPause() {
        if (noteState.isEdit) {
            saveControl.onPauseSave(noteState.isEdit)
            saveControl.setSaveHandlerEvent(isStart = false)
        }
    }

    override fun onUpdateData() {
        callback?.apply {
            notifyDataSetChanged(noteItem.rollList)
            changeCheckToggle(state = false)
        }
    }

    override fun onClickBackArrow() {
        if (!noteState.isCreate && noteState.isEdit && id != NoteData.Default.ID) {
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

        saveControl.needSave = false

        return if (!onMenuSave(changeMode = true)) {
            if (!noteState.isCreate) onRestoreData() else false
        } else {
            true
        }
    }

    private fun onRestoreData(): Boolean {
        if (id == NoteData.Default.ID) return false

        /**
         * Get color before restore data.
         */
        val colorFrom = noteItem.color

        iInteractor.getModel(id, updateBind = false)?.let {
            noteItem = it
        } ?: run {
            parentCallback?.finish()
            return false
        }

        callback?.notifyDataSetChanged(noteItem.rollList)
        onMenuEdit(isEdit = false)
        callback?.tintToolbar(colorFrom, noteItem.color)

        inputControl.reset()

        return true
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
        val enterText = callback?.getEnterText()?.clearSpace() ?: ""

        if (enterText.isEmpty()) return

        callback?.clearEnterText()

        val p = if (simpleClick) noteItem.rollList.size else 0
        val rollItem = RollItem(position = p, text = enterText)

        inputControl.onRollAdd(p, rollItem.toJson())

        noteItem.rollList.add(p, rollItem)

        callback?.apply {
            onBindingInput(inputControl.access, noteItem)
            scrollToItem(simpleClick, p, noteItem.rollList)
        }
    }

    override fun onClickItemCheck(p: Int) {
        iInteractor.updateRollCheck(noteItem, p)
        callback?.notifyListItem(p, noteItem.rollList[p])
    }

    override fun onLongClickItemCheck() {
        iInteractor.updateRollCheck(noteItem)
        callback?.changeCheckToggle(state = true)

        onUpdateData()
    }

    //region Results of dialogs

    override fun onResultColorDialog(check: Int) {
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback?.apply {
            onBindingInput(inputControl.access, noteItem)
            tintToolbar(check)
        }
    }

    override fun onResultRankDialog(check: Int) {
        val rankId = iInteractor.getRankId(check)

        inputControl.onRankChange(noteItem.rankId, noteItem.rankPs, rankId, check)

        noteItem.apply {
            this.rankId = rankId
            this.rankPs = check
        }

        callback?.apply {
            onBindingInput(inputControl.access, noteItem)
            onBingingNote(noteItem)
        }
    }

    override fun onResultDateDialog(calendar: Calendar) {
        viewModelScope.launch { callback?.showTimeDialog(calendar, iInteractor.getDateList()) }
    }

    override fun onResultDateDialogClear() {
        viewModelScope.launch {
            iInteractor.clearDate(noteItem)
            iBindInteractor.notifyInfoBind(callback)
        }

        noteItem.clearAlarm()

        callback?.onBingingNote(noteItem)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.beforeNow()) return

        /**
         * TODO check callback успевает ли получить данные, прежде чем пользователь сможет открыть диалог
         */
        viewModelScope.launch {
            iInteractor.setDate(noteItem, calendar)
            iBindInteractor.notifyInfoBind(callback)
            callback?.onBingingNote(noteItem)
        }
    }

    override fun onResultConvertDialog() {
        iInteractor.convert(noteItem)
        parentCallback?.onConvertNote()
    }

    //endregion

    /**
     * Calls on cancel note bind from status bar for update bind indicator
     */
    override fun onCancelNoteBind() {
        callback?.onBingingNote(noteItem.apply { isStatus = false })
    }

    //region Menu click

    override fun onMenuRestore() {
        /**
         * TODO change workaround
         */
        noteItem.let { viewModelScope.launch { iInteractor.restoreNote(it) } }
        parentCallback?.finish()
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteItem.restore()

        iconState.notAnimate { onMenuEdit(isEdit = false) }

        viewModelScope.launch { iInteractor.updateNote(noteItem, updateBind = false) }
    }

    override fun onMenuClear() {
        /**
         * TODO change workaround
         */
        noteItem.let { viewModelScope.launch { iInteractor.clearNote(it) } }
        parentCallback?.finish()
    }


    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    private fun onMenuUndoRedo(isUndo: Boolean) {
        val item = if (isUndo) inputControl.undo() else inputControl.redo()

        if (item != null) inputControl.makeNotEnabled {
            val rollList = noteItem.rollList

            when (item.tag) {
                InputAction.RANK -> {
                    val list = StringConverter().toList(item[isUndo])
                    noteItem.rankId = list[0]
                    noteItem.rankPs = list[1].toInt()
                }
                InputAction.COLOR -> {
                    val colorFrom = noteItem.color
                    val colorTo = item[isUndo].toInt()

                    noteItem.color = colorTo

                    callback?.tintToolbar(colorFrom, colorTo)
                }
                InputAction.NAME -> callback?.changeName(item[isUndo], cursor = item.cursor[isUndo])
                InputAction.ROLL -> {
                    rollList[item.p].text = item[isUndo]
                    callback?.notifyItemChanged(item.p, cursor = item.cursor[isUndo], list = rollList)
                }
                InputAction.ROLL_ADD, InputAction.ROLL_REMOVE -> {
                    val isAddUndo = isUndo && item.tag == InputAction.ROLL_ADD
                    val isRemoveRedo = !isUndo && item.tag == InputAction.ROLL_REMOVE

                    if (isAddUndo || isRemoveRedo) {
                        rollList.removeAt(item.p)
                        callback?.notifyItemRemoved(item.p, rollList)
                    } else {
                        val rollItem = RollItem[item[isUndo]]
                        if (rollItem != null) {
                            rollList.add(item.p, rollItem)
                            callback?.notifyItemInserted(item.p, rollItem.text.length, rollList)
                        }
                    }
                }
                InputAction.ROLL_MOVE -> {
                    val from = item[!isUndo].toInt()
                    val to = item[isUndo].toInt()

                    rollList.move(from, to)
                    callback?.notifyItemMoved(from, to, rollList)
                }
            }
        }

        callback?.onBindingInput(inputControl.access, noteItem)
    }

    override fun onMenuRank() {
        callback?.showRankDialog(check = noteItem.rankPs + 1)
    }

    override fun onMenuColor() {
        callback?.showColorDialog(noteItem.color, iInteractor.theme)
    }

    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (!noteItem.isSaveEnabled()) return false

        iInteractor.saveNote(noteItem, noteState.isCreate)

        /**
         * Change to read mode.
         */
        if (changeMode) {
            callback?.hideKeyboard()
            onMenuEdit(isEdit = false)
            inputControl.reset()
        }

        noteState.ifCreate {
            id = noteItem.id
            parentCallback?.onUpdateNoteId(id)

            if (!changeMode) callback?.changeToolbarIcon(drawableOn = true, needAnim = true)
        }

        callback?.notifyList(noteItem.rollList)

        return true
    }


    override fun onMenuNotification() {
        callback?.showDateDialog(noteItem.alarmDate.getCalendar(), noteItem.haveAlarm())
    }

    override fun onMenuBind() {
        noteItem.apply { isStatus = !isStatus }

        callback?.bindEdit(noteState.isEdit, noteItem)

        viewModelScope.launch { iInteractor.updateNote(noteItem, updateBind = true) }
    }

    override fun onMenuConvert() {
        callback?.showConvertDialog()
    }

    override fun onMenuDelete() {
        viewModelScope.launch {
            iInteractor.deleteNote(noteItem)
            iBindInteractor.notifyInfoBind(callback)
        }

        parentCallback?.finish()
    }

    override fun onMenuEdit(isEdit: Boolean) = inputControl.makeNotEnabled {
        noteState.isEdit = isEdit

        callback?.apply {
            changeToolbarIcon(
                    drawableOn = isEdit && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            bindEdit(isEdit, noteItem)
            onBindingInput(inputControl.access, noteItem)
            updateNoteState(noteState)

            if (isEdit) focusOnEdit()
        }

        saveControl.setSaveHandlerEvent(isEdit)
    }

    //endregion

    override fun onResultSaveControl() = context.showToast(
            if (onMenuSave(changeMode = false)) R.string.toast_note_save_done else R.string.toast_note_save_error
    )

    override fun onInputTextChange() {
        callback?.onBindingInput(inputControl.access, noteItem)
    }

    override fun onInputRollChange(p: Int, text: String) {
        callback?.apply {
            notifyListItem(p, noteItem.rollList[p].apply { this.text = text })
            onBindingInput(inputControl.access, noteItem)
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
        val rollItem = noteItem.rollList.removeAt(p)

        inputControl.onRollRemove(p, rollItem.toJson())

        callback?.apply {
            onBindingInput(inputControl.access, noteItem)
            notifyItemRemoved(p, noteItem.rollList)
        }
    }

    /**
     * All item positions updates after call [onMenuSave], because it's hard
     * to control in Edit.
     */
    override fun onTouchMove(from: Int, to: Int): Boolean {
        callback?.notifyItemMoved(from, to, noteItem.rollList.apply { move(from, to) })
        return true
    }

    override fun onTouchMoveResult(from: Int, to: Int) {
        inputControl.onRollMove(from, to)
        callback?.onBindingInput(inputControl.access, noteItem)
    }

    //endregion

}