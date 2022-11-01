package sgtmelon.scriptum.infrastructure.screen.main.bin

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * Interface for communication [IBinViewModel] with [BinFragment].
 */
interface IBinFragment {

    fun setupToolbar()

    fun setupRecycler()

    fun setupDialog()


    fun prepareForLoad()

    fun showProgress()

    fun hideEmptyInfo()


    fun onBindingList()

    fun scrollTop()

    @Deprecated("Create delegator for screens open")
    fun openNoteScreen(item: NoteItem)

    fun showOptionsDialog(title: String, itemArray: Array<String>, p: Int)


    fun notifyMenuClearBin()

    fun notifyList(list: List<NoteItem>)


    fun getString(@StringRes resId: Int): String

    fun getStringArray(@ArrayRes arrayId: Int): Array<String>

    fun copyClipboard(text: String)

}