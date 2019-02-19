package sgtmelon.scriptum.app.ui.main.notes

import sgtmelon.scriptum.app.model.NoteRepo

/**
 * Интерфейс для общения [NotesViewModel] и [NotesFragment]
 */
interface NotesCallback {

    fun bind()

    fun scrollTop()

    fun notifyDataSetChanged(list: MutableList<NoteRepo>)

    fun notifyItemChanged(list: MutableList<NoteRepo>, p: Int)

    fun notifyItemRemoved(list: MutableList<NoteRepo>, p: Int)

}