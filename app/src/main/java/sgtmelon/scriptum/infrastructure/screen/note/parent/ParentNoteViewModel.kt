package sgtmelon.scriptum.infrastructure.screen.note.parent

import androidx.lifecycle.LiveData
import java.util.Calendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControlImpl
import sgtmelon.scriptum.cleanup.presentation.screen.IParentViewModel
import sgtmelon.scriptum.data.noteHistory.HistoryAction
import sgtmelon.scriptum.data.noteHistory.HistoryMoveAvailable
import sgtmelon.scriptum.infrastructure.listener.HistoryTextWatcher
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.note.NoteMenu

/**
 * Parent interface for communicate with children of [ParentNoteViewModelImpl].
 */
@Deprecated("Remove ParentViewModel, change name")
interface ParentNoteViewModel<N : NoteItem> : IParentViewModel,
    UnbindNoteReceiver.Callback,
    NoteMenu,
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

    fun addToHistory(action: HistoryAction)

    //region Cleanup

    fun onResume()

    fun onPause()


    fun onClickBackArrow()

    fun onPressBack(): Boolean


    // TODO may be pass already color?
    fun onResultColorDialog(check: Int)

    fun onResultRankDialog(check: Int)

    fun onResultDateDialog(calendar: Calendar)

    fun onResultDateDialogClear()

    fun onResultTimeDialog(calendar: Calendar)

    fun onResultConvertDialog()

    //endregion

}