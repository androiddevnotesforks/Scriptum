package sgtmelon.scriptum.screen.ui.callback.main


import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Interface for communication [NotesViewModel] with [NotesFragment]
 */
interface INotesFragment : INotesBridge {

    fun setupToolbar()

    fun setupRecycler(@Theme theme: Int)

    fun setupBinding(isListHide: Boolean)

    fun bind()

    fun scrollTop()

    fun startNoteActivity(noteEntity: NoteEntity)

    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun notifyDataSetChanged(list: MutableList<NoteModel>)

    fun notifyItemChanged(p: Int, list: MutableList<NoteModel>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>)

}