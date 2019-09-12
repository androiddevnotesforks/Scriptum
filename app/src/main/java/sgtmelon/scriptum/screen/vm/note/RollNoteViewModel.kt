package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.extension.getDateFormat
import sgtmelon.extension.getTime
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.extension.getCheck
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.extension.swap
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.CheckState
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.room.converter.StringConverter
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.ui.callback.note.roll.IRollNoteFragment
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.note.IRollNoteViewModel
import java.util.*

/**
 * ViewModel for [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
class RollNoteViewModel(application: Application) : ParentViewModel<IRollNoteFragment>(application),
        IRollNoteViewModel,
        SaveControl.Result {

    var parentCallback: INoteChild? = null

    private val inputControl = InputControl()
    private val saveControl = SaveControl(context, result = this)

    private var id: Long = NoteData.Default.ID
    private lateinit var noteModel: NoteModel

    private lateinit var noteState: NoteState
    private lateinit var rankIdVisibleList: List<Long>
    private var isRankEmpty: Boolean = true

    private val iconState = IconState()
    private val checkState = CheckState()

    override fun onSetup(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        if (!::noteModel.isInitialized) {
            rankIdVisibleList = iRoomRepo.getRankIdVisibleList()
            isRankEmpty = iRoomRepo.getRankCount()

            if (id == NoteData.Default.ID) {
                noteModel = NoteModel.getCreate(
                        getTime(), iPreferenceRepo.defaultColor, NoteType.ROLL
                )

                noteState = NoteState(isCreate = true)
            } else {
                iRoomRepo.getNoteModel(id)?.let {
                    noteModel = it
                } ?: run {
                    parentCallback?.finish()
                    return
                }

                BindControl(context, noteModel).updateBind(rankIdVisibleList)

                noteState = NoteState(isCreate = false, isBin = noteModel.noteEntity.isBin)
            }
        }

        callback?.apply {
            setupBinding(iPreferenceRepo.theme, isRankEmpty)
            setupToolbar(iPreferenceRepo.theme, noteModel.noteEntity.color, noteState)
            setupDialog(iRoomRepo.getRankDialogItemArray())
            setupEnter(inputControl)
            setupRecycler(inputControl)
        }

        iconState.notAnimate { onMenuEdit(noteState.isEdit) }
    }

    override fun onSaveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onResultSaveControl() = context.showToast(
            if (onMenuSave(changeMode = false)) R.string.toast_note_save_done else R.string.toast_note_save_error
    )


    override fun onMenuRestore() {
        noteModel.noteEntity.let { viewModelScope.launch { iRoomRepo.restoreNote(it) } }
        parentCallback?.finish()
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteModel.noteEntity.apply {
            change = getTime()
            isBin = false
        }

        iconState.notAnimate { onMenuEdit(editMode = false) }

        iRoomRepo.updateNote(noteModel.noteEntity)
    }

    override fun onMenuClear() {
        noteModel.noteEntity.let { viewModelScope.launch { iRoomRepo.clearNote(it) } }
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
            setCompleteText(rollList.getCheck(), rollList.size)
        }

        /**
         * Переход в режим просмотра
         */
        if (changeMode) {
            callback?.hideKeyboard()
            onMenuEdit(false)
            inputControl.reset()
        }

        noteModel = iRoomRepo.saveRollNote(noteModel, noteState.isCreate)

        BindControl(context, noteModel).updateBind(rankIdVisibleList)

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

    override fun onMenuBind() = with(noteModel) {
        noteEntity.isStatus = !noteEntity.isStatus

        if (noteEntity.isVisible(rankIdVisibleList)) BindControl(context, noteModel).updateBind()

        callback?.bindEdit(noteState.isEdit, noteModel)

        iRoomRepo.updateNote(noteEntity)
    }

    override fun onMenuConvert() {
        callback?.showConvertDialog()
    }

    override fun onMenuDelete() {
        val noteEntity = noteModel.noteEntity

        BindControl(context, noteModel).cancelBind()
        callback?.cancelAlarm(AlarmReceiver.getInstance(context, noteEntity))
        viewModelScope.launch { iRoomRepo.deleteNote(noteEntity) }

        parentCallback?.finish()
    }

    override fun onMenuEdit(editMode: Boolean) = inputControl.makeNotEnabled {
        noteState.isEdit = editMode

        callback?.apply {
            changeToolbarIcon(
                    drawableOn = editMode && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            bindEdit(editMode, noteModel)
            bindInput(inputControl.access, noteModel)
            updateNoteState(noteState)

            if (editMode) focusOnEdit()
        }

        saveControl.setSaveHandlerEvent(editMode)
    }


    override fun onResultInputTextChange() {
        callback?.bindInput(inputControl.access, noteModel)
    }

    override fun onResultInputRollChange(p: Int, text: String) {
        callback?.apply {
            notifyListItem(p, noteModel.rollList[p].apply { this.text = text })
            bindInput(inputControl.access, noteModel)
        }
    }

    override fun onResultTouchFlags(drag: Boolean) = ItemTouchHelper.Callback.makeMovementFlags(
            if (noteState.isEdit && drag) ItemTouchHelper.UP or ItemTouchHelper.DOWN else 0,
            if (noteState.isEdit) ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT else 0
    )

    override fun onResultTouchClear(dragFrom: Int, dragTo: Int) {
        inputControl.onRollMove(dragFrom, dragTo)
        callback?.bindInput(inputControl.access, noteModel)
    }

    override fun onResultTouchSwipe(p: Int) {
        val rollEntity = noteModel.rollList[p]
        noteModel.rollList.removeAt(p)

        inputControl.onRollRemove(p, rollEntity.toString())

        callback?.apply {
            bindInput(inputControl.access, noteModel)
            notifyItemRemoved(p, noteModel.rollList)
        }
    }

    override fun onResultTouchMove(from: Int, to: Int): Boolean {
        callback?.notifyItemMoved(from, to, noteModel.rollList.apply { swap(from, to) })
        return true
    }


    override fun onPause() = saveControl.onPauseSave(noteState.isEdit)

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        parentCallback = null
        saveControl.setSaveHandlerEvent(isStart = false)
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

        iRoomRepo.getNoteModel(id)?.let {
            noteModel = it
        } ?: run {
            parentCallback?.finish()
            return false
        }

        callback?.notifyDataSetChanged(noteModel.rollList)
        onMenuEdit(editMode = false)
        callback?.tintToolbar(colorFrom, noteModel.noteEntity.color)

        inputControl.reset()

        return true
    }


    override fun onEditorClick(i: Int): Boolean {
        val enterText = callback?.getEnterText() ?: ""

        if (enterText.isEmpty() || i != EditorInfo.IME_ACTION_DONE) return false

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

        val noteEntity = noteModel.noteEntity.apply {
            change = getTime()
            setCompleteText(check, rollList.size)
        }

        if (checkState.setAll(check, rollList.size)) callback?.bindNote(noteModel)

        iRoomRepo.updateRollCheck(noteEntity, rollEntity)

        BindControl(context, noteModel).updateBind(rankIdVisibleList)
    }

    override fun onLongClickItemCheck() {
        val size: Int = noteModel.rollList.size
        val isAll = checkState.isAll

        noteModel.updateCheck(!isAll)

        val noteEntity = noteModel.noteEntity.apply {
            change = getTime()
            setCompleteText(if (isAll) 0 else size, size)
        }

        iRoomRepo.updateRollCheck(noteEntity, !isAll)
        BindControl(context, noteModel).updateBind(rankIdVisibleList)

        callback?.apply {
            bindNote(noteModel)
            changeCheckToggle(state = true)
        }

        onUpdateData()
    }

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


        val rankId = if (check != NoteEntity.ND_RANK_PS) {
            iRoomRepo.getRankIdList()[check]
        } else {
            NoteEntity.ND_RANK_ID
        }

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
        viewModelScope.launch {
            callback?.showTimeDialog(calendar, iRoomAlarmRepo.getList().map { it.alarm.date })
        }
    }

    override fun onResultDateDialogClear() {
        iRoomAlarmRepo.delete(noteModel.alarmEntity.noteId)

        noteModel.alarmEntity.apply {
            id = AlarmEntity.ND_ID
            date = AlarmEntity.ND_DATE
        }

        callback?.cancelAlarm(AlarmReceiver.getInstance(context, noteModel.noteEntity))
        callback?.bindNote(noteModel)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.beforeNow()) return

        iRoomAlarmRepo.insertOrUpdate(noteModel.alarmEntity.apply {
            date = getDateFormat().format(calendar.time)
        })

        callback?.setAlarm(calendar, AlarmReceiver.getInstance(context, noteModel.noteEntity))
        callback?.bindNote(noteModel)
    }

    override fun onResultConvertDialog() {
        iRoomRepo.convertToText(noteModel)

        parentCallback?.onConvertNote()
    }

    override fun onCancelNoteBind() {
        callback?.bindNote(noteModel.apply { noteEntity.isStatus = false })
    }

}