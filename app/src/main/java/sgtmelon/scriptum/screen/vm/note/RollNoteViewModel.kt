package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.extension.getString
import sgtmelon.extension.getTime
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.extension.getCheck
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.extension.swap
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.interactor.note.RollNoteInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.CheckState
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.room.converter.StringConverter
import sgtmelon.scriptum.room.entity.RollEntity
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
    private lateinit var noteModel: NoteModel
    private var noteState: NoteState = NoteState()
    private var isRankEmpty: Boolean = true

    private val iconState = IconState()
    private val checkState = CheckState()

    override fun onSetup(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        /**
         * If first open
         */
        if (!::noteModel.isInitialized) {
            isRankEmpty = iInteractor.isRankEmpty()

            if (id == NoteData.Default.ID) {
                noteModel = NoteModel.getCreate(
                        getTime(), iInteractor.defaultColor, NoteType.ROLL
                )

                noteState = NoteState(isCreate = true)
            } else {
                iInteractor.getModel(id, updateBind = true)?.let {
                    noteModel = it
                } ?: run {
                    parentCallback?.finish()
                    return
                }

                noteState = NoteState(isBin = noteModel.noteEntity.isBin)
            }
        }

        callback?.apply {
            setupBinding(iInteractor.theme, isRankEmpty)
            setupToolbar(iInteractor.theme, noteModel.noteEntity.color, noteState)
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
        checkState.setAll(noteModel.rollList)

        callback?.apply {
            notifyDataSetChanged(noteModel.rollList)
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

        val colorFrom = noteModel.noteEntity.color

        iInteractor.getModel(id, updateBind = false)?.let {
            noteModel = it
        } ?: run {
            parentCallback?.finish()
            return false
        }

        callback?.notifyDataSetChanged(noteModel.rollList)
        onMenuEdit(isEdit = false)
        callback?.tintToolbar(colorFrom, noteModel.noteEntity.color)

        inputControl.reset()

        return true
    }


    override fun onEditorClick(i: Int): Boolean {
        val enterText = callback?.getEnterText() ?: ""

        if (enterText.isEmpty() || i != EditorInfo.IME_ACTION_DONE) {
            onMenuSave(changeMode = true)
            return false
        }

        onClickAdd(simpleClick = true)
        return true
    }

    override fun onClickAdd(simpleClick: Boolean) {
        val enterText = callback?.getEnterText() ?: ""
        callback?.clearEnterText()

        if (enterText.isEmpty()) return

        val p = if (simpleClick) noteModel.rollList.size else 0
        val rollEntity = RollEntity().apply {
            noteId = noteModel.noteEntity.id
            text = enterText
        }

        inputControl.onRollAdd(p, rollEntity.toString())

        noteModel.rollList.add(p, rollEntity)

        callback?.apply {
            bindInput(inputControl.access, noteModel)
            scrollToItem(simpleClick, p, noteModel.rollList)
        }
    }

    override fun onClickItemCheck(p: Int) {
        val rollList = noteModel.rollList
        val rollEntity = rollList[p].apply { isCheck = !isCheck }

        callback?.notifyListItem(p, rollEntity)

        val check = rollList.getCheck()

        noteModel.noteEntity.apply {
            change = getTime()
            setCompleteText(check, rollList.size)
        }

        if (checkState.setAll(check, rollList.size)) callback?.bindNote(noteModel)

        iInteractor.updateRollCheck(noteModel, rollEntity)
    }

    override fun onLongClickItemCheck() {
        val size: Int = noteModel.rollList.size
        val isAll = checkState.isAll

        noteModel.updateCheck(!isAll)
        noteModel.noteEntity.apply {
            change = getTime()
            setCompleteText(if (isAll) 0 else size, size)
        }

        iInteractor.updateRollCheck(noteModel, !isAll)

        callback?.apply {
            bindNote(noteModel)
            changeCheckToggle(state = true)
        }

        onUpdateData()
    }

    //region Results of dialogs

    override fun onResultColorDialog(check: Int) {
        val noteEntity = noteModel.noteEntity
        inputControl.onColorChange(noteEntity.color, check)
        noteEntity.color = check

        callback?.apply {
            bindInput(inputControl.access, noteModel)
            tintToolbar(check)
        }
    }

    override fun onResultRankDialog(check: Int) {
        val noteEntity = noteModel.noteEntity

        val rankId = iInteractor.getRankId(check)

        inputControl.onRankChange(noteEntity.rankId, noteEntity.rankPs, rankId, check)

        noteEntity.apply {
            this.rankId = rankId
            this.rankPs = check
        }

        callback?.apply {
            bindInput(inputControl.access, noteModel)
            bindNote(noteModel)
        }
    }

    override fun onResultDateDialog(calendar: Calendar) {
        viewModelScope.launch { callback?.showTimeDialog(calendar, iInteractor.getDateList()) }
    }

    override fun onResultDateDialogClear() {
        viewModelScope.launch {
            iInteractor.clearDate(noteModel)
            iBindInteractor.notifyInfoBind(callback)
        }

        noteModel.alarmEntity.clear()

        callback?.bindNote(noteModel)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.beforeNow()) return

        noteModel.alarmEntity.date = calendar.getString()

        viewModelScope.launch {
            iInteractor.setDate(noteModel, calendar)
            iBindInteractor.notifyInfoBind(callback)
        }

        callback?.bindNote(noteModel)
    }

    override fun onResultConvertDialog() {
        iInteractor.convert(noteModel)
        parentCallback?.onConvertNote()
    }

    //endregion

    /**
     * Calls on cancel note bind from status bar and need update UI
     */
    override fun onCancelNoteBind() {
        callback?.bindNote(noteModel.apply { noteEntity.isStatus = false })
    }

    //region Menu click

    override fun onMenuRestore() {
        noteModel.let { viewModelScope.launch { iInteractor.restoreNote(it) } }
        parentCallback?.finish()
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteModel.noteEntity.apply {
            change = getTime()
            isBin = false
        }

        iconState.notAnimate { onMenuEdit(isEdit = false) }

        viewModelScope.launch { iInteractor.updateNote(noteModel, updateBind = false) }
    }

    override fun onMenuClear() {
        noteModel.let { viewModelScope.launch { iInteractor.clearNote(it) } }
        parentCallback?.finish()
    }


    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    private fun onMenuUndoRedo(isUndo: Boolean) {
        val item = if (isUndo) inputControl.undo() else inputControl.redo()

        if (item != null) inputControl.makeNotEnabled {
            val noteEntity = noteModel.noteEntity
            val rollList = noteModel.rollList

            when (item.tag) {
                InputAction.RANK -> {
                    val list = StringConverter().toList(item[isUndo])
                    noteEntity.rankId = list[0]
                    noteEntity.rankPs = list[1].toInt()
                }
                InputAction.COLOR -> {
                    val colorFrom = noteEntity.color
                    val colorTo = item[isUndo].toInt()

                    noteEntity.color = colorTo

                    callback?.tintToolbar(colorFrom, colorTo)
                }
                InputAction.NAME -> callback?.changeName(item[isUndo], cursor = item.cursor[isUndo])
                InputAction.ROLL -> {
                    rollList[item.p].text = item[isUndo]
                    callback?.notifyItemChanged(item.p, rollList, cursor = item.cursor[isUndo])
                }
                InputAction.ROLL_ADD, InputAction.ROLL_REMOVE -> {
                    val isAddUndo = isUndo && item.tag == InputAction.ROLL_ADD
                    val isRemoveRedo = !isUndo && item.tag == InputAction.ROLL_REMOVE

                    if (isAddUndo || isRemoveRedo) {
                        rollList.removeAt(item.p)
                        callback?.notifyItemRemoved(item.p, rollList)
                    } else {
                        val rollEntity = RollEntity[item[isUndo]]
                        if (rollEntity != null) {
                            rollList.add(item.p, rollEntity)
                            callback?.notifyItemInserted(item.p, rollEntity.text.length, rollList)
                        }
                    }
                }
                InputAction.ROLL_MOVE -> {
                    val from = item[!isUndo].toInt()
                    val to = item[isUndo].toInt()

                    rollList.swap(from, to)
                    callback?.notifyItemMoved(from, to, rollList)
                }
            }
        }

        callback?.bindInput(inputControl.access, noteModel)
    }

    override fun onMenuRank() {
        callback?.showRankDialog(check = noteModel.noteEntity.rankPs + 1)
    }

    override fun onMenuColor() {
        callback?.showColorDialog(noteModel.noteEntity.color)
    }

    override fun onMenuSave(changeMode: Boolean): Boolean {
        val rollList = noteModel.rollList

        if (!noteModel.isSaveEnabled()) return false

        noteModel.noteEntity.apply {
            change = getTime()
            setCompleteText(rollList)
        }

        /**
         * Change to read mode
         */
        if (changeMode) {
            callback?.hideKeyboard()
            onMenuEdit(isEdit = false)
            inputControl.reset()
        }

        iInteractor.saveNote(noteModel, noteState.isCreate)

        noteState.ifCreate {
            id = noteModel.noteEntity.id
            parentCallback?.onUpdateNoteId(id)

            if (!changeMode) callback?.changeToolbarIcon(drawableOn = true, needAnim = true)
        }

        callback?.notifyList(rollList)

        return true
    }


    override fun onMenuNotification() {
        val date = noteModel.alarmEntity.date
        callback?.showDateDialog(date.getCalendar(), date.isNotEmpty())
    }

    override fun onMenuBind() {
        noteModel.noteEntity.apply { isStatus = !isStatus }

        callback?.bindEdit(noteState.isEdit, noteModel)

        viewModelScope.launch { iInteractor.updateNote(noteModel, updateBind = true) }
    }

    override fun onMenuConvert() {
        callback?.showConvertDialog()
    }

    override fun onMenuDelete() {
        viewModelScope.launch {
            iInteractor.deleteNote(noteModel)
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

            bindEdit(isEdit, noteModel)
            bindInput(inputControl.access, noteModel)
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
        callback?.bindInput(inputControl.access, noteModel)
    }

    override fun onInputRollChange(p: Int, text: String) {
        callback?.apply {
            notifyListItem(p, noteModel.rollList[p].apply { this.text = text })
            bindInput(inputControl.access, noteModel)
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

    override fun onTouchSwipe(p: Int) {
        val rollEntity = noteModel.rollList[p]
        noteModel.rollList.removeAt(p)

        inputControl.onRollRemove(p, rollEntity.toString())

        callback?.apply {
            bindInput(inputControl.access, noteModel)
            notifyItemRemoved(p, noteModel.rollList)
        }
    }

    override fun onTouchMove(from: Int, to: Int): Boolean {
        callback?.notifyItemMoved(from, to, noteModel.rollList.apply { swap(from, to) })
        return true
    }

    override fun onTouchMoveResult(from: Int, to: Int) {
        inputControl.onRollMove(from, to)
        callback?.bindInput(inputControl.access, noteModel)
    }

    //endregion

}