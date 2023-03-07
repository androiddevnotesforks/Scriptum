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
import sgtmelon.extensions.postValueWithChange
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NoteRank
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.data.noteHistory.model.HistoryChange
import sgtmelon.scriptum.data.noteHistory.model.HistoryMoveAvailable
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
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateTypeNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.getNote.GetNoteUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.utils.extensions.note.clearAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onRestore
import sgtmelon.scriptum.infrastructure.utils.extensions.note.switchStatus

/**
 * Parent ViewModel for notes, describes main logic and features.
 */
abstract class ParentNoteViewModelImpl<N : NoteItem>(
    private val colorConverter: ColorConverter,
    init: NoteInit,
    protected val history: NoteHistory,
    createNote: CreateTypeNoteUseCase<N>, // TODO remove (refactor use case)?
    getNote: GetNoteUseCase<N>, // TODO remove (also check useCase)?
    protected val cacheNote: CacheNoteUseCase<N>,
    private val convertNote: ConvertNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val restoreNote: RestoreNoteUseCase,
    private val clearNote: ClearNoteUseCase,
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val getNotificationsDateList: GetNotificationsDateListUseCase,
    private val getRankId: GetRankIdUseCase,
    private val getRankDialogNames: GetRankDialogNamesUseCase,
    private val getHistoryResult: GetHistoryResultUseCase
) : ViewModel(),
    ParentNoteViewModel<N> {

    @Deprecated("Remove it, everything is ready")
    override val isDataReady: MutableLiveData<Boolean> = MutableLiveData(false)

    override val noteState: MutableLiveData<NoteState> = MutableLiveData(init.state)

    override val isEdit: MutableLiveData<Boolean> = MutableLiveData(init.isEdit)

    override val color: MutableLiveData<Color> = MutableLiveData(init.noteItem.color)

    /** App doesn't have any categories (ranks) if size == 1. */
    override val rankDialogItems: MutableLiveData<Array<String>> = MutableLiveData()

    override val noteItem: MutableLiveData<N> = MutableLiveData()

    override val historyAvailable: MutableLiveData<HistoryMoveAvailable> = MutableLiveData()

    override val notificationsDateList: Flow<List<String>>
        get() = flowOnBack { emit(getNotificationsDateList()) }

    init {
        viewModelScope.launch {
            runBack { rankDialogItems.postValue(getRankDialogNames()) }

            val item = init.noteItem as N // TODO think about it
            noteItem.postValue(item)
            cacheNote(item)
            isDataReady.postValue(true)
            initAfterDataReady(item)
        }
    }

    /** Describes initialization which must be done after [noteItem] loading. */
    abstract suspend fun initAfterDataReady(item: N)

    //region Menu clicks

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


    override fun undoAction(): Flow<HistoryResult> = onUndoRedoAction(isUndo = true)

    override fun redoAction(): Flow<HistoryResult> = onUndoRedoAction(isUndo = false)

    private fun onUndoRedoAction(isUndo: Boolean): Flow<HistoryResult> = flowOnBack {
        if (isReadMode) return@flowOnBack

        val item = if (isUndo) history.undo() else history.redo()
        if (item != null) {
            val result = getHistoryResult(item, isUndo)
            /** Emit needed for change something directly in UI. Request focus, set cursor, ect. */
            emit(result)
            selectHistoryResult(result)
        }

        historyAvailable.postValue(history.available)
    }

    /** Function must describe logic of switching by [HistoryResult]. */
    abstract fun selectHistoryResult(result: HistoryResult)

    protected fun onHistoryRank(result: HistoryResult.Rank) {
        noteItem.postValueWithChange { it.rank = NoteRank(result.id, result.position) }
    }

    protected fun onHistoryColor(result: HistoryResult.Color) {
        color.postValue(result.value)
        noteItem.postValueWithChange { it.color = result.value }
    }

    override fun changeColor(check: Int) {
        if (isReadMode) return

        val newColor = colorConverter.toEnum(check) ?: return
        val item = noteItem.value ?: return

        history.add(HistoryAction.Color(HistoryChange(item.color, newColor)))
        historyAvailable.postValue(history.available)

        color.postValue(newColor)
        noteItem.postValueWithChange { it.color = newColor }
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

            noteItem.postValueWithChange { it.rank = NoteRank(rankId, check) }
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

    override fun switchBind() {
        if (isEditMode) return

        viewModelScope.launch {
            val item = noteItem.value ?: return@launch
            item.switchStatus()
            runBack { updateNote(item) }

            cacheNote(item)
            noteItem.postValue(item)
        }
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

    override fun edit() = isEdit.postValue(true)

    //endregion

    //region Other

    /** Calls on note notification cancel from status bar for update bind indicator. */
    override fun onReceiveUnbindNote(noteId: Long) {
        if (id.value != noteId) return

        noteItem.postValueWithChange { it.isStatus = false }
        cacheNote.item?.isStatus = false
    }

    override fun disableHistoryChanges(func: () -> Unit) {
        history.saveChanges = false
        func()
        history.saveChanges = true
    }

    override fun onHistoryAdd(action: HistoryAction) = history.add(action)

    override fun onHistoryEnterChanged(text: String) = historyAvailable.postValue(history.available)

    //endregion

}