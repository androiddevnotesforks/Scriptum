package sgtmelon.scriptum.ui.screen.note.roll

import sgtmelon.scriptum.ui.ParentUi

class RollNoteScreen : ParentUi() {

    companion object {
        operator fun invoke(func: RollNoteScreen.() -> Unit) = RollNoteScreen().apply { func() }
    }

    fun assert(func: RollNoteAssert.() -> Unit) = RollNoteAssert().apply { func() }

}