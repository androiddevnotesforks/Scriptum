package sgtmelon.scriptum.ui.widget.note.toolbar

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.ui.widget.note.STATE

class NoteToolbarAssert : BasicMatch() {

    // TODO (hint check) (focus on title check)

    fun onDisplayContent(state: STATE) { // TODO (when theme dark - onDisplayIndicator R.id.toolbar_note_color_view)
        onDisplay(R.id.toolbar_note_container)

        when(state) {
            STATE.READ, STATE.BIN -> {
                doesNotDisplay(R.id.toolbar_note_enter)
                onDisplay(R.id.toolbar_note_scroll)
                onDisplay(R.id.toolbar_note_text)
            }
            STATE.EDIT -> {
                onDisplay(R.id.toolbar_note_enter)
                doesNotDisplay(R.id.toolbar_note_scroll)
                doesNotDisplay(R.id.toolbar_note_text)
            }
        }
    }

}