package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.control.input.watcher.InputTextWatcher
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.InputAction
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import sgtmelon.scriptum.office.utils.showToast
import sgtmelon.scriptum.room.converter.StringConverter
import sgtmelon.scriptum.screen.callback.note.NoteChildCallback
import sgtmelon.scriptum.screen.callback.note.text.TextNoteCallback
import sgtmelon.scriptum.screen.callback.note.text.TextNoteMenuCallback
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel

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
    lateinit var parentCallback: NoteChildCallback

    private val inputControl = InputControl()
    private val saveControl = SaveControl(context, result = this)

    private var id: Long = NoteData.Default.ID

    private lateinit var noteModel: NoteModel

    private lateinit var noteState: NoteState
    private lateinit var rankIdVisibleList: List<Long>
    private var isRankEmpty: Boolean = true

    private val iconState = IconState()

    fun onSetupData(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        if (!::noteModel.isInitialized) {
            rankIdVisibleList = iRoomRepo.getRankIdVisibleList()
            isRankEmpty = iRoomRepo.getRankCount()

            if (id == NoteData.Default.ID) {
                noteModel = NoteModel(NoteItem(
                        create = context.getTime(),
                        color = preference.defaultColor,
                        type = NoteType.TEXT
                ), ArrayList())

                noteState = NoteState(isCreate = true)
            } else {
                noteModel = iRoomRepo.getNoteModel(id)

                BindControl(context, noteModel.noteItem).updateBind(rankIdVisibleList)

                noteState = NoteState(isCreate = false, isBin = noteModel.noteItem.isBin)
            }
        }

        callback.apply {
            setupBinding(isRankEmpty)
            setupToolbar(noteModel.noteItem.color, noteState)
            setupDialog(iRoomRepo.getRankNameList())
            setupEnter(inputControl)
        }

        iconState.notAnimate { onMenuEdit(noteState.isEdit) }
    }

    fun onSaveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onResultSaveControl() = context.showToast(
            if (onMenuSave(changeMode = false)) R.string.toast_note_save_done else R.string.toast_note_save_error
    )

    override fun onResultInputTextChange() = callback.bindInput(inputControl.access, noteModel.isSaveEnabled())

    override fun onMenuRestore() {
        noteModel.noteItem.let { viewModelScope.launch { iRoomRepo.restoreNote(it) } }
        parentCallback.finish()
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
        noteModel.noteItem.let { viewModelScope.launch { iRoomRepo.clearNote(it) } }
        parentCallback.finish()
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
                InputAction.name -> callback.changeName(item[isUndo], cursor = item.cursor?.get(isUndo)
                        ?: 0)
                InputAction.text -> callback.changeText(item[isUndo], cursor = item.cursor?.get(isUndo)
                        ?: 0)
            }
        }

        callback.bindInput(inputControl.access, noteModel.isSaveEnabled())
    }

    override fun onMenuRank() =
            callback.showRankDialog(iRoomRepo.getRankCheckArray(noteModel.noteItem))

    override fun onMenuColor() = callback.showColorDialog(noteModel.noteItem.color)

    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (!noteModel.isSaveEnabled()) return false

        noteModel.noteItem.change = context.getTime()

        if (changeMode) {
            callback.hideKeyboard()
            onMenuEdit(editMode = false)
            inputControl.reset()
        }

        noteModel = iRoomRepo.saveTextNote(noteModel, noteState.isCreate)

        BindControl(context, noteModel.noteItem).updateBind(rankIdVisibleList)

        noteState.ifCreate {
            id = noteModel.noteItem.id
            parentCallback.onUpdateNoteId(id)

            if (!changeMode) callback.changeToolbarIcon(drawableOn = true, needAnim = true)
        }

        return true
    }

    override fun onMenuBind() = with(noteModel) {
        noteItem.isStatus = !noteItem.isStatus

        BindControl(context, noteItem).updateBind()

        callback.bindEdit(noteState.isEdit, noteItem)

        iRoomRepo.updateNote(noteItem)
    }

    override fun onMenuConvert() = callback.showConvertDialog()

    override fun onMenuDelete() {
        noteModel.noteItem.let {
            viewModelScope.launch { iRoomRepo.deleteNote(it) }
            BindControl(context, it).cancelBind()
        }

        parentCallback.finish()
    }

    override fun onMenuEdit(editMode: Boolean) = inputControl.makeNotEnabled {
        noteState.isEdit = editMode

        callback.apply {
            changeToolbarIcon(
                    drawableOn = editMode && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            bindEdit(editMode, noteModel.noteItem)
            bindInput(inputControl.access, noteModel.isSaveEnabled())

            if (editMode) focusOnEdit()
        }

        saveControl.setSaveHandlerEvent(editMode)
    }

    fun onPause() = saveControl.onPauseSave(noteState.isEdit)

    fun onDestroy() = saveControl.setSaveHandlerEvent(isStart = false)

    fun onClickBackArrow() {
        if (!noteState.isCreate && noteState.isEdit && id != NoteData.Default.ID) {
            callback.hideKeyboard()
            onRestoreData()
        } else {
            saveControl.needSave = false
            parentCallback.finish()
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

        callback.apply {
            bindInput(inputControl.access, noteModel.isSaveEnabled())
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

        callback.bindInput(inputControl.access, noteModel.isSaveEnabled())
        callback.bindItem(noteItem)
    }

    fun onResultConvertDialog() {
        noteModel = iRoomRepo.convertToRoll(noteModel)

        parentCallback.onConvertNote()
    }

    fun onCancelNoteBind() = with(noteModel) {
        noteItem.isStatus = false
        callback.bindItem(noteItem)
    }

}