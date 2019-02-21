package sgtmelon.scriptum.ui.widget.note.toolbar

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi

class NoteToolbarUi : ParentUi() {

    fun assert(func: NoteToolbarAssert.() -> Unit) = NoteToolbarAssert().apply { func() }

    fun enterName(name: String) = action { onEnter(R.id.toolbar_note_enter, name) }

    companion object {
        operator fun invoke(func: NoteToolbarUi.() -> Unit) = NoteToolbarUi().apply { func() }
    }

}