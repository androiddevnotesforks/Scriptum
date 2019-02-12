package sgtmelon.scriptum.ui.screen.note.text

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.ui.widget.note.State
import sgtmelon.scriptum.ui.widget.note.panel.NotePanelUi
import sgtmelon.scriptum.ui.widget.note.toolbar.NoteToolbarUi

class TextNoteAssert : BasicMatch() {

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

        NoteToolbarUi { assert { onDisplayContent(state) } }
        NotePanelUi { assert { onDisplayContent(state, NoteType.TEXT) } }
    }

}