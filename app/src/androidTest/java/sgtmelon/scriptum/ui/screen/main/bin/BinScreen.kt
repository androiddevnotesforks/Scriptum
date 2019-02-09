package sgtmelon.scriptum.ui.screen.main.bin

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.note.NoteDialogUi

class BinScreen : ParentRecyclerScreen(R.id.bin_recycler) {

    companion object {
        operator fun invoke(func: BinScreen.() -> Unit) = BinScreen().apply { func() }
    }

    fun assert(func: BinAssert.() -> Unit) = BinAssert().apply { func() }
    fun noteDialog(func: NoteDialogUi.() -> Unit) = NoteDialogUi().apply { func() }

    fun onLongClickItem(position: Int = positionRandom) =
            action { onLongClick(recyclerId, position) }

}