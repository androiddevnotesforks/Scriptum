package sgtmelon.scriptum.ui.screen.note.text

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.ui.widget.note.STATE
import sgtmelon.scriptum.ui.widget.note.panel.NotePanelUi
import sgtmelon.scriptum.ui.widget.note.toolbar.NoteToolbarUi

class TextNoteAssert : BasicMatch() {

    fun onDisplayContent(state: STATE) { // TODO добавить наполнение в зависимости от открывающегося экрана заметки (ENUM)
        onDisplay(R.id.text_note_parent_container)

        onDisplay(R.id.text_note_card)
        onDisplay(R.id.text_note_scroll)

        when(state) {
            STATE.READ, STATE.BIN -> {
                doesNotDisplay(R.id.text_note_enter)
                onDisplay(R.id.text_note_text)
            }
            STATE.EDIT -> {
                onDisplay(R.id.text_note_enter)
                doesNotDisplay(R.id.text_note_text)
            }
        }

        NoteToolbarUi { assert { onDisplayContent(state) } }
        NotePanelUi { assert { onDisplayContent(state) } }
    }

}