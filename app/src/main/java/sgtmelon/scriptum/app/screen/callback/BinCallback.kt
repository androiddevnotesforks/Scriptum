package sgtmelon.scriptum.app.screen.callback

import android.content.Intent
import sgtmelon.scriptum.app.model.NoteModel

/**
 * Интерфейс для общения [BinViewModel] и [BinFragment]
 */
interface BinCallback {

    fun bind()

    fun scrollTop()

    fun startNote(intent: Intent)

    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun notifyMenuClearBin()

    fun notifyDataSetChanged(list: MutableList<NoteModel>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>)

}