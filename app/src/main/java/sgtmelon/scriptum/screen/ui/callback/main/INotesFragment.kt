package sgtmelon.scriptum.screen.ui.callback.main


import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.vm.main.NotesViewModel
import java.util.*

/**
 * Interface for communication [NotesViewModel] with [NotesFragment]
 */
interface INotesFragment : INotesBridge {

    fun setupToolbar()

    fun setupRecycler(@Theme theme: Int)

    fun setupDialog()

    fun setupBinding(isListHide: Boolean)


    /**
     * Calls before load data inside list.
     */
    fun beforeLoad()

    fun showProgress()


    fun onBindingList()

    fun scrollTop()

    fun startNoteActivity(item: NoteItem)


    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean, p: Int)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>, p: Int)


    fun notifyList(list: List<NoteItem>)

    fun notifyItemChanged(list: List<NoteItem>, p: Int)

    fun notifyItemRemoved(list: List<NoteItem>, p: Int)

}