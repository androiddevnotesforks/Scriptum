package sgtmelon.scriptum.infrastructure.screen.note.parent

import androidx.lifecycle.LiveData
import java.util.Calendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.input.watcher.InputTextWatcher
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControlImpl
import sgtmelon.scriptum.cleanup.presentation.screen.IParentViewModel
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
    InputTextWatcher.Callback {

    val isEdit: LiveData<Boolean>

    val noteState: LiveData<NoteState>

    val id: LiveData<Long>

    val color: LiveData<Color>

    val rankDialogItems: LiveData<Array<String>>

    val noteItem: LiveData<N>

    //region Cleanup

    fun onResume()

    fun onPause()


    fun onClickBackArrow()

    fun onPressBack(): Boolean


    fun onResultColorDialog(check: Int)

    fun onResultRankDialog(check: Int)

    fun onResultDateDialog(calendar: Calendar)

    fun onResultDateDialogClear()

    fun onResultTimeDialog(calendar: Calendar)

    fun onResultConvertDialog()

    //endregion

}