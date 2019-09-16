package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.extension.getDateFormat
import sgtmelon.extension.getTime
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.interactor.note.TextNoteInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.room.converter.StringConverter
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.ui.callback.note.text.ITextNoteFragment
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.note.ITextNoteViewModel
import java.util.*


/**
 * ViewModel for [TextNoteFragment]
 */
class TextNoteViewModel(application: Application) : ParentViewModel<ITextNoteFragment>(application),
        ITextNoteViewModel {

    private val iInteractor: ITextNoteInteractor by lazy { TextNoteInteractor(context, callback) }

    var parentCallback: INoteChild? = null

    private val saveControl = SaveControl(context, callback = this)
    private val inputControl = InputControl()

    private var id: Long = NoteData.Default.ID
    private lateinit var noteModel: NoteModel
    private lateinit var noteState: NoteState
    private var isRankEmpty: Boolean = true

    private val iconState = IconState()

    override fun onSetup(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        /**
         * If first open
         */
        if (!::noteModel.isInitialized) {
            isRankEmpty = iInteractor.isRankEmpty()

            if (id == NoteData.Default.ID) {
                noteModel = NoteModel.getCreate(
                        getTime(), iInteractor.defaultColor, NoteType.TEXT
                )

                noteState = NoteState(isCreate = true)
            } else {
                iInteractor.getModel(id, updateBind = true)?.let {
                    noteModel = it
                } ?: run {
                    parentCallback?.finish()
                    return
                }

                noteState = NoteState(isCreate = false, isBin = noteModel.noteEntity.isBin)
            }
        }

        callback?.apply {
            setupBinding(iInteractor.theme, isRankEmpty)
            setupToolbar(iInteractor.theme, noteModel.noteEntity.color, noteState)
            setupDialog(iInteractor.getRankDialogItemArray())
            setupEnter(inputControl)
        }

        iconState.notAnimate { onMenuEdit(noteState.isEdit) }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        iInteractor.onDestroy()
        parentCallback = null
        saveControl.setSaveHandlerEvent(isStart = false)
    }


    override fun onSaveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onPause() = saveControl.onPauseSave(noteState.isEdit)

    override fun onClickBackArrow() {
        if (!noteState.isCreate && noteState.isEdit && id != NoteData.Default.ID) {
            callback?.hideKeyboard()
            onRestoreData()
        } else {
            saveControl.needSave = false
            parentCallback?.finish()
        }
    }

    /**
     * FALSE - will call super.onBackPress()
     */
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

        iInteractor.getModel(id, updateBind = false)?.let {
            noteModel = it
        } ?: run {
            parentCallback?.finish()
            return false
        }

        onMenuEdit(editMode = false)
        callback?.tintToolbar(colorFrom, noteModel.noteEntity.color)

        inputControl.reset()

        return true
    }

    //region Results of dialogs

    override fun onResultColorDialog(check: Int) {
        val noteEntity = noteModel.noteEntity
        inputControl.onColorChange(noteEntity.color, check)
        noteEntity.color = check

        callback?.apply {
            bindInput(inputControl.access, noteModel)
            tintToolbar(check)
        }
    }

    override fun onResultRankDialog(check: Int) {
        val noteEntity = noteModel.noteEntity

        val rankId = iInteractor.getRankId(check)

        inputControl.onRankChange(noteEntity.rankId, noteEntity.rankPs, rankId, check)

        noteEntity.apply {
            this.rankId = rankId
            this.rankPs = check
        }

        callback?.apply {
            bindInput(inputControl.access, noteModel)
            bindNote(noteModel)
        }
    }

    override fun onResultDateDialog(calendar: Calendar) {
        viewModelScope.launch { callback?.showTimeDialog(calendar, iInteractor.getDateList()) }
    }

    override fun onResultDateDialogClear() {
        viewModelScope.launch { iInteractor.clearDate(noteModel) }

        noteModel.alarmEntity.apply {
            id = AlarmEntity.ND_ID
            date = AlarmEntity.ND_DATE
        }

        callback?.bindNote(noteModel)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.beforeNow()) return

        noteModel.alarmEntity.date = getDateFormat().format(calendar.time)

        viewModelScope.launch { iInteractor.setDate(noteModel, calendar) }

        callback?.bindNote(noteModel)
    }

    override fun onResultConvertDialog() {
        iInteractor.convert(noteModel)
        parentCallback?.onConvertNote()
    }

    //endregion

    /**
     * Calls on cancel note bind from status bar and need update UI
     */
    override fun onCancelNoteBind() {
        callback?.bindNote(noteModel.apply { noteEntity.isStatus = false })
    }

    //region Menu click

    override fun onMenuRestore() {
        noteModel.let { viewModelScope.launch { iInteractor.restoreNote(it) } }
        parentCallback?.finish()
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteModel.noteEntity.apply {
            change = getTime()
            isBin = false
        }

        iconState.notAnimate { onMenuEdit(editMode = false) }

        viewModelScope.launch { iInteractor.updateNote(noteModel, updateBind = false) }
    }

    override fun onMenuClear() {
        noteModel.let { viewModelScope.launch { iInteractor.clearNote(it) } }
        parentCallback?.finish()
    }


    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    private fun onMenuUndoRedo(isUndo: Boolean) {
        val item = if (isUndo) inputControl.undo() else inputControl.redo()

        if (item != null) inputControl.makeNotEnabled {
            val noteEntity = noteModel.noteEntity

            when (item.tag) {
                InputAction.RANK -> {
                    val list = StringConverter().toList(item[isUndo])
                    noteEntity.rankId = list[0]
                    noteEntity.rankPs = list[1].toInt()
                }
                InputAction.COLOR -> {
                    val colorFrom = noteEntity.color
                    val colorTo = item[isUndo].toInt()

                    noteEntity.color = colorTo

                    callback?.tintToolbar(colorFrom, colorTo)
                }
                InputAction.NAME -> callback?.changeName(item[isUndo], cursor = item.cursor[isUndo])
                InputAction.TEXT -> callback?.changeText(item[isUndo], cursor = item.cursor[isUndo])
            }
        }

        callback?.bindInput(inputControl.access, noteModel)
    }

    override fun onMenuRank() {
        callback?.showRankDialog(check = noteModel.noteEntity.rankPs + 1)
    }

    override fun onMenuColor() {
        callback?.showColorDialog(noteModel.noteEntity.color)
    }

    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (!noteModel.isSaveEnabled()) return false

        noteModel.noteEntity.change = getTime()

        if (changeMode) {
            callback?.hideKeyboard()
            onMenuEdit(editMode = false)
            inputControl.reset()
        }

        iInteractor.saveNote(noteModel, noteState.isCreate)

        noteState.ifCreate {
            id = noteModel.noteEntity.id
            parentCallback?.onUpdateNoteId(id)

            if (!changeMode) callback?.changeToolbarIcon(drawableOn = true, needAnim = true)
        }

        return true
    }


    override fun onMenuNotification() {
        val date = noteModel.alarmEntity.date
        callback?.showDateDialog(date.getCalendar(), date.isNotEmpty())
    }

    override fun onMenuBind() {
        noteModel.noteEntity.apply { isStatus = !isStatus }

        callback?.bindEdit(noteState.isEdit, noteModel)

        viewModelScope.launch { iInteractor.updateNote(noteModel, updateBind = true) }
    }

    override fun onMenuConvert() {
        callback?.showConvertDialog()
    }

    override fun onMenuDelete() {
        viewModelScope.launch { iInteractor.deleteNote(noteModel) }
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

    //endregion

    override fun onResultSaveControl() = context.showToast(
            if (onMenuSave(changeMode = false)) R.string.toast_note_save_done else R.string.toast_note_save_error
    )

    override fun onInputTextChange() {
        callback?.bindInput(inputControl.access, noteModel)
    }

}