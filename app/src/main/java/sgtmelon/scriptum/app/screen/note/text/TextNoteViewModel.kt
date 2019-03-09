package sgtmelon.scriptum.app.screen.note.text

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.control.SaveControl
import sgtmelon.scriptum.app.control.input.InputControl
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.model.key.InputAction
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.model.state.IconState
import sgtmelon.scriptum.app.model.state.NoteState
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.room.converter.StringConverter
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.app.watcher.InputTextWatcher
import sgtmelon.scriptum.office.utils.AppUtils.showToast
import sgtmelon.scriptum.office.utils.PrefUtils
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import java.util.*

/**
 * ViewModel для [TextNoteFragment]
 */
class TextNoteViewModel(application: Application) : AndroidViewModel(application),
        SaveControl.Result,
        InputTextWatcher.TextChange,
        MenuCallback {

    private val context: Context = application.applicationContext

    private val prefUtils = PrefUtils(context)
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: TextNoteCallback
    lateinit var noteCallback: NoteCallback

    private val inputControl: InputControl = InputControl()
    private val saveControl: SaveControl = SaveControl(context, result = this)

    private var id: Long = NoteData.Default.ID

    private lateinit var noteModel: NoteModel

    private lateinit var noteState: NoteState
    private lateinit var rankVisibleList: List<Long>

    private val iconState = IconState()

    fun setupData(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        if (::noteModel.isInitialized) return

        rankVisibleList = iRoomRepo.getRankVisibleList()

        if (id == NoteData.Default.ID) {
            val noteItem = NoteItem(context.getTime(), prefUtils.defaultColor, NoteType.TEXT)
            val statusItem = StatusItem(context, noteItem, false)

            noteModel = NoteModel(noteItem, ArrayList(), statusItem)
            noteState = NoteState(isCreate = true)
        } else {
            noteModel = iRoomRepo.getNoteRepo(id)
            noteState = NoteState(isCreate = false, isBin = noteModel.noteItem.isBin)
        }

        callback.apply {
            setupBinding(rankVisibleList.isEmpty())
            setupToolbar(noteModel.noteItem.color, noteState)
            setupDialog(iRoomRepo.getRankDialogName())
            setupEnter(inputControl)
        }

        iconState.notAnimate { onMenuEdit(noteState.isEdit) }
    }

    fun saveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onResultSaveControl() = context.showToast(when (onMenuSave(changeMode = false)) {
        true -> R.string.toast_note_save_done
        false -> R.string.toast_note_save_error
    })

    override fun onResultInputTextChange() =
            callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)

    override fun onMenuRestore() {
        iRoomRepo.restoreNoteItem(noteModel.noteItem.id)
        noteCallback.finish()
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteModel.noteItem.apply {
            change = context.getTime()
            isBin = false
        }

        iconState.notAnimate { onMenuEdit(mode = false) }

        iRoomRepo.updateNoteItem(noteModel.noteItem)
    }

    override fun onMenuClear() {
        iRoomRepo.clearNoteItem(noteModel.noteItem.id)

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
                    noteItem.rankId = StringConverter().fromString(inputItem.getValue(undo))
                InputAction.color -> {
                    val colorFrom = noteItem.color
                    val colorTo = Integer.parseInt(inputItem.getValue(undo))

                    noteItem.color = colorTo

                    callback.tintToolbar(colorFrom, colorTo)
                }
                InputAction.name -> callback.changeName(
                        inputItem.getValue(undo), inputItem.cursorItem!!.getValue(undo)
                )
                InputAction.text -> callback.changeText(
                        inputItem.getValue(undo), inputItem.cursorItem!!.getValue(undo)
                )
            }

            inputControl.setEnabled(true)
        }

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
    }

    override fun onMenuRank() =
            callback.showRankDialog(iRoomRepo.getRankCheck(noteModel.noteItem.rankId))

    override fun onMenuColor() = callback.showColorDialog(noteModel.noteItem.color)

    override fun onMenuSave(changeMode: Boolean): Boolean {
        val noteItem = noteModel.noteItem

        if (TextUtils.isEmpty(noteModel.noteItem.text)) return false

        noteItem.change = context.getTime()

        if (changeMode) {
            callback.hideKeyboard()
            onMenuEdit(mode = false)
            inputControl.clear()
        }

        noteModel = iRoomRepo.saveTextNote(noteModel, noteState.isCreate)

        noteState.ifCreate {
            if (!changeMode) {
                callback.changeToolbarIcon(drawableOn = true, needAnim = true)
            }
        }

        return true
    }

    override fun onMenuBind() {
        val noteItem = noteModel.noteItem
        noteItem.isStatus = !noteItem.isStatus

        noteModel.updateStatus(noteItem.isStatus)

        callback.bindEdit(noteState.isEdit, noteItem)

        iRoomRepo.updateNoteItemBind(noteItem.id, noteItem.isStatus)
    }

    override fun onMenuConvert() = callback.showConvertDialog()

    override fun onMenuDelete() {
        noteModel.updateStatus(status = false)

        iRoomRepo.deleteNoteItem(noteModel.noteItem.id)
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
            bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
        }

        saveControl.setSaveHandlerEvent(mode)

        inputControl.apply {
            setEnabled(true)
            isChangeEnabled = true
        }
    }

    fun onPause() = saveControl.onPauseSave(noteState.isEdit)

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
        noteModel = iRoomRepo.getNoteRepo(id)

        onMenuEdit(mode = false)
        callback.tintToolbar(colorFrom, noteModel.noteItem.color)

        inputControl.clear()

        return true
    }

    fun onResultColorDialog(check: Int) {
        val noteItem = noteModel.noteItem
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback.apply {
            bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
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

        val noteItem = noteModel.noteItem

        inputControl.onRankChange(noteItem.rankId, rankId)

        noteItem.rankId = rankId
        noteItem.rankPs = rankPs

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
    }

    fun onResultConvertDialog() {
        iRoomRepo.convertToRoll(noteModel.noteItem)

        noteCallback.showRollFragment(noteModel.noteItem.id, isSave = false)
    }

}