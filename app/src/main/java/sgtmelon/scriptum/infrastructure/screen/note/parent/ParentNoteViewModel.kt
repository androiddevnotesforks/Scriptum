package sgtmelon.scriptum.infrastructure.screen.note.parent

import androidx.lifecycle.LiveData
import java.util.Calendar
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControlImpl
import sgtmelon.scriptum.data.noteHistory.HistoryMoveAvailable
import sgtmelon.scriptum.infrastructure.listener.HistoryTextWatcher
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver

/**
 * Parent interface for communicate with children of [ParentNoteViewModelImpl].
 */
@Deprecated("Remove ParentViewModel, change name")
interface ParentNoteViewModel<N : NoteItem> :
    UnbindNoteReceiver.Callback,
    SaveControlImpl.Callback,
    HistoryTextWatcher.Callback {

    val isDataReady: LiveData<Boolean>

    val isEdit: LiveData<Boolean>

    val noteState: LiveData<NoteState>

    val id: LiveData<Long>

    val color: LiveData<Color>

    val rankDialogItems: LiveData<Array<String>>

    val noteItem: LiveData<N>

    val historyAvailable: LiveData<HistoryMoveAvailable>

    val notificationsDateList: Flow<List<String>>

    //region Cleanup

    fun onResume()

    fun onPause()

    fun onDestroy()


    fun onClickBackArrow()

    fun onPressBack(): Boolean




    //endregion

    // TODO move this functions into ParentNoteViewModel

    // Inside bin

    fun restore(): Flow<NoteItem>

    fun restoreOpen()

    fun deleteForever(): Flow<NoteItem>


    // Edit mode

    fun undoAction()

    fun redoAction()

    // TODO may be pass already color?
    fun changeColor(check: Int): Flow<Color>

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

}