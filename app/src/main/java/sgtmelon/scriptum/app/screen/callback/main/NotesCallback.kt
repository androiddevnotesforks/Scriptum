package sgtmelon.scriptum.app.screen.callback.main

import android.content.Intent
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.screen.view.main.NotesFragment
import sgtmelon.scriptum.app.screen.vm.main.NotesViewModel

/**
 * Интерфейс для общения [NotesViewModel] и [NotesFragment]
 *
 * @author SerjantArbuz
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