package sgtmelon.scriptum.screen.ui.callback.main


import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.room.entity.NoteEntity
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

    fun bind()

    fun scrollTop()

    fun startNoteActivity(noteEntity: NoteEntity)


    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean, p: Int)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>, p: Int)


    fun notifyDataSetChanged(list: MutableList<NoteModel>)

    fun notifyItemChanged(p: Int, list: MutableList<NoteModel>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>)

}