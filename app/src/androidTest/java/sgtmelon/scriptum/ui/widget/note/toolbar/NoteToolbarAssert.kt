package sgtmelon.scriptum.ui.widget.note.toolbar

import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.def.ThemeDef
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.widget.note.State

class NoteToolbarAssert : BasicMatch() {

    // TODO (hint check) (focus on title check)

    fun onDisplayContent(state: State) {
        onDisplay(R.id.toolbar_note_container)

        if (theme == ThemeDef.dark) onDisplay(R.id.toolbar_note_color_view)

        when (state) {
            State.READ, State.BIN -> {
                doesNotDisplay(R.id.toolbar_note_enter)
                onDisplay(R.id.toolbar_note_scroll)
                onDisplay(R.id.toolbar_note_text)
            }
            State.EDIT, State.NEW -> {
                onDisplay(R.id.toolbar_note_enter)
                doesNotDisplay(R.id.toolbar_note_scroll)
                doesNotDisplay(R.id.toolbar_note_text)
            }
        }
    }

}