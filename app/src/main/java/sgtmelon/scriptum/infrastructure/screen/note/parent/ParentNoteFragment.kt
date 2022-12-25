package sgtmelon.scriptum.infrastructure.screen.note.parent

import java.util.Calendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.noteHistory.HistoryMoveAvailable
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.receiver.service.EternalServiceReceiver

/**
 * Parent interface for [TextNoteFragment] and [RollNoteFragment].
 */
interface ParentNoteFragment<N : NoteItem> : EternalServiceReceiver.Bridge.Alarm,
    EternalServiceReceiver.Bridge.Bind {

    val isDialogOpen: Boolean

    fun hideKeyboard()


    fun onBindingEdit(item: N, isEditMode: Boolean)

    fun onBindingNote(item: N)

    fun onBindingInput(item: N, historyMove: HistoryMoveAvailable)


    fun onPressBack(): Boolean

    fun tintToolbar(from: Color, to: Color)

    fun tintToolbar(color: Color)

    fun setToolbarBackIcon(isCancel: Boolean, needAnim: Boolean)


    fun changeName(text: String, cursor: Int)


    fun showRankDialog(check: Int)

    fun showColorDialog(color: Color)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>)

    fun showConvertDialog()


    fun showSaveToast(isSuccess: Boolean)

    fun finish()

}