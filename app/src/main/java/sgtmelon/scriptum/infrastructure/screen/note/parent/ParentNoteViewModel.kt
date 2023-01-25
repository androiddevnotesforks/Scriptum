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

/**
 * Parent interface for communicate with children of [ParentNoteViewModelImpl].
 */
@Deprecated("Remove ParentViewModel, change name")
interface ParentNoteViewModel<N : NoteItem> :
    UnbindNoteReceiver.Callback,
    NoteHistoryEnableControl,
    HistoryTextWatcher.Callback {

    val isDataReady: LiveData<Boolean>

    val isEdit: LiveData<Boolean>

    val isEditMode: Boolean get() = isEdit.value.isTrue()
    val isReadMode: Boolean get() = isEdit.value.isFalse()

    val noteState: LiveData<NoteState>

    val id: LiveData<Long>

    val color: LiveData<Color>

    val rankDialogItems: LiveData<Array<String>>

    val noteItem: LiveData<N>

    val historyAvailable: LiveData<HistoryMoveAvailable>

    val notificationsDateList: Flow<List<String>>

    /**
     * Return TRUE if [noteItem] data successfully restored.
     */
    fun restoreDataOrExit(): Boolean {
        if (isEditMode && noteState.value != NoteState.CREATE) {
            return restoreData()
        }

        return false
    }

    /**
     * Return FALSE if can't save or restore [noteItem] data.
     */
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

    //region Cleanup

    // Inside bin

    fun restore(): Flow<NoteItem>

    fun restoreOpen()

    fun deleteForever(): Flow<NoteItem>

    // Edit mode

    fun undoAction(): Flow<HistoryResult>

    fun redoAction(): Flow<HistoryResult>

    // TODO may be pass Color value as parameter (not Int)?
    fun changeColor(check: Int)

    fun changeRank(check: Int)

    /**
     * Return true on success save
     * [changeMode] - need change mode or not.
     */
    fun save(changeMode: Boolean): Boolean

    // Read mode

    fun setNotification(calendar: Calendar): Flow<N>

    fun removeNotification(): Flow<N>

    fun switchBind(): Flow<N>

    fun convert(): Flow<N>

    fun delete(): Flow<N>

    fun edit()

    //endregion

}