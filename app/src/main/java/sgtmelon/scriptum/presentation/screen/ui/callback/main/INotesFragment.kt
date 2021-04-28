package sgtmelon.scriptum.presentation.screen.ui.callback.main


import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.ClipboardControl
import sgtmelon.scriptum.presentation.receiver.eternal.BindEternalReceiver
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.main.INotesViewModel
import java.util.*

/**
 * Interface for communication [INotesViewModel] with [NotesFragment].
 */
interface INotesFragment : BindEternalReceiver.Broadcast,
    AlarmControl.Bridge.Full,
    ClipboardControl.Bridge {

    fun setupToolbar()

    fun setupRecycler()

    fun setupDialog()

    fun setupBinding(isListHide: Boolean)


    /**
     * Calls before load data inside list.
     */
    fun beforeLoad()

    fun showProgress()


    fun onBindingList()

    fun scrollTop()

    fun openNoteScreen(item: NoteItem)


    fun showOptionsDialog(title: String, itemArray: Array<String>, p: Int)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean, p: Int)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>, p: Int)


    fun notifyList(list: List<NoteItem>)

    fun notifyItemChanged(list: List<NoteItem>, p: Int)

    fun notifyItemRemoved(list: List<NoteItem>, p: Int)


    fun getStringArray(@ArrayRes arrayId: Int): Array<String>

    fun getString(@StringRes stringId: Int): String

}