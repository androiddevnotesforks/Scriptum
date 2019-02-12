package sgtmelon.scriptum.ui.screen.note.roll

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.ui.widget.note.State
import sgtmelon.scriptum.ui.widget.note.panel.NotePanelUi
import sgtmelon.scriptum.ui.widget.note.panel.RollEnterPanelUi
import sgtmelon.scriptum.ui.widget.note.toolbar.NoteToolbarUi

class RollNoteAssert : BasicMatch() {

    fun onDisplayContent(state: State) {
        onDisplay(R.id.roll_note_parent_container)
        onDisplay(R.id.roll_note_recycler)

        NoteToolbarUi { assert { onDisplayContent(state) } }
        RollEnterPanelUi { assert { onDisplayContent(state) } }
        NotePanelUi { assert { onDisplayContent(state, NoteType.ROLL) } }
    }

}