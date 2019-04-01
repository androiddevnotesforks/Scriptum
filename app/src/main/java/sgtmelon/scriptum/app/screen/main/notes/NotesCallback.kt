package sgtmelon.scriptum.app.screen.main.notes

import android.content.Intent
import sgtmelon.scriptum.app.model.NoteModel

/**
 * Интерфейс для общения [NotesViewModel] и [NotesFragment]
 *
 * @author SerjantArbuz
 * @version 1.0
 */
interface NotesCallback {

    fun bind()

    fun scrollTop()

    fun startNote(intent: Intent)

    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun notifyDataSetChanged(list: MutableList<NoteModel>)

    fun notifyItemChanged(p: Int, list: MutableList<NoteModel>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>)

}