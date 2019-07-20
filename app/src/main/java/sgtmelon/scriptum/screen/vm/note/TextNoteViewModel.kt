package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.extension.getTime
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.room.converter.StringConverter
import sgtmelon.scriptum.screen.view.callback.note.INoteChild
import sgtmelon.scriptum.screen.view.callback.note.text.ITextNoteFragment
import sgtmelon.scriptum.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * ViewModel for [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
class TextNoteViewModel(application: Application) : ParentViewModel<ITextNoteFragment>(application),
        ITextNoteViewModel,
        SaveControl.Result {

    var parentCallback: INoteChild? = null

    private val inputControl = InputControl()
    private val saveControl = SaveControl(context, result = this)

    private var id: Long = NoteData.Default.ID
    private lateinit var noteModel: NoteModel

    private lateinit var noteState: NoteState
    private lateinit var rankIdVisibleList: List<Long>
    private var isRankEmpty: Boolean = true

    private val iconState = IconState()

    override fun onSetupData(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        if (!::noteModel.isInitialized) {
            rankIdVisibleList = iRoomRepo.getRankIdVisibleList()
            isRankEmpty = iRoomRepo.getRankCount()

            if (id == NoteData.Default.ID) {
                noteModel = NoteModel.getCreate(
                        context.getTime(), iPreferenceRepo.defaultColor, NoteType.TEXT
                )

                noteState = NoteState(isCreate = true)
            } else {
                noteModel = iRoomRepo.getNoteModel(id)

                BindControl(context, noteModel.noteEntity).updateBind(rankIdVisibleList)

                noteState = NoteState(isCreate = false, isBin = noteModel.noteEntity.isBin)
            }
        }

        callback?.apply {
            setupBinding(iPreferenceRepo.theme, isRankEmpty)
            setupToolbar(iPreferenceRepo.theme, noteModel.noteEntity.color, noteState)
            setupDialog(iRoomRepo.getRankNameList())
            setupEnter(inputControl)
        }

        iconState.notAnimate { onMenuEdit(noteState.isEdit) }
    }

    override fun onSaveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onResultSaveControl() = context.showToast(
            if (onMenuSave(changeMode = false)) R.string.toast_note_save_done else R.string.toast_note_save_error
    )

    override fun onMenuRestore() {
        noteModel.noteEntity.let { viewModelScope.launch { iRoomRepo.restoreNote(it) } }
        parentCallback?.finish()
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteModel.noteEntity.apply {
            change = context.getTime()
            isBin = false
        }

        iconState.notAnimate { onMenuEdit(editMode = false) }

        iRoomRepo.updateNote(noteModel.noteEntity)
    }

    override fun onMenuClear() {
        noteModel.noteEntity.let { viewModelScope.launch { iRoomRepo.clearNote(it) } }
        parentCallback?.finish()
    }

    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    private fun onMenuUndoRedo(isUndo: Boolean) {
        val item = if (isUndo) inputControl.undo() else inputControl.redo()

        if (item != null) inputControl.makeNotEnabled {
            val noteEntity = noteModel.noteEntity

            when (item.tag) {
                InputAction.rank -> noteEntity.rankId = StringConverter().toList(item[isUndo])
                InputAction.color -> {
                    val colorFrom = noteEntity.color
                    val colorTo = item[isUndo].toInt()

                    noteEntity.color = colorTo

                    callback?.tintToolbar(colorFrom, colorTo)
                }
                InputAction.name -> callback?.changeName(item[isUndo], cursor = item.cursor[isUndo])
                InputAction.text -> callback?.changeText(item[isUndo], cursor = item.cursor[isUndo])
            }
        }

        callback?.bindInput(inputControl.access, noteModel)
    }

    override fun onMenuRank() {
        callback?.showRankDialog(iRoomRepo.getRankCheckArray(noteModel.noteEntity))
    }

    override fun onMenuColor() {
        callback?.showColorDialog(noteModel.noteEntity.color)
    }

    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (!noteModel.isSaveEnabled()) return false

        noteModel.noteEntity.change = context.getTime()

        if (changeMode) {
            callback?.hideKeyboard()
            onMenuEdit(editMode = false)
            inputControl.reset()
        }

        noteModel = iRoomRepo.saveTextNote(noteModel, noteState.isCreate)

        BindControl(context, noteModel.noteEntity).updateBind(rankIdVisibleList)

        noteState.ifCreate {
            id = noteModel.noteEntity.id
            parentCallback?.onUpdateNoteId(id)

            if (!changeMode) callback?.changeToolbarIcon(drawableOn = true, needAnim = true)
        }

        return true
    }

    override fun onMenuNotification() {
        callback?.showDateDialog()
    }

    override fun onMenuBind() = with(noteModel) {
        noteEntity.isStatus = !noteEntity.isStatus

        if (noteEntity.isVisible(rankIdVisibleList)) BindControl(context, noteEntity).updateBind()

        callback?.bindEdit(noteState.isEdit, this)

        iRoomRepo.updateNote(noteEntity)
    }

    override fun onMenuConvert() {
        callback?.showConvertDialog()
    }

    override fun onMenuDelete() {
        noteModel.noteEntity.let {
            viewModelScope.launch { iRoomRepo.deleteNote(it) }
            BindControl(context, it).cancelBind()
        }

        parentCallback?.finish()
    }

    override fun onMenuEdit(editMode: Boolean) = inputControl.makeNotEnabled {
        noteState.isEdit = editMode

        callback?.apply {
            changeToolbarIcon(
                    drawableOn = editMode && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            bindEdit(editMode, noteModel)
            bindInput(inputControl.access, noteModel)

            if (editMode) focusOnEdit()
        }

        saveControl.setSaveHandlerEvent(editMode)
    }

    override fun onResultInputTextChange() {
        callback?.bindInput(inputControl.access, noteModel)
    }
    
    override fun onPause() = saveControl.onPauseSave(noteState.isEdit)

    override fun onDestroy() = saveControl.setSaveHandlerEvent(isStart = false)

    override fun onClickBackArrow() {
        if (!noteState.isCreate && noteState.isEdit && id != NoteData.Default.ID) {
            callback?.hideKeyboard()
            onRestoreData()
        } else {
            saveControl.needSave = false
            parentCallback?.finish()
        }
    }

    override fun onPressBack(): Boolean {
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

        val colorFrom = noteModel.noteEntity.color
        noteModel = iRoomRepo.getNoteModel(id)

        onMenuEdit(editMode = false)
        callback?.tintToolbar(colorFrom, noteModel.noteEntity.color)

        inputControl.reset()

        return true
    }

    override fun onResultColorDialog(check: Int) {
        val noteEntity = noteModel.noteEntity
        inputControl.onColorChange(noteEntity.color, check)
        noteEntity.color = check

        callback?.apply {
            bindInput(inputControl.access, noteModel)
            tintToolbar(check)
        }
    }

    override fun onResultRankDialog(check: BooleanArray) {
        val rankId = ArrayList<Long>()
        val rankPs = ArrayList<Long>()

        iRoomRepo.getRankIdList().forEachIndexed { i, id ->
            if (check[i]) {
                rankId.add(id)
                rankPs.add(i.toLong())
            }
        }

        val noteEntity = noteModel.noteEntity

        inputControl.onRankChange(noteEntity.rankId, rankId)

        noteEntity.apply {
            this.rankId = rankId
            this.rankPs = rankPs
        }

        callback?.apply {
            bindInput(inputControl.access, noteModel)
            bindNote(noteModel)
        }
    }

    override fun onResultConvertDialog() {
        noteModel = iRoomRepo.convertToRoll(noteModel)

        parentCallback?.onConvertNote()
    }

    override fun onCancelNoteBind() {
        callback?.bindNote(noteModel.apply { noteEntity.isStatus = false })
    }

}