package sgtmelon.scriptum.app.screen.note.text

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.control.InputControl
import sgtmelon.scriptum.app.control.SaveControl
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.NoteData
import sgtmelon.scriptum.office.intf.InputTextWatcher
import sgtmelon.scriptum.office.state.NoteState
import sgtmelon.scriptum.office.utils.PrefUtils
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import java.util.*

/**
 * ViewModel для [TextNoteFragmentNew]
 */
class TextNoteViewModel(application: Application) : AndroidViewModel(application),
        MenuCallback,
        InputTextWatcher.Result {

    private val context: Context = application.applicationContext

    private val prefUtils = PrefUtils(context)
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: TextNoteCallback

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
            val noteItem = NoteItem(context.getTime(), prefUtils.defaultColor, NoteType.TEXT)
            val statusItem = StatusItem(context, noteItem, false)

            noteRepo = NoteRepo(noteItem, ArrayList(), statusItem)

            noteState = NoteState(isCreate = true)
        } else {
            noteRepo = iRoomRepo.getNoteRepo(id)

            noteState = NoteState(isCreate = false, isBin = noteRepo.noteItem.isBin)
        }

        callback.setupBinding(noteRepo.noteItem, rankVisibleList.isEmpty())
        callback.setupToolbar(noteRepo.noteItem.color, noteState)
        callback.setupDialog(iRoomRepo.getRankDialogName())

        onEditClick(noteState.isEdit)
        noteState.isFirst = false
    }

    fun saveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onTextResult() =
            callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)

    override fun onRestoreClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRestoreOpenClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClearClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUndoClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRedoClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRankClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onColorClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSaveClick(changeMode: Boolean): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConvertClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDeleteClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)

        saveControl.setSaveHandlerEvent(mode)

        inputControl.setEnabled(true)
        inputControl.isChangeEnabled = true
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


    fun onClickConvertDialog() {
        val noteItem = noteRepo.noteItem

        val db = RoomDb.provideDb(context)
        val listRoll = db.daoRoll().insert(noteItem.id, noteItem.text)

        noteItem.change = context.getTime()
        noteItem.type = NoteType.ROLL
        noteItem.setText(0, listRoll.size)

        db.daoNote().update(noteItem)
        db.close()

        noteRepo.listRoll = listRoll

        // TODO
//        noteCallback.viewModel.noteRepo = noteRepo
//        noteCallback.setupFragment(false)
    }

    fun onClickColorDialog(check: Int) {
        val noteItem = noteRepo.noteItem
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
        callback.tintToolbar(check)
    }

    fun onClickRankDialog(check: BooleanArray) {
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

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
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


    lateinit var noteCallback: NoteCallback

    fun getNoteColor(): Int = noteRepo.noteItem.color

    fun onMenuRank(): BooleanArray {
        val db = RoomDb.provideDb(context)
        val check = db.daoRank().getCheck(noteRepo.noteItem.rankId)
        db.close()

        return check
    }

    fun onMenuBind() {
        val noteItem = noteRepo.noteItem

        noteItem.isStatus = !noteItem.isStatus
        noteRepo.updateStatus(noteItem.isStatus)

        val db = RoomDb.provideDb(context)
        db.daoNote().update(noteItem.id, noteItem.isStatus)
        db.close()

        noteRepo.noteItem = noteItem
//        noteCallback.viewModel.noteRepo = noteRepo
    }

}