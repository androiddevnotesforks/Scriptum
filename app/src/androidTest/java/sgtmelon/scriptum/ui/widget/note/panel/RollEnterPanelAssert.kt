package sgtmelon.scriptum.ui.widget.note.panel

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.ui.widget.note.STATE

class RollEnterPanelAssert : BasicMatch() {

    fun onDisplayContent(state: STATE) {
        when (state) {
            STATE.READ, STATE.BIN-> {
                doesNotDisplay(R.id.roll_note_enter_container)
                doesNotDisplay(R.id.roll_note_enter)
                doesNotDisplay(R.id.roll_note_add_button)
            }
            STATE.EDIT -> {
                onDisplay(R.id.roll_note_enter_container)
                onDisplay(R.id.roll_note_enter)
                onDisplay(R.id.roll_note_add_button)
            }
        }
    }

}