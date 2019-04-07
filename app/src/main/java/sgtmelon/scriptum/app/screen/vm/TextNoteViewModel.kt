package sgtmelon.scriptum.app.screen.vm

import android.app.Application
import android.os.Bundle
import android.text.TextUtils
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
import sgtmelon.scriptum.app.room.converter.StringConverter
import sgtmelon.scriptum.app.screen.callback.NoteCallback
import sgtmelon.scriptum.app.screen.callback.TextNoteCallback
import sgtmelon.scriptum.app.screen.callback.TextNoteMenuCallback
import sgtmelon.scriptum.app.watcher.InputTextWatcher
import sgtmelon.scriptum.office.utils.AppUtils.showToast
import sgtmelon.scriptum.office.utils.TimeUtils.getTime

/**
 * ViewModel для [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
class TextNoteViewModel(application: Application) : ParentViewModel(application),
        SaveControl.Result,
        InputTextWatcher.TextChange,
        TextNoteMenuCallback {

    lateinit var callback: TextNoteCallback
    lateinit var noteCallback: NoteCallback

    private val inputControl: InputControl = InputControl()
    private val saveControl: SaveControl = SaveControl(context, result = this)

    private var id: Long = NoteData.Default.ID

    private lateinit var noteModel: NoteModel

    private lateinit var noteState: NoteState
    private lateinit var rankIdVisibleList: List<Long>

    private val iconState = IconState()

    fun setupData(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        if (!::noteModel.isInitialized) {
            rankIdVisibleList = iRoomRepo.getRankIdVisibleList()

            if (id == NoteData.Default.ID) {
                val noteItem = NoteItem(context.getTime(), preference.defaultColor, NoteType.TEXT)
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
                InputAction.text -> callback.changeText(
                        inputItem.getValue(undo), inputItem.cursorItem!!.getValue(undo)
                )
            }

            inputControl.setEnabled(true)
        }

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
    }

    override fun onMenuRank() =
            callback.showRankDialog(iRoomRepo.getRankCheckArray(noteModel.noteItem))

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
        noteModel.updateStatus(rankIdVisibleList)

        noteState.ifCreate {
            if (!changeMode) {
                callback.changeToolbarIcon(drawableOn = true, needAnim = true)
            }
        }

        return true
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
        noteModel = iRoomRepo.getNoteModel(id)

        onMenuEdit(mode = false)
        callback.tintToolbar(colorFrom, noteModel.noteItem.color)

        inputControl.clear()

        return true
    }

    fun onResultColorDialog(check: Int) {
        val noteItem = noteModel.noteItem
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        noteModel.updateStatus(rankIdVisibleList)

        callback.apply {
            bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
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

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
    }

    fun onResultConvertDialog() {
        noteModel = iRoomRepo.convertToRoll(noteModel)

        noteCallback.showRollFragment(noteModel.noteItem.id, isSave = false)
    }

}