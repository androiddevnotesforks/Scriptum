package sgtmelon.scriptum.parent.ui.feature

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.parent.ui.model.exception.EmptyListException
import sgtmelon.scriptum.parent.ui.screen.dialogs.NoteDialogUi
import sgtmelon.test.cappuccino.utils.getRandomPosition

/**
 * Abstraction for screens with ability to open [NoteItem] dialog.
 */
interface OpenNoteDialog {

    val recyclerView: Matcher<View>

    fun getItem(p: Int): Callback

    fun openNoteDialog(
        item: NoteItem,
        p: Int? = recyclerView.getRandomPosition(),
        func: NoteDialogUi.() -> Unit = {}
    ) = apply {
        if (p == null) throw EmptyListException()

        getItem(p).openDialog(item, func)
    }


    interface Callback {

        fun dialogClick(item: NoteItem)

        fun openDialog(item: NoteItem, func: NoteDialogUi.() -> Unit) {
            dialogClick(item)
            NoteDialogUi(func, item)
        }
    }
}