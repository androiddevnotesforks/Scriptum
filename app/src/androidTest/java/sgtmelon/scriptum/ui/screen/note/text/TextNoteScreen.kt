package sgtmelon.scriptum.ui.screen.note.text

import sgtmelon.scriptum.ui.ParentUi

class TextNoteScreen : ParentUi() {

    companion object {
        operator fun invoke(func: TextNoteScreen.() -> Unit) = TextNoteScreen().apply { func() }
    }

    fun assert(func: TextNoteAssert.() -> Unit) = TextNoteAssert().apply { func() }

}