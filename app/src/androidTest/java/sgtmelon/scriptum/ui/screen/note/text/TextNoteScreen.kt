package sgtmelon.scriptum.ui.screen.note.text

import androidx.test.espresso.Espresso.closeSoftKeyboard
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.widget.note.State
import sgtmelon.scriptum.ui.widget.note.panel.NotePanelUi
import sgtmelon.scriptum.ui.widget.note.toolbar.NoteToolbarUi

class TextNoteScreen : ParentUi() {

    fun assert(func: TextNoteAssert.() -> Unit) = TextNoteAssert().apply { func() }

    fun addNote(noteItem: NoteItem) = action {
        assert { onDisplayContent(State.NEW) }

        NoteToolbarUi { enterName(noteItem.name) }
        onEnter(R.id.text_note_content_enter, noteItem.text)

        closeSoftKeyboard()
        NotePanelUi { onClickSave() }

        assert { onDisplayContent(State.READ) }
    }

    companion object {
        operator fun invoke(func: TextNoteScreen.() -> Unit) = TextNoteScreen().apply { func() }
    }

}