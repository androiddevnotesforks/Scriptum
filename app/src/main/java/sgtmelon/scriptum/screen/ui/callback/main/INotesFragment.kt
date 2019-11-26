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

    fun onBingingList()

    fun scrollTop()

    fun startNoteActivity(noteItem: NoteItem)


    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean, p: Int)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>, p: Int)


    fun notifyDataSetChanged(list: MutableList<NoteItem>)

    fun notifyItemChanged(p: Int, list: MutableList<NoteItem>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteItem>)

}