package sgtmelon.scriptum.ui.screen.note

import androidx.test.espresso.Espresso.closeSoftKeyboard
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.widget.NotePanel
import sgtmelon.scriptum.ui.widget.NoteToolbar

class TextNoteScreen : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun addNote(noteItem: NoteItem) = action {
        assert { onDisplayContent(State.NEW) }

        NoteToolbar { enterName(noteItem.name) }
        onEnter(R.id.text_note_content_enter, noteItem.text)

        closeSoftKeyboard()
        NotePanel { onClickSave() }

        assert { onDisplayContent(State.READ) }
    }

    companion object {
        operator fun invoke(func: TextNoteScreen.() -> Unit) = TextNoteScreen().apply { func() }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(state: State) {
            onDisplay(R.id.text_note_parent_container)

            onDisplay(R.id.text_note_content_card)
            onDisplay(R.id.text_note_content_scroll)

            when(state) {
                State.READ, State.BIN -> {
                    doesNotDisplay(R.id.text_note_content_enter)
                    onDisplay(R.id.text_note_content_text)
                }
                State.EDIT, State.NEW -> {
                    onDisplay(R.id.text_note_content_enter)
                    doesNotDisplay(R.id.text_note_content_text)
                }
            }

            NoteToolbar { assert { onDisplayContent(state) } }
            NotePanel { assert { onDisplayContent(state, NoteType.TEXT) } }
        }

    }

}