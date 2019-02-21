package sgtmelon.scriptum.app.ui.main.bin

import android.content.Intent
import sgtmelon.scriptum.app.model.NoteRepo

/**
 * Интерфейс для общения [BinViewModel] и [BinFragment]
 */
interface BinCallback {

    fun bind()

    fun scrollTop()

    fun startNote(intent: Intent)

    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun notifyMenuClearBin()

    fun notifyDataSetChanged(list: MutableList<NoteRepo>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteRepo>)

}