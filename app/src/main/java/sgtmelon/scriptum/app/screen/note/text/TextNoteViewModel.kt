package sgtmelon.scriptum.app.screen.note.text

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.control.SaveControl
import sgtmelon.scriptum.app.control.input.InputControl
import sgtmelon.scriptum.app.control.input.InputDef
import sgtmelon.scriptum.app.control.input.InputTextWatcher
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.room.converter.StringConverter
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.NoteData
import sgtmelon.scriptum.office.state.NoteState
import sgtmelon.scriptum.office.utils.PrefUtils
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import java.util.*

/**
 * ViewModel для [TextNoteFragment]
 */
class TextNoteViewModel(application: Application) : AndroidViewModel(application),
        InputTextWatcher.Result,
        MenuCallback {

    private val context: Context = application.applicationContext

    private val prefUtils = PrefUtils(context)
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: TextNoteCallback
    lateinit var noteCallback: NoteCallback

    private val inputControl: InputControl = InputControl()
    private val saveControl: SaveControl = SaveControl(context)

    private var id: Long = NoteData.Default.ID

    private lateinit var noteRepo: NoteRepo

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

        callback.setupBinding(rankVisibleList.isEmpty())
        callback.setupToolbar(noteRepo.noteItem.color, noteState)
        callback.setupDialog(iRoomRepo.getRankDialogName())
        callback.setupEnter(inputControl)

        onEditClick(noteState.isEdit)
        noteState.isFirst = false
    }

    fun saveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onInputTextChangeResult() =
            callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)

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

    override fun onUndoClick() { // TODO сократить в один приватный метод
        val inputItem = inputControl.undo()

        if (inputItem != null) {
            inputControl.setEnabled(false)

            val noteItem = noteRepo.noteItem

            when (inputItem.tag) {
                InputDef.rank ->
                    noteItem.rankId = StringConverter().fromString(inputItem.valueFrom)
                InputDef.color -> {
                    val colorFrom = noteItem.color
                    val colorTo = Integer.parseInt(inputItem.valueFrom)

                    noteItem.color = colorTo

                    callback.tintToolbar(colorFrom, colorTo)
                }
                InputDef.name ->
                    callback.changeName(inputItem.valueFrom, inputItem.cursorItem!!.valueFrom)
                InputDef.text ->
                    callback.changeText(inputItem.valueFrom, inputItem.cursorItem!!.valueFrom)
            }

            inputControl.setEnabled(true)
        }

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
    }

    override fun onRedoClick() { // TODO сократить в один приватный метод
        val inputItem = inputControl.redo()

        if (inputItem != null) {
            inputControl.setEnabled(false)

            val noteItem = noteRepo.noteItem

            when (inputItem.tag) {
                InputDef.rank ->
                    noteItem.rankId = StringConverter().fromString(inputItem.valueTo)
                InputDef.color -> {
                    val colorFrom = noteItem.color
                    val colorTo = Integer.parseInt(inputItem.valueTo)

                    noteItem.color = colorTo

                    callback.tintToolbar(colorFrom, colorTo)
                }
                InputDef.name ->
                    callback.changeName(inputItem.valueTo, inputItem.cursorItem!!.valueTo)
                InputDef.text ->
                    callback.changeText(inputItem.valueTo, inputItem.cursorItem!!.valueTo)

            }

            inputControl.setEnabled(true)
        }

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
    }

    override fun onRankClick() =
            callback.showRankDialog(iRoomRepo.getRankCheck(noteRepo.noteItem.rankId))

    override fun onColorClick() = callback.showColorDialog(noteRepo.noteItem.color)

    override fun onSaveClick(changeMode: Boolean): Boolean {
        val noteItem = noteRepo.noteItem

        if (TextUtils.isEmpty(noteItem.text)) return false

        noteItem.change = context.getTime()

        if (changeMode) {
            callback.hideKeyboard()
            onEditClick(mode = false)
        }

        iRoomRepo.saveTextNote(noteItem, noteState.isCreate)?.let { noteItem.id = it }

        if (noteState.isCreate) {
            noteState.isCreate = false

            if (!changeMode) {
                callback.changeToolbarIcon(drawableOn = true, needAnim = true)
            }
        }

        inputControl.clear()
        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)

        return true
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
        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)

        saveControl.setSaveHandlerEvent(mode)

        inputControl.setEnabled(true)
        inputControl.isChangeEnabled = true
    }

    fun onResultColorDialog(check: Int) {
        val noteItem = noteRepo.noteItem
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
        callback.tintToolbar(check)
    }

    fun onResultRankDialog(check: BooleanArray) {
        val idArray: Array<Long> = iRoomRepo.getRankId()

        val rankId = ArrayList<Long>()
        val rankPs = ArrayList<Long>()

        for (i in idArray.indices) {
            if (check[i]) {
                rankId.add(idArray[i])
                rankPs.add(i.toLong())
            }
        }

        val noteItem = noteRepo.noteItem

        inputControl.onRankChange(noteItem.rankId, rankId)

        noteItem.rankId = rankId
        noteItem.rankPs = rankPs

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
    }

    fun onResultConvertDialog() {
        iRoomRepo.convertToRoll(noteRepo.noteItem)

        noteCallback.showRollFragment(noteRepo.noteItem.id, isSave = false)
    }

}