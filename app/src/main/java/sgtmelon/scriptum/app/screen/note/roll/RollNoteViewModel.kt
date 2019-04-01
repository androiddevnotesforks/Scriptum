package sgtmelon.scriptum.app.screen.note.roll

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.app.control.SaveControl
import sgtmelon.scriptum.app.control.input.InputControl
import sgtmelon.scriptum.app.control.touch.RollTouchControl
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.model.key.InputAction
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.model.state.CheckState
import sgtmelon.scriptum.app.model.state.IconState
import sgtmelon.scriptum.app.model.state.NoteState
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.room.converter.StringConverter
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.app.watcher.InputTextWatcher
import sgtmelon.scriptum.office.utils.AppUtils.showToast
import sgtmelon.scriptum.office.utils.AppUtils.swap
import sgtmelon.scriptum.office.utils.HelpUtils.Note.getCheck
import sgtmelon.scriptum.office.utils.Preference
import sgtmelon.scriptum.office.utils.TimeUtils.getTime

/**
 * ViewModel для [RollNoteFragment]
 *
 * @author SerjantArbuz
 * @version 1.0
 */
class RollNoteViewModel(application: Application) : AndroidViewModel(application),
        SaveControl.Result,
        InputTextWatcher.TextChange,
        RollWriteHolder.TextChange,
        RollTouchControl.Result,
        MenuCallback {

    private val context: Context = application.applicationContext

    private val prefUtils = Preference(context)
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: RollNoteCallback
    lateinit var noteCallback: NoteCallback

    private val inputControl: InputControl = InputControl()
    private val saveControl: SaveControl = SaveControl(context, result = this)

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
                val noteItem = NoteItem(context.getTime(), prefUtils.defaultColor, NoteType.ROLL)
                val statusItem = StatusItem(context, noteItem, false)

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
            if (TextUtils.isEmpty(text)) {
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

        iconState.notAnimate { onMenuEdit(mode = false) }

        iRoomRepo.updateNote(noteModel.noteItem)
    }

    override fun onMenuClear() {
        iRoomRepo.clearNote(noteModel.noteItem)

        noteModel.updateStatus(status = false)

        noteCallback.finish()
    }

    override fun onMenuUndo() = onMenuUndoRedo(undo = true)

    override fun onMenuRedo() = onMenuUndoRedo(undo = false)

    private fun onMenuUndoRedo(undo: Boolean) {
        val inputItem = if (undo) inputControl.undo() else inputControl.redo()

        if (inputItem != null) {
            inputControl.setEnabled(false)

            val noteItem = noteModel.noteItem

            when (inputItem.tag) {
                InputAction.rank ->
                    noteItem.rankId = StringConverter().toList(inputItem.getValue(undo))
                InputAction.color -> {
                    val colorFrom = noteItem.color
                    val colorTo = Integer.parseInt(inputItem.getValue(undo))

                    noteItem.color = colorTo

                    callback.tintToolbar(colorFrom, colorTo)
                }
                InputAction.name -> callback.changeName(
                        inputItem.getValue(undo), inputItem.cursorItem!!.getValue(undo)
                )
                InputAction.roll -> {
                    val p = inputItem.position
                    with(noteModel.listRoll) {
                        get(p).text = inputItem.getValue(undo)
                        callback.notifyItemChanged(
                                p, inputItem.cursorItem!!.getValue(undo), list = this
                        )
                    }
                }
                InputAction.rollAdd, InputAction.rollRemove -> {
                    val p = inputItem.position

                    val tag = inputItem.tag
                    if (tag == InputAction.rollAdd && undo || tag == InputAction.rollRemove && !undo) {
                        with(noteModel.listRoll) {
                            removeAt(p)
                            callback.notifyItemRemoved(p, list = this)
                        }
                    } else {
                        val rollItem = RollItem(inputItem.getValue(undo))
                        with(noteModel.listRoll) {
                            add(p, rollItem)
                            callback.notifyItemInserted(p, rollItem.text.length, list = this)
                        }
                    }
                }
                InputAction.rollMove -> {
                    val from = Integer.parseInt(inputItem.getValue(!undo))
                    val to = Integer.parseInt(inputItem.getValue(undo))

                    with(noteModel.listRoll) {
                        swap(from, to)
                        callback.notifyItemMoved(from, to, list = this)
                    }
                }
            }

            if (inputItem.tag != InputAction.roll) {
                inputControl.setEnabled(true)
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
            inputControl.clear()
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

    override fun onMenuEdit(mode: Boolean) {
        inputControl.apply {
            setEnabled(false)
            isChangeEnabled = false
        }

        noteState.isEdit = mode

        callback.apply {
            changeToolbarIcon(
                    drawableOn = mode && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            bindEdit(mode, noteModel.noteItem)
            bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
            updateNoteState(noteState)
        }

        saveControl.setSaveHandlerEvent(mode)

        inputControl.apply {
            setEnabled(true)
            isChangeEnabled = true
        }
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
        onMenuEdit(mode = false)
        callback.tintToolbar(colorFrom, noteModel.noteItem.color)

        inputControl.clear()

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

        noteItem.rankId = rankId
        noteItem.rankPs = rankPs

        noteModel.updateStatus(rankIdVisibleList)

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
    }

    fun onResultConvertDialog() {
        iRoomRepo.convertToText(noteModel)

        noteCallback.showTextFragment(noteModel.noteItem.id, isSave = false)
    }

}