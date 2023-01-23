package sgtmelon.scriptum.infrastructure.screen.note.parent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.Calendar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import sgtmelon.extensions.flowOnBack
import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NoteRank
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.data.noteHistory.model.HistoryChange
import sgtmelon.scriptum.data.noteHistory.model.HistoryMoveAvailable
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.model.result.HistoryResult
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationsDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetHistoryResultUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.cacheNote.CacheNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.getNote.GetNoteUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.utils.extensions.note.clearAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onRestore
import sgtmelon.scriptum.infrastructure.utils.extensions.note.switchStatus

/**
 * TODO normal description
 */
abstract class ParentNoteViewModelImpl<N : NoteItem>(
    init: NoteInit,
    protected val history: NoteHistory,
    createNote: CreateNoteUseCase<N>,
    getNote: GetNoteUseCase<N>,
    protected val cacheNote: CacheNoteUseCase<N>,

    //TODO cleanup
    protected val colorConverter: ColorConverter,
    protected val preferencesRepo: PreferencesRepo,
    private val convertNote: ConvertNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val restoreNote: RestoreNoteUseCase,
    private val clearNote: ClearNoteUseCase,
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val getNotificationsDateList: GetNotificationsDateListUseCase,
    private val getRankId: GetRankIdUseCase,
    protected val getRankDialogNames: GetRankDialogNamesUseCase,
    private val getHistoryResult: GetHistoryResultUseCase
) : ViewModel(),
    ParentNoteViewModel<N> {

    override val isDataReady: MutableLiveData<Boolean> = MutableLiveData(false)

    override val isEdit: MutableLiveData<Boolean> = MutableLiveData(init.isEdit)

    override val noteState: MutableLiveData<NoteState> = MutableLiveData(init.state)

    override val id: MutableLiveData<Long> = MutableLiveData(init.id)

    override val color: MutableLiveData<Color> = MutableLiveData(init.color)

    /** App doesn't have any categories (ranks) if size == 1. */
    override val rankDialogItems: MutableLiveData<Array<String>> = MutableLiveData()

    override val noteItem: MutableLiveData<N> = MutableLiveData()

    init {
        viewModelScope.launchBack {
            rankDialogItems.postValue(getRankDialogNames())

            val id = init.id
            val value = if (id == Default.ID) createNote() else getNote(id)
            if (value != null) {
                noteItem.postValue(value)
                cacheNote(value)
            } else {
                // TODO report about null item
            }

            isDataReady.postValue(true)
        }
    }

    override val historyAvailable: MutableLiveData<HistoryMoveAvailable> = MutableLiveData()

    override val notificationsDateList: Flow<List<String>>
        get() = flowOnBack { emit(getNotificationsDateList()) }

    //region Cleanup

    /**
     * Call after [tryInitializeNote]
     */
    abstract suspend fun setupAfterInitialize()

    @Deprecated("Use new realization")
    protected lateinit var deprecatedNoteItem: N

    // Menu click

    override fun undoAction(): Flow<HistoryResult> = onUndoRedoAction(isUndo = true)

    override fun redoAction(): Flow<HistoryResult> = onUndoRedoAction(isUndo = false)

    private fun onUndoRedoAction(isUndo: Boolean): Flow<HistoryResult> = flowOnBack {
        if (isReadMode) return@flowOnBack

        val item = if (isUndo) history.undo() else history.redo()
        if (item != null) {
            // TODO disable after select and when collect emit
            val result = getHistoryResult(item, isUndo)
            selectHistoryResult(result)
            emit(result)
        }

        historyAvailable.postValue(history.available)
    }

    /**
     * Function must describe logic of switching by [HistoryResult] and call [onEmit] if it's
     * needed.
     */
    abstract fun selectHistoryResult(result: HistoryResult)

    protected fun onHistoryRank(result: HistoryResult.Rank) {
        noteItem.value?.let {
            it.rank = NoteRank(result.id, result.position)
            noteItem.postValue(it)
        }
    }

    protected fun onHistoryColor(action: HistoryResult.Color) {
        val colorTo = action.value

        color.postValue(colorTo)
        noteItem.value?.let {
            it.color = colorTo
            noteItem.postValue(it)
        }
    }

    override fun edit() {
        if (isEditMode) return

        setupEditMode(isEdit = true)
    }

    /**
     * Function must describe changing of edit/read modes.
     */
    protected fun setupEditMode(isEdit: Boolean) {
        this.isEdit.postValue(isEdit)
        // TODO may be post noteItem?
        historyAvailable.postValue(history.available) // TODO it's really needed?
    }

    override fun onHistoryAdd(action: HistoryAction) = history.add(action)

    /**
     * TODO check issue described below:
     * Need check isNoteInitialized for prevent crash. Strange what this function calls before
     * note initialisation, may be it related with view binding.
     */
    override fun onHistoryEnterChanged(text: String) {
        //        if (!isNoteInitialized()) return
        historyAvailable.postValue(history.available)
    }

    //endregion

    //region Menu clicks

    // TODO correct order of menu functions

    override fun restore(): Flow<NoteItem> = flowOnBack {
        val item = noteItem.value ?: return@flowOnBack
        restoreNote(item)
        emit(item)
    }

    override fun restoreOpen() {
        viewModelScope.launch {
            val item = noteItem.value ?: return@launch

            item.onRestore()
            launchBack { updateNote(item) }

            noteItem.postValue(item)
            noteState.postValue(NoteState.EXIST)
        }
    }

    override fun deleteForever(): Flow<NoteItem> = flowOnBack {
        val item = noteItem.value ?: return@flowOnBack
        clearNote(item)
        emit(item)
    }


    override fun changeColor(check: Int) {
        if (isReadMode) return

        val newColor = colorConverter.toEnum(check) ?: return
        val item = noteItem.value ?: return

        history.add(HistoryAction.Color(HistoryChange(item.color, newColor)))
        historyAvailable.postValue(history.available)

        color.postValue(newColor)
        item.color = newColor
    }

    override fun changeRank(check: Int) {
        if (isReadMode) return

        viewModelScope.launch {
            val item = noteItem.value ?: return@launch

            val rankId = runBack { getRankId(check) }
            val historyAction = HistoryAction.Rank(
                HistoryChange(item.rank.id, rankId),
                HistoryChange(item.rank.position, check)
            )

            history.add(historyAction)
            historyAvailable.postValue(history.available)

            item.rank = NoteRank(rankId, check)
            noteItem.postValue(item)
        }
    }


    override fun setNotification(calendar: Calendar): Flow<N> = flowOnBack {
        if (isEditMode) return@flowOnBack

        val item = noteItem.value

        if (item == null || calendar.isBeforeNow()) return@flowOnBack

        setNotification(item, calendar)
        cacheNote(item)

        noteItem.postValue(item)
        emit(item)
    }

    override fun removeNotification(): Flow<N> = flowOnBack {
        if (isEditMode) return@flowOnBack

        val item = noteItem.value ?: return@flowOnBack

        deleteNotification(item)
        item.clearAlarm()
        cacheNote(item)

        noteItem.postValue(item)
        emit(item)
    }

    override fun switchBind(): Flow<N> = flowOnBack {
        if (isEditMode) return@flowOnBack

        val item = noteItem.value ?: return@flowOnBack

        item.switchStatus()
        cacheNote(item)
        noteItem.postValue(item)

        updateNote(item)
        emit(item)
    }

    override fun convert(): Flow<N> = flowOnBack {
        if (isEditMode) return@flowOnBack

        val item = noteItem.value ?: return@flowOnBack
        convertNote(item)
        emit(item)
    }

    override fun delete(): Flow<N> = flowOnBack {
        if (isEditMode) return@flowOnBack

        val item = noteItem.value ?: return@flowOnBack
        deleteNote(item)
        emit(item)
    }

    //endregion

    // Other

    /**
     * Calls on note notification cancel from status bar for update bind indicator.
     */
    override fun onReceiveUnbindNote(noteId: Long) {
        if (id.value != noteId) return

        val item = noteItem.value ?: return
        item.isStatus = false
        cacheNote.item?.isStatus = false

        noteItem.postValue(item)
    }

    override fun disableHistoryChanges(func: () -> Unit) {
        history.saveChanges = false
        func()
        history.saveChanges = true
    }
}