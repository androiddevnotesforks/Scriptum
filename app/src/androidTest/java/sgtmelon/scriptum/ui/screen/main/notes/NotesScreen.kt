package sgtmelon.scriptum.ui.screen.main.notes

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentRecyclerScreen

class NotesScreen : ParentRecyclerScreen(R.id.notes_recycler) {

    companion object {
        operator fun invoke(func: NotesScreen.() -> Unit) = NotesScreen().apply { func() }
    }

    fun assert(func: NotesAssert.() -> Unit) = NotesAssert().apply { func() }

}