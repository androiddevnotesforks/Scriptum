package sgtmelon.scriptum.infrastructure.screen.main.notes


import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import java.util.Calendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver

/**
 * Interface for communication [INotesViewModel] with [NotesFragment].
 */
interface INotesFragment : SystemReceiver.Bridge.Alarm,
    SystemReceiver.Bridge.Bind {

    fun setupToolbar()

    fun setupRecycler()

    fun setupDialog()

    fun setupBinding(isListHide: Boolean)


    fun prepareForLoad()

    fun showProgress()

    fun hideEmptyInfo()


    fun onBindingList()

    fun scrollTop()

    @Deprecated("Create delegator for screens open")
    fun openNoteScreen(item: NoteItem)


    fun showOptionsDialog(title: String, itemArray: Array<String>, p: Int)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean, p: Int)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>, p: Int)


    fun notifyList(list: List<NoteItem>)


    fun getStringArray(@ArrayRes arrayId: Int): Array<String>

    fun getString(@StringRes stringId: Int): String

    fun copyClipboard(text: String)

}