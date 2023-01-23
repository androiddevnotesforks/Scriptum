package sgtmelon.scriptum.infrastructure.screen.note.text

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.noteHistory.NoteHistory
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
import sgtmelon.scriptum.domain.useCase.note.SaveNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.cacheNote.CacheTextNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateTextNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.getNote.GetTextNoteUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.note.copy
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onSave

class TextNoteViewModelImpl(
    init: NoteInit,
    history: NoteHistory,
    createNote: CreateTextNoteUseCase,
    getNote: GetTextNoteUseCase,
    cacheNote: CacheTextNoteUseCase,

    // TODO cleanup
    private val callback: TextNoteFragment,
    colorConverter: ColorConverter,
    preferencesRepo: PreferencesRepo,
    private val saveNote: SaveNoteUseCase,
    convertNote: ConvertNoteUseCase,
    updateNote: UpdateNoteUseCase,
    deleteNote: DeleteNoteUseCase,
    restoreNote: RestoreNoteUseCase,
    clearNote: ClearNoteUseCase,
    setNotification: SetNotificationUseCase,
    deleteNotification: DeleteNotificationUseCase,
    getNotificationDateList: GetNotificationsDateListUseCase,
    getRankId: GetRankIdUseCase,
    getRankDialogNames: GetRankDialogNamesUseCase,
    getHistoryResult: GetHistoryResultUseCase
) : ParentNoteViewModelImpl<NoteItem.Text>(
    init, history, createNote, getNote, cacheNote,

    // TODO cleanup
    colorConverter, preferencesRepo, convertNote,
    updateNote, deleteNote, restoreNote, clearNote, setNotification, deleteNotification,
    getNotificationDateList, getRankId, getRankDialogNames, getHistoryResult
), TextNoteViewModel {

    // TODO remove
    override suspend fun setupAfterInitialize() {
//        mayAnimateIcon = false
        // TODO may this is not needed?
        setupEditMode(isEditMode)
        //        mayAnimateIcon = true
    }

    //region Cleanup

    override fun restoreData(): Boolean {
        if (id.value == Default.ID || deprecatedNoteItem.id == Default.ID) return false

        val restoreItem = cacheNote.item
        if (restoreItem != null) {
            deprecatedNoteItem = restoreItem.copy()
        }
        val colorTo = deprecatedNoteItem.color

        setupEditMode(isEdit = false)

        color.postValue(colorTo)
        history.reset()

        return true
    }

    override fun selectHistoryResult(result: HistoryResult) {
        when (result) {
            is HistoryResult.Rank -> onHistoryRank(result)
            is HistoryResult.Color -> onHistoryColor(result)
            else -> return
        }
    }

    /**
     * Don't need update [color] because it's happen in [changeColor] function.
     */
    override fun save(changeMode: Boolean): Boolean {
        if (isReadMode || !deprecatedNoteItem.isSaveEnabled) return false

        deprecatedNoteItem.onSave()

        if (changeMode) {
            setupEditMode(isEdit = false)
            history.reset()
        }

        viewModelScope.launch {
            val isCreate = noteState.value == NoteState.CREATE
            runBack { saveNote(deprecatedNoteItem, isCreate) }
            cacheNote(deprecatedNoteItem)

            if (isCreate) {
                noteState.postValue(NoteState.EXIST)
                id.postValue(deprecatedNoteItem.id)
            }

            callback.sendNotifyNotesBroadcast()
        }

        return true
    }

    //endregion

}