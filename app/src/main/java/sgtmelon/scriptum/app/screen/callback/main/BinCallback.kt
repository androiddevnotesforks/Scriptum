package sgtmelon.scriptum.app.screen.callback.main

import android.content.Intent
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.screen.view.main.BinFragment
import sgtmelon.scriptum.app.screen.vm.main.BinViewModel

/**
 * Интерфейс для общения [BinViewModel] и [BinFragment]
 *
 * @author SerjantArbuz
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