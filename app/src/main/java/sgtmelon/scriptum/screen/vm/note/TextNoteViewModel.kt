package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.StatusItem
import sgtmelon.scriptum.model.key.InputAction
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import sgtmelon.scriptum.office.utils.showToast
import sgtmelon.scriptum.room.converter.StringConverter
import sgtmelon.scriptum.screen.callback.note.NoteCallback
import sgtmelon.scriptum.screen.callback.note.text.TextNoteCallback
import sgtmelon.scriptum.screen.callback.note.text.TextNoteMenuCallback
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.watcher.InputTextWatcher

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

    private val inputControl = InputControl()
    private val saveControl = SaveControl(context, result = this)

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
                val noteItem = NoteItem(
                        create = context.getTime(),
                        color = preference.defaultColor,
                        type = NoteType.TEXT
                )
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

            when (item.tag) {
                InputAction.rank -> noteItem.rankId = StringConverter().toList(item[isUndo])
                InputAction.color -> {
                    val colorFrom = noteItem.color
                    val colorTo = item[isUndo].toInt()

                    noteItem.color = colorTo

                    callback.tintToolbar(colorFrom, colorTo)
                }
                InputAction.name -> callback.changeName(item[isUndo], item.cursor!![isUndo])
                InputAction.text -> callback.changeText(item[isUndo], item.cursor!![isUndo])
            }
        }

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
    }

    override fun onMenuRank() =
            callback.showRankDialog(iRoomRepo.getRankCheckArray(noteModel.noteItem))

    override fun onMenuColor() = callback.showColorDialog(noteModel.noteItem.color)

    override fun onMenuSave(changeMode: Boolean): Boolean {
        val noteItem = noteModel.noteItem

        if (noteModel.noteItem.text.isEmpty()) return false

        noteItem.change = context.getTime()

        if (changeMode) {
            callback.hideKeyboard()
            onMenuEdit(editMode = false)
            inputControl.reset()
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

    override fun onMenuEdit(editMode: Boolean) = inputControl.makeNotEnabled {
        noteState.isEdit = editMode

        callback.apply {
            changeToolbarIcon(
                    drawableOn = editMode && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            bindEdit(editMode, noteModel.noteItem)
            bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
        }

        saveControl.setSaveHandlerEvent(editMode)
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

        onMenuEdit(editMode = false)
        callback.tintToolbar(colorFrom, noteModel.noteItem.color)

        inputControl.reset()

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
        callback.bindItem(noteItem)
    }

    fun onResultConvertDialog() {
        noteModel = iRoomRepo.convertToRoll(noteModel)

        noteCallback.showRollFragment(noteModel.noteItem.id, isSave = false)
    }

}