package sgtmelon.scriptum.ui.screen.main.notes

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.note.NoteDialogUi

class NotesScreen : ParentRecyclerScreen(R.id.notes_recycler) {

    companion object {
        operator fun invoke(func: NotesScreen.() -> Unit) = NotesScreen().apply { func() }
    }

    fun assert(func: NotesAssert.() -> Unit) = NotesAssert().apply { func() }
    fun noteDialog(func: NoteDialogUi.() -> Unit) = NoteDialogUi().apply { func() }

    fun onLongClickItem(position: Int = positionRandom) =
            action { onLongClick(recyclerId, position) }

}