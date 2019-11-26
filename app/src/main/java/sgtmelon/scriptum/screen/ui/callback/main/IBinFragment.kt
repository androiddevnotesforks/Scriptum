package sgtmelon.scriptum.screen.ui.callback.main

import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.screen.vm.main.BinViewModel

/**
 * Interface for communication [BinViewModel] with [BinFragment]
 */
interface IBinFragment : IBinBridge{

    fun setupToolbar()

    fun setupRecycler(@Theme theme: Int)

    fun onBingingList()

    fun scrollTop()

    fun startNoteActivity(noteItem: NoteItem)

    fun showOptionsDialog(itemArray: Array<String>, p: Int)

    fun notifyMenuClearBin()

    fun notifyDataSetChanged(list: MutableList<NoteItem>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteItem>)

}