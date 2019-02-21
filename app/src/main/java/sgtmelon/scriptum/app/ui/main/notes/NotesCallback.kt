package sgtmelon.scriptum.app.ui.main.notes

import android.content.Intent
import sgtmelon.scriptum.app.model.NoteRepo

/**
 * Интерфейс для общения [NotesViewModel] и [NotesFragment]
 */
interface NotesCallback {

    fun bind()

    fun scrollTop()

    fun startNote(intent: Intent)

    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun notifyDataSetChanged(list: MutableList<NoteRepo>)

    fun notifyItemChanged(p: Int, list: MutableList<NoteRepo>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteRepo>)

}