package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.common.test.annotation.RunProtected
import sgtmelon.common.test.idling.impl.AppIdlingResource
import sgtmelon.common.utils.beforeNow
import sgtmelon.common.utils.getCalendar
import sgtmelon.scriptum.data.room.converter.type.StringConverter
import sgtmelon.scriptum.domain.interactor.callback.note.IParentNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.domain.model.data.IntentData.Note.Default
import sgtmelon.scriptum.domain.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.domain.model.item.InputItem
import sgtmelon.scriptum.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.extension.launchBack
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.control.note.save.ISaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IParentNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.note.IParentNoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel
import java.util.*

/**
 * Parent viewModel for [TextNoteViewModel] and [RollNoteViewModel].
 */
abstract class ParentNoteViewModel<N : NoteItem, C : IParentNoteFragment<N>, I : IParentNoteInteractor<N>>(
    callback: C,
    @RunProtected var parentCallback: INoteConnector?,
    @RunProtected val interactor: I
) : ParentViewModel<C>(callback),
    IParentNoteViewModel {

    //region Variables

    /**
     * Abstract because need setup callback but this class not final.
     */
    @RunProtected lateinit var saveControl: ISaveControl
        private set

    /**
     * Need call after [interactor] initialization.
     */
    fun setSaveControl(saveControl: ISaveControl) {
        this.saveControl = saveControl
    }

    @RunProtected var inputControl: IInputControl = InputControl()

    @RunProtected var id: Long = Default.ID
    @RunProtected var color: Int = Default.COLOR

    @RunProtected lateinit var noteItem: N

    /**
     * Item for cash data before enter edit mode (for easy data restore).
     */
    @RunProtected lateinit var restoreItem: N

    @RunProtected var noteState = NoteState()
    @RunProtected var mayAnimateIcon = true

    /**
     * App doesn't have ranks if size == 1.
     */
    @RunProtected var rankDialogItemArray: Array<String> = emptyArray()

    //endregion

    @RunProtected fun isNoteInitialized(): Boolean = ::noteItem.isInitialized

    /**
     * Function must describe cashing data inside [restoreItem].
     *
     * It is important because if not cache data in [restoreItem] it will cause bug with restore.
     * When do changes and click on CHANGE and cancel edit mode by back button or back arrow.
     *
     * Use example: restoreItem = noteItem.deepCopy().
     */
    abstract fun cacheData()

    override fun onSetup(bundle: Bundle?) {
        AppIdlingResource.getInstance().startWork(IdlingTag.Note.LOAD_DATA)

        getBundleData(bundle)
        setupBeforeInitialize()

        viewModelScope.launch {
            if (tryInitializeNote()) {
                setupAfterInitialize()
            }

            AppIdlingResource.getInstance().stopWork(IdlingTag.Note.LOAD_DATA)
        }
    }

    @RunPrivate fun getBundleData(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID
        color = bundle?.getInt(Intent.COLOR, Default.COLOR) ?: Default.COLOR

        if (color == Default.COLOR) {
            color = interactor.defaultColor
        }
    }

    /**
     * Call before [tryInitializeNote]
     */
    abstract fun setupBeforeInitialize()

    /**
     * Return false if happened error while initialize note.
     */
    abstract suspend fun tryInitializeNote(): Boolean

    /**
     * Call after [tryInitializeNote]
     */
    abstract suspend fun setupAfterInitialize()

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        interactor.onDestroy()
        parentCallback = null
        saveControl.setSaveEvent(isWork = false)
    }


    override fun onSaveData(bundle: Bundle) {
        bundle.apply {
            putLong(Intent.ID, id)
            putInt(Intent.COLOR, color)
        }
    }

    override fun onResume() {
        if (noteState.isEdit) {
            saveControl.setSaveEvent(isWork = true)
        }
    }

    override fun onPause() {
        if (parentCallback?.isOrientationChanging() == true) return

        if (noteState.isEdit) {
            saveControl.onPauseSave()
            saveControl.setSaveEvent(isWork = false)
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

        /**
         * If note can't be saved and activity will be closed.
         */
        saveControl.needSave = false

        return if (!onMenuSave(changeMode = true)) {
            if (!noteState.isCreate) onRestoreData() else false
        } else {
            true
        }
    }

    /**
     * Function must describe restoring all data (in code and on screen) after changes
     * was canceled.
     */
    abstract fun onRestoreData(): Boolean

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
            val rankId = runBack { interactor.getRankId(check) }

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
        viewModelScope.launchBack {
            callback?.showTimeDialog(calendar, interactor.getDateList())
        }
    }

    override fun onResultDateDialogClear() {
        viewModelScope.launch {
            runBack { interactor.clearDate(noteItem) }

            callback?.sendCancelAlarmBroadcast(noteItem.id)
            callback?.sendNotifyInfoBroadcast()
        }

        noteItem.clearAlarm()
        cacheData()

        callback?.onBindingNote(noteItem)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.beforeNow()) return

        viewModelScope.launch {
            runBack { interactor.setDate(noteItem, calendar) }
            cacheData()

            callback?.onBindingNote(noteItem)

            callback?.sendSetAlarmBroadcast(noteItem.id, calendar)
            callback?.sendNotifyInfoBroadcast()
        }
    }

    override fun onResultConvertDialog() {
        viewModelScope.launch {
            runBack { interactor.convertNote(noteItem) }
            parentCallback?.onConvertNote()
        }
    }

    //endregion

    /**
     * Calls on note notification cancel from status bar for update bind indicator.
     */
    override fun onReceiveUnbindNote(id: Long) {
        if (this.id != id) return

        noteItem.isStatus = false
        restoreItem.isStatus = false

        callback?.onBindingNote(noteItem)
    }

    //region Menu click

    override fun onMenuRestore() {
        viewModelScope.launch {
            runBack { interactor.restoreNote(noteItem) }
            parentCallback?.finish()
        }
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteItem.onRestore()

        mayAnimateIcon = false
        setupEditMode(isEdit = false)
        mayAnimateIcon = true

        viewModelScope.launchBack { interactor.updateNote(noteItem) }
    }

    override fun onMenuClear() {
        viewModelScope.launch {
            runBack { interactor.clearNote(noteItem) }
            parentCallback?.finish()
        }
    }


    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    @RunPrivate fun onMenuUndoRedo(isUndo: Boolean) {
        if (callback?.isDialogOpen == true || !noteState.isEdit) return

        val item = if (isUndo) inputControl.undo() else inputControl.redo()

        if (item != null) {
            onMenuUndoRedoSelect(item, isUndo)
        }

        callback?.onBindingInput(noteItem, inputControl.access)
    }

    /**
     * Function must describe logic of [isUndo] switch by [InputItem.tag].
     */
    abstract fun onMenuUndoRedoSelect(item: InputItem, isUndo: Boolean)

    @RunProtected fun onMenuUndoRedoRank(item: InputItem, isUndo: Boolean) {
        val list = StringConverter().toList(item[isUndo])

        if (list.size != 2) return

        noteItem.apply {
            rankId = list.firstOrNull() ?: return
            rankPs = list.lastOrNull()?.toInt() ?: return
        }
    }

    @RunProtected fun onMenuUndoRedoColor(item: InputItem, isUndo: Boolean) {
        val colorFrom = noteItem.color
        val colorTo = item[isUndo].toIntOrNull() ?: return

        noteItem.color = colorTo

        callback?.tintToolbar(colorFrom, colorTo)
    }

    @RunProtected fun onMenuUndoRedoName(item: InputItem, isUndo: Boolean) {
        val text = item[isUndo]
        val position = item.cursor[isUndo]

        callback?.changeName(text, position)
    }


    override fun onMenuRank() {
        if (!noteState.isEdit) return

        callback?.showRankDialog(check = noteItem.rankPs + 1)
    }

    override fun onMenuColor() {
        if (!noteState.isEdit) return

        callback?.showColorDialog(noteItem.color)
    }

    /**
     * Function of background work for note saving.
     */
    abstract suspend fun saveBackgroundWork()


    override fun onMenuNotification() {
        if (noteState.isEdit) return

        callback?.showDateDialog(noteItem.alarmDate.getCalendar(), noteItem.haveAlarm())
    }

    override fun onMenuBind() {
        if (callback?.isDialogOpen == true || noteState.isEdit) return

        noteItem.switchStatus()
        cacheData()

        callback?.onBindingEdit(noteItem, noteState.isEdit)

        viewModelScope.launch {
            runBack { interactor.updateNote(noteItem) }

            callback?.sendNotifyNotesBroadcast()
        }
    }

    override fun onMenuConvert() {
        if (noteState.isEdit) return

        callback?.showConvertDialog()
    }

    override fun onMenuDelete() {
        if (callback?.isDialogOpen == true || noteState.isEdit) return

        viewModelScope.launch {
            runBack { interactor.deleteNote(noteItem) }

            callback?.sendCancelAlarmBroadcast(noteItem.id)
            callback?.sendCancelNoteBroadcast(noteItem.id)
            callback?.sendNotifyInfoBroadcast()

            parentCallback?.finish()
        }
    }

    override fun onMenuEdit() {
        if (callback?.isDialogOpen == true || noteState.isEdit) return

        setupEditMode(isEdit = true)
    }

    /**
     * Function must describe changing of edit/read modes.
     */
    abstract fun setupEditMode(isEdit: Boolean)

    //endregion

    override fun onResultSaveControl() {
        callback?.showSaveToast(onMenuSave(changeMode = false))
    }

    /**
     * Need check [isNoteInitialized] for prevent crash. Strange what this function calls before
     * note initialisation, may be it related with view binding.
     */
    override fun onInputTextChange() {
        if (!isNoteInitialized()) return

        callback?.onBindingInput(noteItem, inputControl.access)
    }

}