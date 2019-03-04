package sgtmelon.scriptum.app.screen.note.roll

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.control.SaveControl
import sgtmelon.scriptum.app.control.input.InputControl
import sgtmelon.scriptum.app.control.input.InputTextWatcher
import sgtmelon.scriptum.app.control.touch.RollTouchControl
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.NoteData
import sgtmelon.scriptum.office.state.CheckState
import sgtmelon.scriptum.office.state.NoteState
import sgtmelon.scriptum.office.utils.HelpUtils.Note.getCheck
import sgtmelon.scriptum.office.utils.PrefUtils
import sgtmelon.scriptum.office.utils.TimeUtils.getTime

/**
 * ViewModel для [RollNoteFragment]
 */
class RollNoteViewModel(application: Application) : AndroidViewModel(application),
        InputTextWatcher.Result,
        RollTouchControl.Result,
        MenuCallback {

    private val context: Context = application.applicationContext

    private val prefUtils = PrefUtils(context)
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: RollNoteCallback
    lateinit var noteCallback: NoteCallback

    private val inputControl: InputControl = InputControl()
    private val saveControl: SaveControl = SaveControl(context)

    private var id: Long = NoteData.Default.ID

    private lateinit var noteRepo: NoteRepo

    private lateinit var noteState: NoteState
    private lateinit var rankVisibleList: List<Long>

    private val checkState = CheckState()

    private val isSaveEnable by lazy { noteRepo.listRoll.size != 0 }

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
            setupRecycler()
        }

        onMenuEdit(noteState.isEdit)
        noteState.isFirst = false
    }

    fun saveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onInputTextChangeResult() =
            callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)

    override fun getEditMode() = noteState.isEdit

    override fun onTouchClear(dragFrom: Int, dragTo: Int) {
        inputControl.onRollMove(dragFrom, dragTo)

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
    }

    override fun onTouchSwipe(p: Int) {
        val listRoll = noteRepo.listRoll

        inputControl.onRollRemove(p, listRoll[p].toString())

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)

        listRoll.removeAt(p)

        callback.notifyItemRemoved(p, listRoll)
    }

    override fun onTouchMove(from: Int, to: Int) {
        val listRoll = noteRepo.listRoll
        val rollItem = listRoll[from]

        listRoll.removeAt(from)
        listRoll.add(to, rollItem)

        callback.notifyItemMoved(from, to, listRoll)
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

    override fun onMenuUndo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMenuRedo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        }

        noteRepo = iRoomRepo.saveRollNote(noteRepo, noteState.isCreate)

        // TODO проверить id
        noteRepo.listRoll.forEach {
            Log.i("HERE", "id = ${it.id} | ps = ${it.position} | text = ${it.text}")
        }

        if (noteState.isCreate) {
            noteState.isCreate = false

            if (!changeMode) {
                callback.changeToolbarIcon(drawableOn = true, needAnim = true)
            }
        }

        callback.notifyList(listRoll)

        inputControl.clear()
        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)

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

    fun onUpdateData() {
        checkState.setAll(noteRepo.listRoll)

        callback.apply {
            notifyDataSetChanged(noteRepo.listRoll)
            changeCheckToggle(state = false)
        }
    }

    fun onClickBackArrow() {
        if (!noteState.isCreate && noteState.isEdit) {
            callback.hideKeyboard()

            val colorFrom = noteRepo.noteItem.color

            noteRepo = iRoomRepo.getNoteRepo(id)
            callback.notifyDataSetChanged(noteRepo.listRoll)

            onMenuEdit(mode = false)
            callback.tintToolbar(colorFrom, noteRepo.noteItem.color)

            inputControl.clear()
            callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
        } else {
            saveControl.needSave = false
            noteCallback.finish()
        }
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

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)

        noteRepo.listRoll.add(p, rollItem)

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