package sgtmelon.scriptum.app.ui.main.bin

import sgtmelon.scriptum.app.model.NoteRepo

/**
 * Интерфейс для общения [BinViewModel] и [BinFragment]
 */
interface BinCallback {

    fun bind()

    fun scrollTop()

    fun notifyMenuClearBin()

    fun notifyDataSetChanged(list: MutableList<NoteRepo>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteRepo>)

}