package sgtmelon.scriptum.infrastructure.screen.note.parent

import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import sgtmelon.extensions.flowBack
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
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.note.clearAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onRestore
import sgtmelon.scriptum.infrastructure.utils.extensions.note.switchStatus
import java.util.Calendar

/**
 * Parent ViewModel for notes, describes main logic and features.
 */
abstract class ParentNoteViewModelImpl<N : NoteItem>(
    private val colorConverter: ColorConverter,
    init: NoteInit,
    protected val history: NoteHistory,
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

    /** This cast is save because we chose (UI class + viewModel) rely on [NoteType]. */
    @Suppress("UNCHECKED_CAST")
    override val noteItem: MutableLiveData<N> = MutableLiveData(init.noteItem as N)

    override val noteState: MutableLiveData<NoteState> = MutableLiveData(init.state)

    override val isEdit: MutableLiveData<Boolean> = MutableLiveData(init.isEdit)

    override val color: MutableLiveData<Color> = MutableLiveData(init.noteItem.color)

    /** If app doesn't have any categories (ranks) when array size == 1. */
    override val rankDialogItems: MutableLiveData<Array<String>> = MutableLiveData()

    override val historyAvailable: MutableLiveData<HistoryMoveAvailable> = MutableLiveData(
        HistoryMoveAvailable(undo = false, redo = false)
    )

    override val notificationsDateList: Flow<List<String>>
        get() = flowBack { emit(getNotificationsDateList()) }

    override fun fetchData() {
        viewModelScope.launch {
            runBack { rankDialogItems.postValue(getRankDialogNames.invoke()) }

            val item = noteItem.value
            if (item != null) {
                cacheNote(item)
                afterDataInit(item)
            }
        }
    }

    /** Describes initialization which must be done after [noteItem] setting up. */
    @CallSuper open fun afterDataInit(item: N) = Unit

    //region Menu clicks

    override fun restore(): Flow<NoteItem> = flowBack {
        val item = noteItem.value ?: return@flowBack
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

    override fun deleteForever(): Flow<NoteItem> = flowBack {
        val item = noteItem.value ?: return@flowBack
        clearNote(item)
        emit(item)
    }


    override fun undoAction(): Flow<HistoryResult> = onUndoRedoAction(isUndo = true)

    override fun redoAction(): Flow<HistoryResult> = onUndoRedoAction(isUndo = false)

    private fun onUndoRedoAction(isUndo: Boolean): Flow<HistoryResult> = flowBack {
        if (isReadMode) return@flowBack

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


    override fun setNotification(calendar: Calendar): Flow<N> = flowBack {
        if (isEditMode || calendar.isBeforeNow) return@flowBack

        val item = noteItem.value ?: return@flowBack

        setNotification(item, calendar)
        cacheNote(item)

        noteItem.postValue(item)
        emit(item)
    }

    override fun removeNotification(): Flow<N> = flowBack {
        if (isEditMode) return@flowBack

        val item = noteItem.value ?: return@flowBack

        deleteNotification(item)
        item.clearAlarm()
        cacheNote(item)

        noteItem.postValue(item)
        emit(item)
    }

    override fun switchBind(): Flow<N> = flowBack {
        if (isEditMode) return@flowBack

        val item = noteItem.value ?: return@flowBack

        item.switchStatus()
        updateNote(item)
        cacheNote(item)

        noteItem.postValue(item)
        emit(item)
    }

    override fun convert(): Flow<NoteItem> = flowBack {
        if (isEditMode) return@flowBack

        val item = noteItem.value ?: return@flowBack
        val newNote = convertNote(item)
        emit(newNote)
    }

    override fun delete(): Flow<N> = flowBack {
        if (isEditMode) return@flowBack

        val item = noteItem.value ?: return@flowBack
        deleteNote(item)
        emit(item)
    }

    override fun edit() = isEdit.postValue(true)

    //endregion

    //region Other

    /** Calls on note notification cancel from status bar for update bind indicator. */
    override fun onReceiveUnbindNote(noteId: Long) {
        if (noteItem.value?.id != noteId) return

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