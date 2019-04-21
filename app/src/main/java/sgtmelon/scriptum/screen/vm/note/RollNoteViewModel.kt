package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.control.touch.RollTouchControl
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.item.StatusItem
import sgtmelon.scriptum.model.key.InputAction
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.CheckState
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.office.utils.AppUtils.showToast
import sgtmelon.scriptum.office.utils.AppUtils.swap
import sgtmelon.scriptum.office.utils.HelpUtils.Note.getCheck
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import sgtmelon.scriptum.room.converter.StringConverter
import sgtmelon.scriptum.screen.callback.note.NoteCallback
import sgtmelon.scriptum.screen.callback.note.roll.RollNoteCallback
import sgtmelon.scriptum.screen.callback.note.roll.RollNoteMenuCallback
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.watcher.InputTextWatcher

/**
 * ViewModel для [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
class RollNoteViewModel(application: Application) : ParentViewModel(application),
        SaveControl.Result,
        InputTextWatcher.TextChange,
        RollWriteHolder.TextChange,
        RollTouchControl.Result,
        RollNoteMenuCallback {

    lateinit var callback: RollNoteCallback
    lateinit var noteCallback: NoteCallback

    private val inputControl = InputControl()
    private val saveControl = SaveControl(context, result = this)

    private var id: Long = NoteData.Default.ID

    private lateinit var noteModel: NoteModel

    private lateinit var noteState: NoteState
    private lateinit var rankIdVisibleList: List<Long>

    private val iconState = IconState()

    private val checkState = CheckState()
    private val isSaveEnable get() = noteModel.listRoll.size != 0

    fun setupData(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        if (!::noteModel.isInitialized) {
            rankIdVisibleList = iRoomRepo.getRankIdVisibleList()

            if (id == NoteData.Default.ID) {
                val noteItem = NoteItem(
                        create = context.getTime(),
                        color = preference.defaultColor,
                        type = NoteType.ROLL
                )
                val statusItem = StatusItem(context, noteItem, notify = false)

                noteModel = NoteModel(noteItem, ArrayList(), statusItem)

                noteState = NoteState(isCreate = true)
            } else {
                noteModel = iRoomRepo.getNoteModel(id)
                noteModel.updateStatus(rankIdVisibleList)

                noteState = NoteState(isCreate = false, isBin = noteModel.noteItem.isBin)
            }
        }

        callback.apply {
            setupBinding(rankIdVisibleList.isEmpty())
            setupToolbar(noteModel.noteItem.color, noteState)
            setupDialog(iRoomRepo.getRankNameList())
            setupEnter(inputControl)
            setupRecycler(inputControl)
        }

        iconState.notAnimate { onMenuEdit(noteState.isEdit) }
    }

    fun saveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onResultSaveControl() = context.showToast(when (onMenuSave(changeMode = false)) {
        true -> R.string.toast_note_save_done
        false -> R.string.toast_note_save_error
    })

    override fun onResultInputTextChange() =
            callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)

    override fun onResultInputRollChange() =
            callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)

    override fun onResultInputRollAfter(p: Int, text: String) { // TODO handler (чтобы была возможность написать что-то обратно
        with(noteModel.listRoll) {
            if (text.isEmpty()) {
                callback.notifyItemRemoved(p, apply { removeAt(p) })
            } else {
                callback.notifyListItem(p, get(p).apply { this.text = text })
            }
        }
    }

    override fun onResultTouchFlags(drag: Boolean): Int {
        val flagsDrag = when (noteState.isEdit && drag) {
            true -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
            false -> 0
        }

        val flagsSwipe = when (noteState.isEdit) {
            true -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            false -> 0
        }

        return ItemTouchHelper.Callback.makeMovementFlags(flagsDrag, flagsSwipe)
    }

    override fun onResultTouchClear(dragFrom: Int, dragTo: Int) {
        inputControl.onRollMove(dragFrom, dragTo)

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
    }

    override fun onResultTouchSwipe(p: Int) {
        val listRoll = noteModel.listRoll

        inputControl.onRollRemove(p, listRoll[p].toString())

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)

        listRoll.removeAt(p)

        callback.notifyItemRemoved(p, listRoll)
    }

    override fun onResultTouchMove(from: Int, to: Int): Boolean {
        callback.notifyItemMoved(from, to, noteModel.listRoll.apply { swap(from, to) })
        return true
    }

    override fun onMenuRestore() {
        iRoomRepo.restoreNote(noteModel.noteItem)
        noteCallback.finish()
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteModel.noteItem.apply {
            change = context.getTime()
            isBin = false
        }

        iconState.notAnimate { onMenuEdit(editMode = false) }

        iRoomRepo.updateNote(noteModel.noteItem)
    }

    override fun onMenuClear() {
        iRoomRepo.clearNote(noteModel.noteItem)

        noteModel.updateStatus(status = false)

        noteCallback.finish()
    }

    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    private fun onMenuUndoRedo(isUndo: Boolean) {
        val item = if (isUndo) inputControl.undo() else inputControl.redo()

        if (item != null) inputControl.makeNotEnabled {
            val noteItem = noteModel.noteItem
            val listRoll = noteModel.listRoll

            when (item.tag) {
                InputAction.rank -> noteItem.rankId = StringConverter().toList(item[isUndo])
                InputAction.color -> {
                    val colorFrom = noteItem.color
                    val colorTo = item[isUndo].toInt()

                    noteItem.color = colorTo

                    callback.tintToolbar(colorFrom, colorTo)
                }
                InputAction.name -> callback.changeName(item[isUndo], item.cursor!![isUndo])
                InputAction.roll -> {
                    listRoll[item.p].text = item[isUndo]
                    callback.notifyItemChanged(item.p, item.cursor!![isUndo], listRoll)
                }
                InputAction.rollAdd, InputAction.rollRemove -> {
                    val isAddUndo = isUndo && item.tag == InputAction.rollAdd
                    val isRemoveRedo = !isUndo && item.tag == InputAction.rollRemove

                    if (isAddUndo || isRemoveRedo) {
                        listRoll.removeAt(item.p)
                        callback.notifyItemRemoved(item.p, listRoll)
                    } else {
                        val rollItem = RollItem[item[isUndo]]

                        listRoll.add(item.p, rollItem)
                        callback.notifyItemInserted(item.p, rollItem.text.length, listRoll)
                    }
                }
                InputAction.rollMove -> {
                    val from = item[!isUndo].toInt()
                    val to = item[isUndo].toInt()

                    listRoll.swap(from, to)
                    callback.notifyItemMoved(from, to, listRoll)
                }
            }
        }

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
    }

    override fun onMenuRank() =
            callback.showRankDialog(iRoomRepo.getRankCheckArray(noteModel.noteItem))

    override fun onMenuColor() = callback.showColorDialog(noteModel.noteItem.color)

    override fun onMenuSave(changeMode: Boolean): Boolean {
        val listRoll = noteModel.listRoll

        if (noteModel.listRoll.size == 0) return false

        noteModel.noteItem.apply {
            change = context.getTime()
            setCompleteText(listRoll.getCheck(), listRoll.size)
        }

        /**
         * Переход в режим просмотра
         */
        if (changeMode) {
            callback.hideKeyboard()
            onMenuEdit(false)
            inputControl.reset()
        }

        noteModel = iRoomRepo.saveRollNote(noteModel, noteState.isCreate)
        noteModel.updateStatus(rankIdVisibleList)

        noteState.ifCreate {
            if (!changeMode) {
                callback.changeToolbarIcon(drawableOn = true, needAnim = true)
            }
        }

        callback.notifyList(listRoll)

        return true
    }

    override fun onMenuCheck() {
        val size: Int = noteModel.listRoll.size
        val isAll = checkState.isAll

        noteModel.updateCheck(!isAll)

        val noteItem = noteModel.noteItem
        noteItem.change = context.getTime()
        noteItem.setCompleteText(if (isAll) 0 else size, size)

        noteModel.updateStatus(rankIdVisibleList)

        iRoomRepo.updateRollCheck(noteItem, !isAll)

        callback.bindNoteItem(noteItem)
        callback.changeCheckToggle(state = true)

        onUpdateData()
    }

    override fun onMenuBind() {
        val noteItem = noteModel.noteItem.apply { isStatus = !isStatus }

        noteModel.updateStatus(noteItem.isStatus)

        callback.bindEdit(noteState.isEdit, noteItem)

        iRoomRepo.updateNote(noteItem)
    }

    override fun onMenuConvert() = callback.showConvertDialog()

    override fun onMenuDelete() {
        noteModel.updateStatus(status = false)

        iRoomRepo.deleteNote(noteModel.noteItem)
        noteCallback.finish()
    }

    override fun onMenuEdit(editMode: Boolean) = inputControl.makeNotEnabled {
        noteState.isEdit = editMode

        callback.apply {
            changeToolbarIcon(
                    drawableOn = editMode && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            bindEdit(editMode, noteModel.noteItem)
            bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
            updateNoteState(noteState)
        }

        saveControl.setSaveHandlerEvent(editMode)
    }

    fun onPause() = saveControl.onPauseSave(noteState.isEdit)

    fun onUpdateData() {
        checkState.setAll(noteModel.listRoll)

        callback.apply {
            notifyDataSetChanged(noteModel.listRoll)
            changeCheckToggle(state = false)
        }
    }

    fun onClickBackArrow() {
        if (!noteState.isCreate && noteState.isEdit && id != NoteData.Default.ID) {
            callback.hideKeyboard()
            onRestoreData()
        } else {
            saveControl.needSave = false
            noteCallback.finish()
        }
    }

    fun onPressBack(): Boolean {
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

        val colorFrom = noteModel.noteItem.color
        noteModel = iRoomRepo.getNoteModel(id)

        callback.notifyDataSetChanged(noteModel.listRoll)
        onMenuEdit(editMode = false)
        callback.tintToolbar(colorFrom, noteModel.noteItem.color)

        inputControl.reset()

        return true
    }

    fun onClickAdd(simpleClick: Boolean) {
        val textEnter = callback.clearEnter()

        if (textEnter.isEmpty()) return

        val p = if (simpleClick) noteModel.listRoll.size else 0
        val rollItem = RollItem().apply {
            noteId = noteModel.noteItem.id
            text = textEnter
        }

        inputControl.onRollAdd(p, rollItem.toString())

        noteModel.listRoll.add(p, rollItem)

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
        callback.scrollToItem(simpleClick, p, noteModel.listRoll)
    }

    fun onClickItemCheck(p: Int) {
        val listRoll = noteModel.listRoll

        val rollItem = listRoll[p]
        rollItem.isCheck = !rollItem.isCheck

        callback.notifyListItem(p, rollItem)

        val noteItem = noteModel.noteItem
        val check = listRoll.getCheck()

        noteItem.change = context.getTime()
        noteItem.setCompleteText(check, listRoll.size)

        if (checkState.setAll(check, listRoll.size)) callback.bindNoteItem(noteItem)

        noteModel.updateStatus(rankIdVisibleList)

        iRoomRepo.updateRollCheck(noteItem, rollItem)
    }

    fun onResultColorDialog(check: Int) {
        val noteItem = noteModel.noteItem
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        noteModel.updateStatus(rankIdVisibleList)

        callback.apply {
            bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
            tintToolbar(check)
        }
    }

    fun onResultRankDialog(check: BooleanArray) {
        val rankId = ArrayList<Long>()
        val rankPs = ArrayList<Long>()

        iRoomRepo.getRankIdList().forEachIndexed { i, id ->
            if (check[i]) {
                rankId.add(id)
                rankPs.add(i.toLong())
            }
        }

        val noteItem = noteModel.noteItem

        inputControl.onRankChange(noteItem.rankId, rankId)

        noteItem.apply {
            this.rankId = rankId
            this.rankPs = rankPs
        }

        noteModel.updateStatus(rankIdVisibleList)

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
        callback.bindItem(noteItem)
    }

    fun onResultConvertDialog() {
        iRoomRepo.convertToText(noteModel)

        noteCallback.showTextFragment(noteModel.noteItem.id, isSave = false)
    }

}