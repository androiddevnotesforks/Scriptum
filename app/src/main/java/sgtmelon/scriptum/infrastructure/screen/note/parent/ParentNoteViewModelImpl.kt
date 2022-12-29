package sgtmelon.scriptum.infrastructure.screen.note.parent

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import java.util.Calendar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.runBack
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.cleanup.presentation.screen.ParentViewModel
import sgtmelon.scriptum.data.noteHistory.HistoryAction
import sgtmelon.scriptum.data.noteHistory.HistoryChange
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.getNote.GetNoteUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.note.NoteConnector
import sgtmelon.scriptum.infrastructure.utils.extensions.isFalse
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue

/**
 * TODO normal description
 */
abstract class ParentNoteViewModelImpl<N : NoteItem, C : ParentNoteFragment<N>>(
    init: NoteInit,
    protected val history: NoteHistory,
    createNote: CreateNoteUseCase<N>,
    getNote: GetNoteUseCase<N>,

    //TODO cleanup
    callback: C,
    private var parentCallback: NoteConnector?,
    protected val colorConverter: ColorConverter,
    protected val preferencesRepo: PreferencesRepo,
    private val convertNote: ConvertNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val restoreNote: RestoreNoteUseCase,
    private val clearNote: ClearNoteUseCase,
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val getNotificationDateList: GetNotificationDateListUseCase,
    private val getRankId: GetRankIdUseCase,
    protected val getRankDialogNames: GetRankDialogNamesUseCase
) : ParentViewModel<C>(callback),
    ParentNoteViewModel<N> {

    override val isDataReady: MutableLiveData<Boolean> = MutableLiveData(false)

    override val isEdit: MutableLiveData<Boolean> = MutableLiveData(init.isEdit)

    override val noteState: MutableLiveData<NoteState> = MutableLiveData(init.state)

    override val id: MutableLiveData<Long> = MutableLiveData(init.id)

    override val color: MutableLiveData<Color> = MutableLiveData(init.color)

    /** App doesn't have any categories (ranks) if size == 1. */
    override val rankDialogItems: MutableLiveData<Array<String>> = MutableLiveData()

    override val noteItem: MutableLiveData<N> = MutableLiveData()
    private var restoreItem: NoteItem? = null

    // TODO add observers for note and remove initialization functions
    init {
        viewModelScope.launchBack {
            rankDialogItems.postValue(getRankDialogNames())

            val id = init.id
            val value = if (id == Default.ID) createNote() else getNote(id)
            if (value != null) {
                noteItem.postValue(value)
                cacheData()
            } else {
                // TODO report about null item
            }

            isDataReady.postValue(true)
        }
    }

    // TODO vvv remove SETUP and staff below vvv

    protected fun isNoteInitialized(): Boolean = ::deprecatedNoteItem.isInitialized

    /**
     * Function must describe cashing data inside [deprecatedRestoreItem].
     *
     * It is important because if not cache data in [deprecatedRestoreItem] it will cause bug with restore.
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
    @Deprecated("init noteItem during init")
    abstract suspend fun tryInitializeNote(): Boolean

    /**
     * Call after [tryInitializeNote]
     */
    abstract suspend fun setupAfterInitialize()


    //region Cleanup

    //region Variables

    /**
     * Abstract because need setup callback but this class not final.
     */
    protected lateinit var saveControl: SaveControl
        private set

    fun setSaveControl(saveControl: SaveControl) {
        this.saveControl = saveControl
    }

    @Deprecated("Use new realization")
    protected lateinit var deprecatedNoteItem: N

    /**
     * Item for cash data before enter edit mode (for easy data restore).
     */
    @Deprecated("Use new realization")
    protected lateinit var deprecatedRestoreItem: N

    protected var mayAnimateIcon = true

    /**
     * App doesn't have ranks if size == 1.
     */
    @Deprecated("use rankDialogItems instead")
    protected var rankDialogItemArray: Array<String> = emptyArray()

    //endregion

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        parentCallback = null
        saveControl.changeAutoSaveWork(isWork = false)
    }

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

        history.add(HistoryAction.Color(HistoryChange(deprecatedNoteItem.color, newColor)))

        color.postValue(newColor)
        deprecatedNoteItem.color = newColor

        callback?.apply {
            onBindingInput(deprecatedNoteItem, history.available)
            tintToolbar(newColor)
        }
    }

    override fun onResultRankDialog(check: Int) {
        viewModelScope.launch {
            val rankId = runBack { getRankId(check) }

            /** Need save data in history, before update it for [deprecatedNoteItem]. */
            val historyAction = HistoryAction.Rank(
                HistoryChange(deprecatedNoteItem.rankId, rankId),
                HistoryChange(deprecatedNoteItem.rankPs, check)
            )
            history.add(historyAction)

            deprecatedNoteItem.apply {
                this.rankId = rankId
                this.rankPs = check
            }

            callback?.apply {
                onBindingInput(deprecatedNoteItem, history.available)
                onBindingNote(deprecatedNoteItem)
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
            runBack { deleteNotification(deprecatedNoteItem) }

            callback?.sendCancelAlarmBroadcast(deprecatedNoteItem)
            callback?.sendNotifyInfoBroadcast()
        }

        deprecatedNoteItem.clearAlarm()
        cacheData()

        callback?.onBindingNote(deprecatedNoteItem)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.isBeforeNow()) return

        viewModelScope.launch {
            runBack { setNotification(deprecatedNoteItem, calendar) }
            cacheData()

            callback?.onBindingNote(deprecatedNoteItem)

            callback?.sendSetAlarmBroadcast(deprecatedNoteItem.id, calendar)
            callback?.sendNotifyInfoBroadcast()
        }
    }

    override fun onResultConvertDialog() {
        viewModelScope.launch {
            runBack { convertNote(deprecatedNoteItem) }
            parentCallback?.convertNote()
        }
    }

    //endregion

    /**
     * Calls on note notification cancel from status bar for update bind indicator.
     */
    override fun onReceiveUnbindNote(noteId: Long) {
        if (id.value != noteId) return

        deprecatedNoteItem.isStatus = false
        deprecatedRestoreItem.isStatus = false

        callback?.onBindingNote(deprecatedNoteItem)
    }

    //region Menu click

    override fun onMenuRestore() {
        viewModelScope.launch {
            runBack { restoreNote(deprecatedNoteItem) }
            callback?.finish()
        }
    }

    override fun onMenuRestoreOpen() {
        noteState.postValue(NoteState.EXIST)

        deprecatedNoteItem.onRestore()

        mayAnimateIcon = false
        setupEditMode(isEdit = false)
        mayAnimateIcon = true

        viewModelScope.launchBack { updateNote(deprecatedNoteItem) }
    }

    override fun onMenuClear() {
        viewModelScope.launch {
            runBack { clearNote(deprecatedNoteItem) }
            callback?.finish()
        }
    }


    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    private fun onMenuUndoRedo(isUndo: Boolean) {
        if (callback?.isDialogOpen == true || isEdit.value.isFalse()) return

        val item = if (isUndo) history.undo() else history.redo()
        if (item != null) {
            onMenuUndoRedoSelect(item, isUndo)
        }

        callback?.onBindingInput(deprecatedNoteItem, history.available)
    }

    /**
     * TODO refactor description.
     * Function must describe logic of [isUndo] switch by [HistoryAction] class.
     */
    abstract fun onMenuUndoRedoSelect(action: HistoryAction, isUndo: Boolean)

    protected fun onMenuUndoRedoRank(action: HistoryAction.Rank, isUndo: Boolean) {
        deprecatedNoteItem.apply {
            rankId = action.id[isUndo]
            rankPs = action.position[isUndo]
        }
    }

    protected fun onMenuUndoRedoColor(action: HistoryAction.Color, isUndo: Boolean) {
        val colorFrom = deprecatedNoteItem.color
        val colorTo = action.value[isUndo]

        color.postValue(colorTo)
        deprecatedNoteItem.color = colorTo

        callback?.tintToolbar(colorFrom, colorTo)
    }

    protected fun onMenuUndoRedoName(action: HistoryAction.Name, isUndo: Boolean) {
        callback?.changeName(action.value[isUndo], action.cursor[isUndo])
    }


    override fun onMenuRank() {
        if (isEdit.value.isFalse()) return

        callback?.showRankDialog(check = deprecatedNoteItem.rankPs + 1)
    }

    override fun onMenuColor() {
        if (isEdit.value.isFalse()) return

        callback?.showColorDialog(deprecatedNoteItem.color)
    }

    /**
     * Function of background work for note saving.
     */
    abstract suspend fun saveBackgroundWork()


    override fun onMenuNotification() {
        if (isEdit.value.isTrue()) return

        callback?.showDateDialog(
            deprecatedNoteItem.alarmDate.toCalendar(),
            deprecatedNoteItem.haveAlarm()
        )
    }

    override fun onMenuBind() {
        if (callback?.isDialogOpen == true || isEdit.value.isTrue()) return

        deprecatedNoteItem.switchStatus()
        cacheData()

        callback?.onBindingEdit(deprecatedNoteItem, isEdit.value.isTrue())

        viewModelScope.launch {
            runBack { updateNote(deprecatedNoteItem) }

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
            runBack { deleteNote(deprecatedNoteItem) }

            callback?.sendCancelAlarmBroadcast(deprecatedNoteItem)
            callback?.sendCancelNoteBroadcast(deprecatedNoteItem)
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
    override fun onHistoryEnterChanged(text: String) {
        if (!isNoteInitialized()) return

        callback?.onBindingInput(deprecatedNoteItem, history.available)
    }

    //endregion

}