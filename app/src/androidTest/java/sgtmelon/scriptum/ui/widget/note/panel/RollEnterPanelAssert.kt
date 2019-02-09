package sgtmelon.scriptum.ui.widget.note.panel

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.ui.widget.note.State

class RollEnterPanelAssert : BasicMatch() {

    fun onDisplayContent(state: State) {
        when (state) {
            State.READ, State.BIN -> {
                doesNotDisplay(R.id.roll_note_enter_container)
                doesNotDisplay(R.id.roll_note_enter)
                doesNotDisplay(R.id.roll_note_add_button)
            }
            State.EDIT, State.NEW -> {
                onDisplay(R.id.roll_note_enter_container)
                onDisplay(R.id.roll_note_enter)
                onDisplay(R.id.roll_note_add_button)
            }
        }
    }

}