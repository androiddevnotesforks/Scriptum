package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.interactor.note.TextNoteInteractor
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.room.converter.StringConverter
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

    var parentCallback: INoteChild? = null

    private val iInteractor: ITextNoteInteractor by lazy { TextNoteInteractor(context, callback) }
    private val iBindInteractor: IBindInteractor by lazy { BindInteractor(context) }

    private val saveControl by lazy { SaveControl(context, iInteractor.getSaveModel(), callback = this) }
    private val inputControl = InputControl()

    private var id: Long = NoteData.Default.ID

    /**
     * TODO replace with nullable
     */
    private lateinit var noteItem: NoteItem
    private var noteState: NoteState = NoteState()
    private var isRankEmpty: Boolean = true

    private val iconState = IconState()

    override fun onSetup(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        /**
         * If first open
         */
        if (!::noteItem.isInitialized) {
            isRankEmpty = iInteractor.isRankEmpty()

            if (id == NoteData.Default.ID) {
                noteItem = NoteItem.getCreate(iInteractor.defaultColor, NoteType.TEXT)
                noteState = NoteState(isCreate = true)
            } else {
                iInteractor.getItem(id, updateBind = true)?.let {
                    noteItem = it
                } ?: run {
                    parentCallback?.finish()
                    return
                }

                noteState = NoteState(isBin = noteItem.isBin)
            }
        }

        callback?.apply {
            setupBinding(iInteractor.theme, isRankEmpty)
            setupToolbar(iInteractor.theme, noteItem.color, noteState)
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

    override fun onResume() {
        if (noteState.isEdit) {
            saveControl.setSaveHandlerEvent(isStart = true)
        }
    }

    override fun onPause() {
        if (noteState.isEdit) {
            saveControl.onPauseSave(noteState.isEdit)
            saveControl.setSaveHandlerEvent(isStart = false)
        }
    }

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

        val colorFrom = noteItem.color

        iInteractor.getItem(id, updateBind = false)?.let {
            noteItem = it
        } ?: run {
            parentCallback?.finish()
            return false
        }

        onMenuEdit(isEdit = false)
        callback?.tintToolbar(colorFrom, noteItem.color)

        inputControl.reset()

        return true
    }

    //region Results of dialogs

    override fun onResultColorDialog(check: Int) {
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback?.apply {
            bindInput(inputControl.access, this@TextNoteViewModel.noteItem)
            tintToolbar(check)
        }
    }

    override fun onResultRankDialog(check: Int) {
        val rankId = iInteractor.getRankId(check)

        inputControl.onRankChange(noteItem.rankId, noteItem.rankPs, rankId, check)

        noteItem.apply {
            this.rankId = rankId
            this.rankPs = check
        }

        callback?.apply {
            bindInput(inputControl.access, this@TextNoteViewModel.noteItem)
            bindNote(this@TextNoteViewModel.noteItem)
        }
    }

    override fun onResultDateDialog(calendar: Calendar) {
        viewModelScope.launch { callback?.showTimeDialog(calendar, iInteractor.getDateList()) }
    }

    override fun onResultDateDialogClear() {
        viewModelScope.launch {
            iInteractor.clearDate(noteItem)
            iBindInteractor.notifyInfoBind(callback)
        }

        noteItem.clearAlarm()

        callback?.bindNote(noteItem)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.beforeNow()) return

        /**
         * TODO check callback успевает ли получить данные
         */
        viewModelScope.launch {
            iInteractor.setDate(noteItem, calendar)
            iBindInteractor.notifyInfoBind(callback)
            callback?.bindNote(noteItem)
        }
    }

    override fun onResultConvertDialog() {
        iInteractor.convert(noteItem)
        parentCallback?.onConvertNote()
    }

    //endregion

    /**
     * Calls on cancel note bind from status bar for update bind indicator
     */
    override fun onCancelNoteBind() {
        callback?.bindNote(noteItem.apply { isStatus = false })
    }

    //region Menu click

    override fun onMenuRestore() {
        noteItem.let { viewModelScope.launch { iInteractor.restoreNote(it) } }
        parentCallback?.finish()
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteItem.restore()

        iconState.notAnimate { onMenuEdit(isEdit = false) }

        viewModelScope.launch { iInteractor.updateNote(noteItem, updateBind = false) }
    }

    override fun onMenuClear() {
        noteItem.let { viewModelScope.launch { iInteractor.clearNote(it) } }
        parentCallback?.finish()
    }


    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    private fun onMenuUndoRedo(isUndo: Boolean) {
        val item = if (isUndo) inputControl.undo() else inputControl.redo()

        if (item != null) inputControl.makeNotEnabled {
            when (item.tag) {
                InputAction.RANK -> {
                    val list = StringConverter().toList(item[isUndo])
                    noteItem.rankId = list[0]
                    noteItem.rankPs = list[1].toInt()
                }
                InputAction.COLOR -> {
                    val colorFrom = noteItem.color
                    val colorTo = item[isUndo].toInt()

                    noteItem.color = colorTo

                    callback?.tintToolbar(colorFrom, colorTo)
                }
                InputAction.NAME -> callback?.changeName(item[isUndo], cursor = item.cursor[isUndo])
                InputAction.TEXT -> callback?.changeText(item[isUndo], cursor = item.cursor[isUndo])
            }
        }

        callback?.bindInput(inputControl.access, noteItem)
    }

    override fun onMenuRank() {
        callback?.showRankDialog(check = noteItem.rankPs + 1)
    }

    override fun onMenuColor() {
        callback?.showColorDialog(noteItem.color, iInteractor.theme)
    }

    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (!noteItem.isSaveEnabled()) return false

        iInteractor.saveNote(noteItem, noteState.isCreate)

        if (changeMode) {
            callback?.hideKeyboard()
            onMenuEdit(isEdit = false)
            inputControl.reset()
        }

        noteState.ifCreate {
            id = noteItem.id
            parentCallback?.onUpdateNoteId(id)

            if (!changeMode) callback?.changeToolbarIcon(drawableOn = true, needAnim = true)
        }

        return true
    }


    override fun onMenuNotification() {
        callback?.showDateDialog(noteItem.alarmDate.getCalendar(), noteItem.haveAlarm())
    }

    override fun onMenuBind() {
        noteItem.apply { isStatus = !isStatus }

        callback?.bindEdit(noteState.isEdit, noteItem)

        viewModelScope.launch { iInteractor.updateNote(noteItem, updateBind = true) }
    }

    override fun onMenuConvert() {
        callback?.showConvertDialog()
    }

    override fun onMenuDelete() {
        viewModelScope.launch {
            iInteractor.deleteNote(noteItem)
            iBindInteractor.notifyInfoBind(callback)
        }

        parentCallback?.finish()
    }

    override fun onMenuEdit(isEdit: Boolean) = inputControl.makeNotEnabled {
        noteState.isEdit = isEdit

        callback?.apply {
            changeToolbarIcon(
                    drawableOn = isEdit && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            bindEdit(isEdit, noteItem)
            bindInput(inputControl.access, noteItem)

            if (isEdit) focusOnEdit()
        }

        saveControl.setSaveHandlerEvent(isEdit)
    }

    //endregion

    override fun onResultSaveControl() = context.showToast(
            if (onMenuSave(changeMode = false)) R.string.toast_note_save_done else R.string.toast_note_save_error
    )

    override fun onInputTextChange() {
        callback?.bindInput(inputControl.access, noteItem)
    }

}