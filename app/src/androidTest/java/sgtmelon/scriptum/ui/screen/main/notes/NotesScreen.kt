package sgtmelon.scriptum.ui.screen.main.notes

class NotesScreen {

    companion object {
        operator fun invoke(func: NotesScreen.() -> Unit) = NotesScreen().apply { func() }
    }

    fun assert(func: NotesAssert.() -> Unit) = NotesAssert().apply { func() }

}