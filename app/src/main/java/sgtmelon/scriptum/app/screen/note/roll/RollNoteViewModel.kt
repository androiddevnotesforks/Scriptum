package sgtmelon.scriptum.app.screen.note.roll

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.control.SaveControl
import sgtmelon.scriptum.app.control.input.InputControl
import sgtmelon.scriptum.app.control.input.InputTextWatcher
import sgtmelon.scriptum.app.control.touch.RollTouchControl
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.NoteData
import sgtmelon.scriptum.office.state.NoteState
import sgtmelon.scriptum.office.utils.PrefUtils
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import java.util.*

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

    lateinit var noteRepo: NoteRepo

    private lateinit var noteState: NoteState
    private lateinit var rankVisibleList: List<Long>

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

        callback.setupBinding(rankVisibleList.isEmpty())
        callback.setupToolbar(noteRepo.noteItem.color, noteState)
        callback.setupDialog(iRoomRepo.getRankDialogName())
        callback.setupEnter(inputControl)

        onEditClick(noteState.isEdit)
        noteState.isFirst = false
    }

    fun saveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onInputTextChangeResult() = callback.bindInput(
            inputControl.isUndoAccess, inputControl.isRedoAccess,
            isSaveEnable = noteRepo.listRoll.size != 0
    )

    override fun getEditMode() = noteState.isEdit

    override fun onTouchClear(dragFrom: Int, dragTo: Int) {
        inputControl.onRollMove(dragFrom, dragTo)

        callback.bindInput(
                inputControl.isUndoAccess, inputControl.isRedoAccess,
                isSaveEnable = noteRepo.listRoll.size != 0
        )
    }

    override fun onTouchSwipe(p: Int) {
        val listRoll = noteRepo.listRoll

        inputControl.onRollRemove(p, listRoll[p].toString())

        callback.bindInput(
                inputControl.isUndoAccess, inputControl.isRedoAccess,
                isSaveEnable = noteRepo.listRoll.size != 0
        )

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

    /**
     *
     */


    /**
     *
     */


    /**
     *
     */


    /**
     *
     */


    /**
     *
     */


    override fun onRestoreClick() {
        iRoomRepo.restoreNoteItem(noteRepo.noteItem.id)
        noteCallback.finish()
    }

    override fun onRestoreOpenClick() {
        noteState.isBin = false

        val noteItem = noteRepo.noteItem
        noteItem.change = context.getTime()
        noteItem.isBin = false

        onEditClick(mode = false) // TODO исправить работу иконки назад (происходит анимация)

        iRoomRepo.updateNoteItem(noteItem)
    }

    override fun onClearClick() {
        iRoomRepo.clearNoteItem(noteRepo.noteItem.id)

        noteRepo.updateStatus(status = false)

        noteCallback.finish()
    }

    override fun onUndoClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRedoClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRankClick() =
            callback.showRankDialog(iRoomRepo.getRankCheck(noteRepo.noteItem.rankId))

    override fun onColorClick() = callback.showColorDialog(noteRepo.noteItem.color)

    override fun onSaveClick(changeMode: Boolean): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCheckClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindClick() {
        val noteItem = noteRepo.noteItem
        noteItem.isStatus = !noteItem.isStatus

        noteRepo.updateStatus(noteItem.isStatus)

        callback.bindEdit(noteState.isEdit, noteItem)

        iRoomRepo.updateNoteItemBind(noteItem.id, noteItem.isStatus)
    }

    override fun onConvertClick() = callback.showConvertDialog()

    override fun onDeleteClick() {
        noteRepo.updateStatus(status = false)

        iRoomRepo.deleteNoteItem(noteRepo.noteItem.id)
        noteCallback.finish()
    }

    override fun onEditClick(mode: Boolean) {
        inputControl.setEnabled(false)
        inputControl.isChangeEnabled = false

        noteState.isEdit = mode

        callback.changeToolbarIcon(
                drawableOn = mode && !noteState.isCreate,
                needAnim = !noteState.isCreate && !noteState.isFirst
        )

        callback.bindEdit(mode, noteRepo.noteItem)
        callback.bindInput(
                inputControl.isUndoAccess, inputControl.isRedoAccess,
                isSaveEnable = noteRepo.listRoll.size != 0
        )

        callback.updateNoteState(noteState)

        saveControl.setSaveHandlerEvent(mode)

        inputControl.setEnabled(true)
        inputControl.isChangeEnabled = true
    }

    fun onResultColorDialog(check: Int) {
        val noteItem = noteRepo.noteItem
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback.bindInput(
                inputControl.isUndoAccess, inputControl.isRedoAccess,
                isSaveEnable = noteRepo.listRoll.size != 0
        )
        callback.tintToolbar(check)
    }

    fun onResultRankDialog(check: BooleanArray) {
        val db = RoomDb.provideDb(context)
        val id: Array<Long> = db.daoRank().id
        db.close()

        val rankId = ArrayList<Long>()
        val rankPs = ArrayList<Long>()

        for (i in id.indices) {
            if (check[i]) {
                rankId.add(id[i])
                rankPs.add(i.toLong())
            }
        }

        val noteItem = noteRepo.noteItem

        inputControl.onRankChange(noteItem.rankId, rankId)

        noteItem.rankId = rankId
        noteItem.rankPs = rankPs

        callback.bindInput(
                inputControl.isUndoAccess, inputControl.isRedoAccess,
                isSaveEnable = noteRepo.listRoll.size != 0
        )
    }

    fun onResultConvertDialog() {
        iRoomRepo.convertToText(noteRepo.noteItem)

        noteCallback.showTextFragment(noteRepo.noteItem.id, isSave = false)
    }

    /**
     *
     */


    /**
     *
     */


    /**
     *
     */


    /**
     *
     */


    fun onMenuCheck(isAll: Boolean): NoteItem {
        val size: Int = noteRepo.listRoll.size

        noteRepo.updateCheck(!isAll)

        val noteItem = noteRepo.noteItem
        noteItem.change = context.getTime()
        noteItem.setText(if (isAll) 0 else size, size)

        val db = RoomDb.provideDb(context)
        db.daoRoll().updateAllCheck(noteItem.id, !isAll)
        db.daoNote().update(noteItem)
        db.close()

//        noteCallback.viewModel.noteRepo = noteRepo

        return noteItem
    }

}