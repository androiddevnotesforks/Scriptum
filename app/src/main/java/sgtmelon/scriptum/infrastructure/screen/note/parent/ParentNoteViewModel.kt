package sgtmelon.scriptum.infrastructure.screen.note.parent

import androidx.lifecycle.LiveData
import java.util.Calendar
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.noteHistory.NoteHistoryEnableControl
import sgtmelon.scriptum.data.noteHistory.model.HistoryMoveAvailable
import sgtmelon.scriptum.domain.model.result.HistoryResult
import sgtmelon.scriptum.infrastructure.listener.HistoryTextWatcher
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.utils.extensions.isFalse
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue

interface ParentNoteViewModel<N : NoteItem> :
    UnbindNoteReceiver.Callback,
    NoteHistoryEnableControl,
    HistoryTextWatcher.Callback {

    val isDataReady: LiveData<Boolean>

    val noteState: LiveData<NoteState>

    val isEdit: LiveData<Boolean>

    val isEditMode: Boolean get() = isEdit.value.isTrue()
    val isReadMode: Boolean get() = isEdit.value.isFalse()

    val id: LiveData<Long>

    val color: LiveData<Color>

    val rankDialogItems: LiveData<Array<String>>

    val noteItem: LiveData<N>

    val historyAvailable: LiveData<HistoryMoveAvailable>

    val notificationsDateList: Flow<List<String>>

    /** Return TRUE if [noteItem] data successfully restored. */
    fun restoreDataOrExit(): Boolean {
        if (isEditMode && noteState.value != NoteState.CREATE) {
            return restoreData()
        }

        return false
    }

    /** Return FALSE if can't save or restore [noteItem] data. */
    fun saveOrRestoreData(): Boolean {
        if (isReadMode) return false

        return when {
            save(changeMode = true) -> true
            noteState.value != NoteState.CREATE -> restoreData()
            else -> false
        }
    }

    /**
     * Function must describe restoring all data (in code and for screen) after changes in
     * [isEditMode] was canceled.
     */
    fun restoreData(): Boolean

    fun changeName(value: String) = run { noteItem.value?.name = value }

    //region Menu clicks

    fun restore(): Flow<NoteItem>

    fun restoreOpen()

    fun deleteForever(): Flow<NoteItem>


    fun undoAction(): Flow<HistoryResult>

    fun redoAction(): Flow<HistoryResult>

    fun changeColor(check: Int)

    fun changeRank(check: Int)

    /**
     * Return TRUE on success save.
     * [changeMode] - need change mode or not.
     */
    fun save(changeMode: Boolean): Boolean


    fun setNotification(calendar: Calendar): Flow<N>

    fun removeNotification(): Flow<N>

    fun switchBind()

    fun convert(): Flow<N>

    fun delete(): Flow<N>

    fun edit()

    //endregion

}