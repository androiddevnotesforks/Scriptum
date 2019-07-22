package sgtmelon.scriptum.screen.ui.callback.main

import android.content.Intent
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.screen.vm.main.BinViewModel

/**
 * Interface for communication [BinViewModel] with [BinFragment]
 *
 * @author SerjantArbuz
 */
interface IBinFragment {

    fun setupToolbar()

    fun setupRecycler(@Theme theme: Int)

    fun bind()

    fun scrollTop()

    fun startActivity(intent: Intent)

    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun notifyMenuClearBin()

    fun notifyDataSetChanged(list: MutableList<NoteModel>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>)

}