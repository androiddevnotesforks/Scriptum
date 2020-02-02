package sgtmelon.scriptum.screen.ui.callback.main

import androidx.annotation.ArrayRes
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


    /**
     * Calls before load data inside list.
     */
    fun beforeLoad()

    fun showProgress()


    fun onBindingList()

    fun scrollTop()

    fun startNoteActivity(item: NoteItem)

    fun showOptionsDialog(itemArray: Array<String>, p: Int)


    fun notifyMenuClearBin()

    fun notifyList(list: List<NoteItem>)

    fun notifyDataSetChanged(list: List<NoteItem>)

    fun notifyItemRemoved(list: List<NoteItem>, p: Int)


    fun getStringArray(@ArrayRes id: Int): Array<String>

}