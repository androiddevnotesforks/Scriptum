package sgtmelon.scriptum.ui.widget.note.toolbar

class NoteToolbarUi {

    companion object {
        operator fun invoke(func: NoteToolbarUi.() -> Unit) = NoteToolbarUi().apply { func() }
    }

    fun assert(func: NoteToolbarAssert.() -> Unit) = NoteToolbarAssert().apply { func() }

}