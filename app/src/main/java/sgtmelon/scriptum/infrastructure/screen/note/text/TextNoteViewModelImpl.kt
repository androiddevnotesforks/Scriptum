package sgtmelon.scriptum.infrastructure.screen.note.text

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.runIO
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.noteHistory.NoteHistory
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
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.database.DbData.Note.Default
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.note.deepCopy
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onSave

class TextNoteViewModelImpl(
    colorConverter: ColorConverter,
    init: NoteInit,
    history: NoteHistory,
    cacheNote: CacheTextNoteUseCase,
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
    colorConverter, init, history, cacheNote,
    convertNote, updateNote, deleteNote, restoreNote, clearNote,
    setNotification, deleteNotification, getNotificationDateList,
    getRankId, getRankDialogNames, getHistoryResult
), TextNoteViewModel {

    override fun restoreData(): Boolean {
        val item = noteItem.value ?: return false
        val restoreItem = cacheNote.item?.deepCopy() ?: return false

        if (item.id == Default.ID) return false

        isEdit.postValue(false)
        noteItem.postValue(restoreItem)
        color.postValue(restoreItem.color)

        history.reset()

        return true
    }

    override fun selectHistoryResult(result: HistoryResult) {
        when (result) {
            is HistoryResult.Name -> return /** [noteItem] will be updated through UI. */
            is HistoryResult.Rank -> onHistoryRank(result)
            is HistoryResult.Color -> onHistoryColor(result)
            is HistoryResult.Text -> return /** [noteItem] will be updated through UI. */
            else -> return /** For another note types. */
        }
    }

    /** Don't need update [color] because it's happen in [changeColor] function. */
    override fun save(changeMode: Boolean): Boolean {
        val item = noteItem.value ?: return false

        if (isReadMode || !item.isSaveEnabled) return false

        item.onSave()
        noteItem.postValue(item)

        if (changeMode) {
            isEdit.postValue(false)
            history.reset()
            historyAvailable.postValue(history.available)
        }

        viewModelScope.launch {
            val isCreate = noteState.value == NoteState.CREATE
            /** [saveNote] updates [NoteItem.id], if it was in [NoteState.CREATE] */
            runIO { saveNote(item, isCreate) }
            cacheNote(item)

            if (isCreate) {
                noteState.postValue(NoteState.EXIST)
            }
        }

        return true
    }
}