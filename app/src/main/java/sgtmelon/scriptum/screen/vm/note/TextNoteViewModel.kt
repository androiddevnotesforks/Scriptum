package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.interactor.note.TextNoteInteractor
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.data.NoteData.Default
import sgtmelon.scriptum.model.data.NoteData.Intent
import sgtmelon.scriptum.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.room.converter.model.StringConverter
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

    private var id: Long = Default.ID
    private var color: Int = Default.COLOR

    private lateinit var noteItem: NoteItem
    private var noteState: NoteState = NoteState()
    private var isRankEmpty: Boolean = true

    private val iconState = IconState()

    override fun onSetup(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID
        color = bundle?.getInt(Intent.COLOR, Default.COLOR) ?: Default.COLOR

        if (color == Default.COLOR) {
            color = iInteractor.defaultColor
        }

        callback?.apply {
            setupBinding(iInteractor.theme)
            setupToolbar(iInteractor.theme, color)
            setupEnter(inputControl)
        }

        viewModelScope.launch {
            /**
             * If first open
             */
            if (!::noteItem.isInitialized) {
                isRankEmpty = iInteractor.isRankEmpty()

                if (id == Default.ID) {
                    noteItem = NoteItem.getCreate(iInteractor.defaultColor, NoteType.TEXT)
                    noteState = NoteState(isCreate = true)
                } else {
                    iInteractor.getItem(id, updateBind = true)?.let {
                        noteItem = it
                    } ?: run {
                        parentCallback?.finish()
                        return@launch
                    }

                    noteState = NoteState(isBin = noteItem.isBin)
                }
            }

            callback?.apply {
                setupDialog(iInteractor.getRankDialogItemArray())
                onBindingLoad(isRankEmpty)
            }

            iconState.notAnimate { onMenuEdit(noteState.isEdit) }
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        iInteractor.onDestroy()
        parentCallback = null
        saveControl.setSaveHandlerEvent(isStart = false)
    }


    override fun onSaveData(bundle: Bundle) {
        bundle.apply {
            putLong(Intent.ID, id)
            putInt(Intent.COLOR, color)
        }
    }

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
        if (!noteState.isCreate && noteState.isEdit && id != Default.ID) {
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
        if (id == Default.ID) return false

        /**
         * Get color before restore data.
         */
        val colorFrom = noteItem.color

        viewModelScope.launch {
            iInteractor.getItem(id, updateBind = false)?.let {
                noteItem = it
            } ?: run {
                parentCallback?.finish()
                return@launch
            }

            onMenuEdit(isEdit = false)
            callback?.tintToolbar(colorFrom, noteItem.color)

            parentCallback?.onUpdateNoteColor(noteItem.color)

            inputControl.reset()
        }

        return true
    }

    //region Results of dialogs

    override fun onResultColorDialog(check: Int) {
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback?.apply {
            onBindingInput(inputControl.access, noteItem)
            tintToolbar(check)
        }
    }

    override fun onResultRankDialog(check: Int) {
        viewModelScope.launch {
            val rankId = iInteractor.getRankId(check)

            inputControl.onRankChange(noteItem.rankId, noteItem.rankPs, rankId, check)

            noteItem.apply {
                this.rankId = rankId
                this.rankPs = check
            }

            callback?.apply {
                onBindingInput(inputControl.access, noteItem)
                onBindingNote(noteItem)
            }
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

        callback?.onBindingNote(noteItem)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.beforeNow()) return

        viewModelScope.launch {
            iInteractor.setDate(noteItem, calendar)
            callback?.onBindingNote(noteItem)

            iBindInteractor.notifyInfoBind(callback)
        }
    }

    override fun onResultConvertDialog() {
        viewModelScope.launch {
            iInteractor.convert(noteItem)
            parentCallback?.onConvertNote()
        }
    }

    //endregion

    /**
     * Calls on cancel note bind from status bar for update bind indicator
     */
    override fun onCancelNoteBind() {
        callback?.onBindingNote(noteItem.apply { isStatus = false })
    }

    //region Menu click

    override fun onMenuRestore() {
        viewModelScope.launch {
            iInteractor.restoreNote(noteItem)
            parentCallback?.finish()
        }
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteItem.restore()

        iconState.notAnimate { onMenuEdit(isEdit = false) }

        viewModelScope.launch { iInteractor.updateNote(noteItem, updateBind = false) }
    }

    override fun onMenuClear() {
        viewModelScope.launch {
            iInteractor.clearNote(noteItem)
            parentCallback?.finish()
        }
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

        callback?.onBindingInput(inputControl.access, noteItem)
    }

    override fun onMenuRank() {
        callback?.showRankDialog(check = noteItem.rankPs + 1)
    }

    override fun onMenuColor() {
        callback?.showColorDialog(noteItem.color, iInteractor.theme)
    }

    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (!noteItem.isSaveEnabled()) return false

        noteItem.onSave()

        if (changeMode) {
            callback?.hideKeyboard()
            onMenuEdit(isEdit = false)
            inputControl.reset()
        } else if (noteState.isCreate) {
            /**
             * Change toolbar icon from arrow to cancel.
             */
            callback?.changeToolbarIcon(drawableOn = true, needAnim = true)
        }

        parentCallback?.onUpdateNoteColor(noteItem.color)

        viewModelScope.launch {
            iInteractor.saveNote(noteItem, noteState.isCreate)

            if (noteState.isCreate) {
                noteState.isCreate = NoteState.ND_CREATE

                id = noteItem.id
                parentCallback?.onUpdateNoteId(id)
            }
        }

        return true
    }


    override fun onMenuNotification() {
        callback?.showDateDialog(noteItem.alarmDate.getCalendar(), noteItem.haveAlarm())
    }

    override fun onMenuBind() {
        noteItem.apply { isStatus = !isStatus }

        callback?.onBindingEdit(noteState.isEdit, noteItem)

        viewModelScope.launch { iInteractor.updateNote(noteItem, updateBind = true) }
    }

    override fun onMenuConvert() {
        callback?.showConvertDialog()
    }

    override fun onMenuDelete() {
        viewModelScope.launch {
            iInteractor.deleteNote(noteItem)
            parentCallback?.finish()

            iBindInteractor.notifyInfoBind(callback)
        }
    }

    override fun onMenuEdit(isEdit: Boolean) = inputControl.makeNotEnabled {
        noteState.isEdit = isEdit

        callback?.apply {
            changeToolbarIcon(
                    drawableOn = isEdit && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            onBindingEdit(isEdit, noteItem)
            onBindingInput(inputControl.access, noteItem)

            if (isEdit) focusOnEdit()
        }

        saveControl.setSaveHandlerEvent(isEdit)
    }

    //endregion

    override fun onResultSaveControl() = context.showToast(
            if (onMenuSave(changeMode = false)) R.string.toast_note_save_done else R.string.toast_note_save_error
    )

    override fun onInputTextChange() {
        callback?.onBindingInput(inputControl.access, noteItem)
    }

    companion object {
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun NoteItem.onSave() {
            name = name.clearSpace()
            updateTime()
        }
    }

}