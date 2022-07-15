package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.system.ClipboardControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IBinViewModel

/**
 * Interface for communication [IBinViewModel] with [BinFragment].
 */
interface IBinFragment : ClipboardControl.Bridge {

    fun setupToolbar()

    fun setupRecycler()

    fun setupDialog()


    fun prepareForLoad()

    fun showProgress()

    fun hideEmptyInfo()


    fun onBindingList()

    fun scrollTop()

    fun openNoteScreen(item: NoteItem)

    fun showOptionsDialog(title: String, itemArray: Array<String>, p: Int)


    fun notifyMenuClearBin()

    fun notifyList(list: List<NoteItem>)

    fun notifyDataSetChanged(list: List<NoteItem>)

    fun notifyItemRemoved(list: List<NoteItem>, p: Int)


    fun getString(@StringRes resId: Int): String

    fun getStringArray(@ArrayRes arrayId: Int): Array<String>

}