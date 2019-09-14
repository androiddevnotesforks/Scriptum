package sgtmelon.scriptum.screen.ui.callback.main

import android.content.Intent
import sgtmelon.scriptum.control.alarm.callback.AlarmCallback
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Interface for communication [NotesViewModel] with [NotesFragment]
 */
interface INotesFragment : AlarmCallback.Cancel {

    fun setupToolbar()

    fun setupRecycler(@Theme theme: Int)

    fun setupBinding(isListHide: Boolean)

    fun bind()

    fun scrollTop()

    fun startActivity(intent: Intent)

    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun notifyDataSetChanged(list: MutableList<NoteModel>)

    fun notifyItemChanged(p: Int, list: MutableList<NoteModel>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>)

}