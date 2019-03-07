package sgtmelon.scriptum.app.screen.note.roll

import android.annotation.SuppressLint
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
import sgtmelon.scriptum.app.control.input.InputDef
import sgtmelon.scriptum.app.control.input.InputTextWatcher
import sgtmelon.scriptum.app.control.touch.RollTouchControl
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.model.state.CheckState
import sgtmelon.scriptum.app.model.state.NoteState
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.room.converter.StringConverter
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.office.utils.AppUtils.showToast
import sgtmelon.scriptum.office.utils.AppUtils.swap
import sgtmelon.scriptum.office.utils.HelpUtils.Note.getCheck
import sgtmelon.scriptum.office.utils.PrefUtils
import sgtmelon.scriptum.office.utils.TimeUtils.getTime

/**
 * ViewModel для [RollNoteFragment]
 */
class RollNoteViewModel(application: Application) : AndroidViewModel(application),
        SaveControl.Result,
        InputTextWatcher.TextChange,
        RollWriteHolder.TextChange,
        RollTouchControl.Result,
        MenuCallback {

    private val context: Context = application.applicationContext

    private val prefUtils = PrefUtils(context)
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: RollNoteCallback
    lateinit var noteCallback: NoteCallback

    private val inputControl: InputControl = InputControl()
    private val saveControl: SaveControl = SaveControl(context, result = this)

    private var id: Long = NoteData.Default.ID

    private lateinit var noteRepo: NoteRepo

    private lateinit var noteState: NoteState
    private lateinit var rankVisibleList: List<Long>

    private val checkState = CheckState()

    private val isSaveEnable get() = noteRepo.listRoll.size != 0

    fun setupData(bundle: Bundle?) {
        id = bundle?.getLong(NoteData.Intent.ID, NoteData.Default.ID) ?: NoteData.Default.ID

        if (::noteRepo.isInitialized) return

        rankVisibleList = iRoomRepo.getRankVisibleList()

        if (id == NoteData.Default.ID) {
            val noteItem = NoteItem(context.getTime(), prefUtils.defaultColor, NoteType.ROLL)
            val statusItem = StatusItem(context, noteItem, false)

            noteRepo = NoteRepo(noteItem, ArrayList(), statusItem)

            noteState = NoteState(isCreate = true)
        } else {
            noteRepo = iRoomRepo.getNoteRepo(id)

            noteState = NoteState(isCreate = false, isBin = noteRepo.noteItem.isBin)
        }

        callback.apply {
            setupBinding(rankVisibleList.isEmpty())
            setupToolbar(noteRepo.noteItem.color, noteState)
            setupDialog(iRoomRepo.getRankDialogName())
            setupEnter(inputControl)
            setupRecycler(inputControl)
        }

        onMenuEdit(noteState.isEdit)
        noteState.isFirst = false
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
        with(noteRepo.listRoll) {
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
        val listRoll = noteRepo.listRoll

        inputControl.onRollRemove(p, listRoll[p].toString())

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)

        listRoll.removeAt(p)

        callback.notifyItemRemoved(p, listRoll)
    }

    override fun onResultTouchMove(from: Int, to: Int): Boolean {
        callback.notifyItemMoved(from, to, noteRepo.listRoll.apply { swap(from, to) })
        return true
    }

    override fun onMenuRestore() {
        iRoomRepo.restoreNoteItem(noteRepo.noteItem.id)
        noteCallback.finish()
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteRepo.noteItem.apply {
            change = context.getTime()
            isBin = false
        }

        onMenuEdit(mode = false) // TODO исправить работу иконки назад (происходит анимация)

        iRoomRepo.updateNoteItem(noteRepo.noteItem)
    }

    override fun onMenuClear() {
        iRoomRepo.clearNoteItem(noteRepo.noteItem.id)

        noteRepo.updateStatus(status = false)

        noteCallback.finish()
    }

    override fun onMenuUndo() = onMenuUndoRedo(undo = true)

    override fun onMenuRedo() = onMenuUndoRedo(undo = false)

    @SuppressLint("SwitchIntDef") // TODO
    private fun onMenuUndoRedo(undo: Boolean) {
        val inputItem = if (undo) inputControl.undo() else inputControl.redo()

        if (inputItem != null) {
            inputControl.setEnabled(false)

            val noteItem = noteRepo.noteItem

            when (inputItem.tag) {
                InputDef.rank ->
                    noteItem.rankId = StringConverter().fromString(inputItem.getValue(undo))
                InputDef.color -> {
                    val colorFrom = noteItem.color
                    val colorTo = Integer.parseInt(inputItem.getValue(undo))

                    noteItem.color = colorTo

                    callback.tintToolbar(colorFrom, colorTo)
                }
                InputDef.name -> callback.changeName(
                        inputItem.getValue(undo), inputItem.cursorItem!!.getValue(undo)
                )
                InputDef.roll -> {
                    val p = inputItem.position
                    with(noteRepo.listRoll) {
                        get(p).text = inputItem.getValue(undo)
                        callback.notifyItemChanged(
                                p, inputItem.cursorItem!!.getValue(undo), list = this
                        )
                    }
                }
                InputDef.rollAdd, InputDef.rollRemove -> {
                    val p = inputItem.position

                    val tag = inputItem.tag
                    if (tag == InputDef.rollAdd && undo || tag == InputDef.rollRemove && !undo) {
                        with(noteRepo.listRoll) {
                            removeAt(p)
                            callback.notifyItemRemoved(p, list = this)
                        }
                    } else {
                        val rollItem = RollItem(inputItem.getValue(undo))
                        with(noteRepo.listRoll) {
                            add(p, rollItem)
                            callback.notifyItemInserted(p, rollItem.text.length, list = this)
                        }
                    }
                }
                InputDef.rollMove -> {
                    val from = Integer.parseInt(inputItem.getValue(!undo))
                    val to = Integer.parseInt(inputItem.getValue(undo))

                    with(noteRepo.listRoll) {
                        swap(from, to)
                        callback.notifyItemMoved(from, to, list = this)
                    }
                }
            }

            if (inputItem.tag != InputDef.roll) {
                inputControl.setEnabled(true)
            }
        }

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
    }

    override fun onMenuRank() =
            callback.showRankDialog(iRoomRepo.getRankCheck(noteRepo.noteItem.rankId))

    override fun onMenuColor() = callback.showColorDialog(noteRepo.noteItem.color)

    override fun onMenuSave(changeMode: Boolean): Boolean {
        val listRoll = noteRepo.listRoll

        if (noteRepo.listRoll.size == 0) return false

        noteRepo.noteItem.apply {
            change = context.getTime()
            setText(listRoll.getCheck(), listRoll.size)
        }

        /**
         * Переход в режим просмотра
         */
        if (changeMode) {
            callback.hideKeyboard()
            onMenuEdit(false)
            inputControl.clear()
        }

        noteRepo = iRoomRepo.saveRollNote(noteRepo, noteState.isCreate)

        if (noteState.isCreate) {
            noteState.isCreate = false

            if (!changeMode) {
                callback.changeToolbarIcon(drawableOn = true, needAnim = true)
            }
        }

        callback.notifyList(listRoll)

        return true
    }

    override fun onMenuCheck() {
        val size: Int = noteRepo.listRoll.size
        val isAll = checkState.isAll

        noteRepo.updateCheck(!isAll)

        val noteItem = noteRepo.noteItem
        noteItem.change = context.getTime()
        noteItem.setText(if (isAll) 0 else size, size)

        iRoomRepo.updateRollCheck(noteItem, !isAll)

        callback.bindNoteItem(noteItem)
        callback.changeCheckToggle(state = true)

        onUpdateData()
    }

    override fun onMenuBind() {
        val noteItem = noteRepo.noteItem
        noteItem.isStatus = !noteItem.isStatus

        noteRepo.updateStatus(noteItem.isStatus)

        callback.bindEdit(noteState.isEdit, noteItem)

        iRoomRepo.updateNoteItemBind(noteItem.id, noteItem.isStatus)
    }

    override fun onMenuConvert() = callback.showConvertDialog()

    override fun onMenuDelete() {
        noteRepo.updateStatus(status = false)

        iRoomRepo.deleteNoteItem(noteRepo.noteItem.id)
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
                    needAnim = !noteState.isCreate && !noteState.isFirst
            )

            bindEdit(mode, noteRepo.noteItem)
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
        checkState.setAll(noteRepo.listRoll)

        callback.apply {
            notifyDataSetChanged(noteRepo.listRoll)
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

        val colorFrom = noteRepo.noteItem.color
        noteRepo = iRoomRepo.getNoteRepo(id)

        callback.notifyDataSetChanged(noteRepo.listRoll)
        onMenuEdit(mode = false)
        callback.tintToolbar(colorFrom, noteRepo.noteItem.color)

        inputControl.clear()

        return true
    }

    fun onClickAdd(simpleClick: Boolean) {
        val textEnter = callback.clearEnter()

        if (textEnter.isEmpty()) return

        val p = if (simpleClick) noteRepo.listRoll.size else 0
        val rollItem = RollItem().apply {
            noteId = noteRepo.noteItem.id
            text = textEnter
        }

        inputControl.onRollAdd(p, rollItem.toString())

        noteRepo.listRoll.add(p, rollItem)

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
        callback.scrollToItem(simpleClick, p, noteRepo.listRoll)
    }

    fun onClickItemCheck(p: Int) {
        val listRoll = noteRepo.listRoll

        val rollItem = listRoll[p]
        rollItem.isCheck = !rollItem.isCheck

        callback.notifyListItem(p, rollItem)

        val noteItem = noteRepo.noteItem
        val check = listRoll.getCheck()

        noteItem.change = context.getTime()
        noteItem.setText(check, listRoll.size)

        if (checkState.setAll(check, listRoll.size)) callback.bindNoteItem(noteItem)

        iRoomRepo.updateRollCheck(rollItem, noteItem)
    }

    fun onResultColorDialog(check: Int) {
        val noteItem = noteRepo.noteItem
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback.apply {
            bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
            tintToolbar(check)
        }
    }

    fun onResultRankDialog(check: BooleanArray) {
        val rankId = ArrayList<Long>()
        val rankPs = ArrayList<Long>()

        iRoomRepo.getRankId().forEachIndexed { index, l ->
            if (check[index]) {
                rankId.add(l)
                rankPs.add(index.toLong())
            }
        }

        val noteItem = noteRepo.noteItem

        inputControl.onRankChange(noteItem.rankId, rankId)

        noteItem.rankId = rankId
        noteItem.rankPs = rankPs

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
    }

    fun onResultConvertDialog() {
        iRoomRepo.convertToText(noteRepo.noteItem)

        noteCallback.showTextFragment(noteRepo.noteItem.id, isSave = false)
    }

}