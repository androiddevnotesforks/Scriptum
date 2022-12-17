package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import java.util.Calendar
import kotlinx.coroutines.launch
import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.runBack
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.IParentNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.ParentNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.types.NumbersJoinConverter
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.note.NoteConnector
import sgtmelon.scriptum.infrastructure.utils.extensions.isFalse
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.test.prod.RunPrivate
import sgtmelon.test.prod.RunProtected

/**
 * Parent viewModel for [TextNoteViewModelImpl] and [RollNoteViewModelImpl].
 */
abstract class ParentNoteViewModelImpl<N : NoteItem, C : IParentNoteFragment<N>>(
    isEdit: Boolean,
    noteState: NoteState,
    id: Long,
    color: Color,

    //TODO cleanup
    callback: C,
    @RunProtected var parentCallback: NoteConnector?,
    @RunProtected val colorConverter: ColorConverter,
    @RunProtected val preferencesRepo: PreferencesRepo,
    private val convertNote: ConvertNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val restoreNote: RestoreNoteUseCase,
    private val clearNote: ClearNoteUseCase,
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val getNotificationDateList: GetNotificationDateListUseCase,
    private val getRankId: GetRankIdUseCase
) : ParentViewModel<C>(callback),
    ParentNoteViewModel {

    override val isEdit: MutableLiveData<Boolean> = MutableLiveData(isEdit)

    override val noteState: MutableLiveData<NoteState> = MutableLiveData(noteState)

    override val id: MutableLiveData<Long> = MutableLiveData(id)

    override val color: MutableLiveData<Color> = MutableLiveData(color)

    //region Cleanup

    //region Variables

    /**
     * Abstract because need setup callback but this class not final.
     */
    @RunProtected lateinit var saveControl: SaveControl
        private set

    fun setSaveControl(saveControl: SaveControl) {
        this.saveControl = saveControl
    }

    @RunProtected var inputControl: IInputControl = InputControl()

    @RunProtected lateinit var noteItem: N

    /**
     * Item for cash data before enter edit mode (for easy data restore).
     */
    @RunProtected lateinit var restoreItem: N

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
        setupBeforeInitialize()

        viewModelScope.launch {
            if (tryInitializeNote()) {
                setupAfterInitialize()
            }
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
        parentCallback = null
        saveControl.changeAutoSaveWork(isWork = false)
    }


    //    override fun onSaveData(bundle: Bundle) {
    //        bundle.apply {
    //            putLong(Intent.ID, deprecatedId)
    //            putInt(Intent.COLOR, colorConverter.toInt(deprecatedColor))
    //        }
    //    }

    override fun onResume() {
        if (isEdit.value.isTrue()) {
            saveControl.changeAutoSaveWork(isWork = true)
        }
    }

    override fun onPause() {
        if (parentCallback?.isOrientationChanging() == true) return

        if (isEdit.value.isTrue()) {
            saveControl.onPauseSave()
            saveControl.changeAutoSaveWork(isWork = false)
        }
    }


    override fun onClickBackArrow() {
        if (noteState.value != NoteState.CREATE && isEdit.value.isTrue()) {
            callback?.hideKeyboard()
            onRestoreData()
        } else {
            saveControl.isNeedSave = false
            callback?.finish()
        }
    }

    /**
     * FALSE - will call super.onBackPress()
     */
    override fun onPressBack(): Boolean {
        if (isEdit.value.isFalse()) return false

        /** If note can't be saved and activity will be closed. */
        saveControl.isNeedSave = false

        return if (!onMenuSave(changeMode = true)) {
            if (noteState.value != NoteState.CREATE) onRestoreData() else false
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
        val newColor = colorConverter.toEnum(check) ?: return

        inputControl.onColorChange(noteItem.color, newColor)

        color.postValue(newColor)
        noteItem.color = newColor

        callback?.apply {
            onBindingInput(noteItem, inputControl.access)
            tintToolbar(newColor)
        }
    }

    override fun onResultRankDialog(check: Int) {
        viewModelScope.launch {
            val rankId = runBack { getRankId(check) }

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
            callback?.showTimeDialog(calendar, getNotificationDateList())
        }
    }

    override fun onResultDateDialogClear() {
        viewModelScope.launch {
            runBack { deleteNotification(noteItem) }

            callback?.sendCancelAlarmBroadcast(noteItem)
            callback?.sendNotifyInfoBroadcast()
        }

        noteItem.clearAlarm()
        cacheData()

        callback?.onBindingNote(noteItem)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.isBeforeNow()) return

        viewModelScope.launch {
            runBack { setNotification(noteItem, calendar) }
            cacheData()

            callback?.onBindingNote(noteItem)

            callback?.sendSetAlarmBroadcast(noteItem.id, calendar)
            callback?.sendNotifyInfoBroadcast()
        }
    }

    override fun onResultConvertDialog() {
        viewModelScope.launch {
            runBack { convertNote(noteItem) }
            parentCallback?.convertNote()
        }
    }

    //endregion

    /**
     * Calls on note notification cancel from status bar for update bind indicator.
     */
    override fun onReceiveUnbindNote(noteId: Long) {
        if (id.value != noteId) return

        noteItem.isStatus = false
        restoreItem.isStatus = false

        callback?.onBindingNote(noteItem)
    }

    //region Menu click

    override fun onMenuRestore() {
        viewModelScope.launch {
            runBack { restoreNote(noteItem) }
            callback?.finish()
        }
    }

    override fun onMenuRestoreOpen() {
        noteState.postValue(NoteState.EXIST)

        noteItem.onRestore()

        mayAnimateIcon = false
        setupEditMode(isEdit = false)
        mayAnimateIcon = true

        viewModelScope.launchBack { updateNote(noteItem) }
    }

    override fun onMenuClear() {
        viewModelScope.launch {
            runBack { clearNote(noteItem) }
            callback?.finish()
        }
    }


    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    @RunPrivate fun onMenuUndoRedo(isUndo: Boolean) {
        if (callback?.isDialogOpen == true || isEdit.value.isFalse()) return

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
        val list = NumbersJoinConverter().toList(item[isUndo])

        if (list.size != 2) return

        // TODO assertSize and record exception in bad case
        noteItem.apply {
            rankId = list.firstOrNull() ?: return
            rankPs = list.lastOrNull()?.toInt() ?: return
        }
    }

    @RunProtected fun onMenuUndoRedoColor(item: InputItem, isUndo: Boolean) {
        val colorFrom = noteItem.color

        // TODO record exception
        val colorOrdinalTo = item[isUndo].toIntOrNull() ?: return
        val colorTo = colorConverter.toEnum(colorOrdinalTo) ?: return

        noteItem.color = colorTo

        callback?.tintToolbar(colorFrom, colorTo)
    }

    @RunProtected fun onMenuUndoRedoName(item: InputItem, isUndo: Boolean) {
        val text = item[isUndo]
        val position = item.cursor[isUndo]

        callback?.changeName(text, position)
    }


    override fun onMenuRank() {
        if (isEdit.value.isFalse()) return

        callback?.showRankDialog(check = noteItem.rankPs + 1)
    }

    override fun onMenuColor() {
        if (isEdit.value.isFalse()) return

        callback?.showColorDialog(noteItem.color)
    }

    /**
     * Function of background work for note saving.
     */
    abstract suspend fun saveBackgroundWork()


    override fun onMenuNotification() {
        if (isEdit.value.isTrue()) return

        callback?.showDateDialog(noteItem.alarmDate.toCalendar(), noteItem.haveAlarm())
    }

    override fun onMenuBind() {
        if (callback?.isDialogOpen == true || isEdit.value.isTrue()) return

        noteItem.switchStatus()
        cacheData()

        callback?.onBindingEdit(noteItem, isEdit.value.isTrue())

        viewModelScope.launch {
            runBack { updateNote(noteItem) }

            callback?.sendNotifyNotesBroadcast()
        }
    }

    override fun onMenuConvert() {
        if (isEdit.value.isTrue()) return

        callback?.showConvertDialog()
    }

    override fun onMenuDelete() {
        if (callback?.isDialogOpen == true || isEdit.value.isTrue()) return

        viewModelScope.launch {
            runBack { deleteNote(noteItem) }

            callback?.sendCancelAlarmBroadcast(noteItem)
            callback?.sendCancelNoteBroadcast(noteItem)
            callback?.sendNotifyInfoBroadcast()

            callback?.finish()
        }
    }

    override fun onMenuEdit() {
        if (callback?.isDialogOpen == true || isEdit.value.isTrue()) return

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

    //endregion

}