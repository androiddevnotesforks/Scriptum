package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.app.Application
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.data.NoteData.Default
import sgtmelon.scriptum.model.data.NoteData.Intent
import sgtmelon.scriptum.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.presentation.screen.ui.callback.note.text.ITextNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.ParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.room.converter.model.StringConverter
import java.util.*

/**
 * ViewModel for [TextNoteFragment].
 */
class TextNoteViewModel(application: Application) : ParentViewModel<ITextNoteFragment>(application),
        ITextNoteViewModel {

    private var parentCallback: INoteChild? = null

    fun setParentCallback(callback: INoteChild?) {
        parentCallback = callback
    }

    private lateinit var interactor: ITextNoteInteractor
    private lateinit var bindInteractor: IBindInteractor

    fun setInteractor(interactor: ITextNoteInteractor, bindInteractor: IBindInteractor) {
        this.interactor = interactor
        this.bindInteractor = bindInteractor
    }


    private val saveControl by lazy { SaveControl(context, interactor.getSaveModel(), callback = this) }
    private val inputControl = InputControl()

    private var id: Long = Default.ID
    private var color: Int = Default.COLOR

    private lateinit var noteItem: NoteItem
    private lateinit var restoreItem: NoteItem

    private var noteState = NoteState()

    /**
     * App doesn't have ranks if size == 1.
     */
    private var rankDialogItemArray: Array<String> = arrayOf()

    private val iconState = IconState()

    override fun onSetup(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID
        color = bundle?.getInt(Intent.COLOR, Default.COLOR) ?: Default.COLOR

        if (color == Default.COLOR) {
            color = interactor.defaultColor
        }

        callback?.apply {
            setupBinding(interactor.theme)
            setupToolbar(interactor.theme, color)
            setupEnter(inputControl)
        }

        viewModelScope.launch {
            /**
             * If first open
             */
            if (!::noteItem.isInitialized) {
                rankDialogItemArray = interactor.getRankDialogItemArray()

                if (id == Default.ID) {
                    noteItem = NoteItem.getCreate(interactor.defaultColor, NoteType.TEXT)
                    restoreItem = noteItem.deepCopy()

                    noteState = NoteState(isCreate = true)
                } else {
                    interactor.getItem(id)?.let {
                        noteItem = it
                        restoreItem = it.deepCopy()
                    } ?: run {
                        parentCallback?.finish()
                        return@launch
                    }

                    noteState = NoteState(isBin = noteItem.isBin)
                }
            }

            callback?.setupDialog(rankDialogItemArray)

            iconState.notAnimate { setupEditMode(noteState.isEdit) }

            callback?.onBindingLoad(rankEmpty = rankDialogItemArray.size == 1)
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        interactor.onDestroy()
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
        noteItem = restoreItem.deepCopy()

        setupEditMode(isEdit = false)
        callback?.tintToolbar(colorFrom, noteItem.color)

        parentCallback?.onUpdateNoteColor(noteItem.color)

        inputControl.reset()

        return true
    }

    //region Results of dialogs

    override fun onResultColorDialog(check: Int) {
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback?.apply {
            onBindingInput(noteItem, inputControl.access)
            tintToolbar(check)
        }
    }

    override fun onResultRankDialog(check: Int) {
        viewModelScope.launch {
            val rankId = interactor.getRankId(check)

            inputControl.onRankChange(noteItem.rankId, noteItem.rankPs, rankId, check)

            noteItem.apply {
                this.rankId = rankId
                this.rankPs = check
            }

            callback?.apply {
                onBindingInput(noteItem, inputControl.access)
                onBindingNote(noteItem)
            }
        }
    }

    override fun onResultDateDialog(calendar: Calendar) {
        viewModelScope.launch { callback?.showTimeDialog(calendar, interactor.getDateList()) }
    }

    override fun onResultDateDialogClear() {
        viewModelScope.launch {
            interactor.clearDate(noteItem)
            bindInteractor.notifyInfoBind(callback)
        }

        noteItem.clearAlarm()

        callback?.onBindingNote(noteItem)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.beforeNow()) return

        viewModelScope.launch {
            interactor.setDate(noteItem, calendar)
            restoreItem = noteItem.deepCopy()

            callback?.onBindingNote(noteItem)

            bindInteractor.notifyInfoBind(callback)
        }
    }

    override fun onResultConvertDialog() {
        viewModelScope.launch {
            interactor.convert(noteItem)
            parentCallback?.onConvertNote()
        }
    }

    //endregion

    /**
     * Calls on note notification cancel from status bar for update bind indicator.
     */
    override fun onReceiveUnbindNote(id: Long) {
        if (this.id != id) return

        noteItem.apply { isStatus = false }
        restoreItem.apply { isStatus = false }

        callback?.onBindingNote(noteItem)
    }

    //region Menu click

    override fun onMenuRestore() {
        viewModelScope.launch {
            interactor.restoreNote(noteItem)
            parentCallback?.finish()
        }
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteItem.restore()

        iconState.notAnimate { setupEditMode(isEdit = false) }

        viewModelScope.launch { interactor.updateNote(noteItem, updateBind = false) }
    }

    override fun onMenuClear() {
        viewModelScope.launch {
            interactor.clearNote(noteItem)
            parentCallback?.finish()
        }
    }


    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    private fun onMenuUndoRedo(isUndo: Boolean) {
        if (callback?.isDialogOpen == true || !noteState.isEdit) return

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

        callback?.onBindingInput(noteItem, inputControl.access)
    }

    override fun onMenuRank() {
        if (!noteState.isEdit) return

        callback?.showRankDialog(check = noteItem.rankPs + 1)
    }

    override fun onMenuColor() {
        if (!noteState.isEdit) return

        callback?.showColorDialog(noteItem.color, interactor.theme)
    }

    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (changeMode && callback?.isDialogOpen == true) return false

        if (!noteState.isEdit || !noteItem.isSaveEnabled()) return false

        noteItem.onSave()

        if (changeMode) {
            callback?.hideKeyboard()
            setupEditMode(isEdit = false)
            inputControl.reset()
        } else if (noteState.isCreate) {
            /**
             * Change toolbar icon from arrow to cancel.
             */
            callback?.setToolbarBackIcon(isCancel = true, needAnim = true)
        }

        parentCallback?.onUpdateNoteColor(noteItem.color)

        viewModelScope.launch {
            interactor.saveNote(noteItem, noteState.isCreate)
            restoreItem = noteItem.deepCopy()

            if (noteState.isCreate) {
                noteState.isCreate = NoteState.ND_CREATE

                id = noteItem.id
                parentCallback?.onUpdateNoteId(id)
            }
        }

        return true
    }


    override fun onMenuNotification() {
        if (noteState.isEdit) return

        callback?.showDateDialog(noteItem.alarmDate.getCalendar(), noteItem.haveAlarm())
    }

    override fun onMenuBind() {
        if (callback?.isDialogOpen == true || noteState.isEdit) return

        noteItem.switchStatus()

        /**
         * If not update [restoreItem] it will cause bug with restore.
         */
        restoreItem = noteItem.deepCopy()

        callback?.onBindingEdit(noteState.isEdit, noteItem)

        viewModelScope.launch { interactor.updateNote(noteItem, updateBind = true) }
    }

    override fun onMenuConvert() {
        if (noteState.isEdit) return

        callback?.showConvertDialog()
    }

    override fun onMenuDelete() {
        if (callback?.isDialogOpen == true || noteState.isEdit) return

        viewModelScope.launch {
            interactor.deleteNote(noteItem)
            parentCallback?.finish()

            bindInteractor.notifyInfoBind(callback)
        }
    }

    override fun onMenuEdit() {
        if (callback?.isDialogOpen == true || noteState.isEdit) return

        setupEditMode(isEdit = true)
    }

    private fun setupEditMode(isEdit: Boolean) =  inputControl.makeNotEnabled {
        noteState.isEdit = isEdit

        callback?.apply {
            setToolbarBackIcon(
                    isCancel = isEdit && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            onBindingEdit(isEdit, noteItem)
            onBindingInput(noteItem, inputControl.access)

            if (isEdit) focusOnEdit(noteState.isCreate)
        }

        saveControl.setSaveHandlerEvent(isEdit)
    }

    //endregion

    override fun onResultSaveControl() {
        callback?.showSaveToast(onMenuSave(changeMode = false))
    }

    override fun onInputTextChange() {
        callback?.onBindingInput(noteItem, inputControl.access)
    }

    companion object {
        @VisibleForTesting
        fun NoteItem.onSave() {
            name = name.clearSpace()
            updateTime()
        }
    }

}