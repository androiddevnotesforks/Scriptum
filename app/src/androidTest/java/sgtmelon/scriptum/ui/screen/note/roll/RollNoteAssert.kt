package sgtmelon.scriptum.ui.screen.note.roll

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.ui.widget.note.STATE
import sgtmelon.scriptum.ui.widget.note.panel.NotePanelUi
import sgtmelon.scriptum.ui.widget.note.panel.RollEnterPanelUi
import sgtmelon.scriptum.ui.widget.note.toolbar.NoteToolbarUi

class RollNoteAssert : BasicMatch() {

    fun onDisplayContent(state: STATE) { // TODO добавить наполнение в зависимости от открывающегося экрана заметки (ENUM)
        onDisplay(R.id.roll_note_parent_container)
        onDisplay(R.id.roll_note_recycler)

        NoteToolbarUi { assert { onDisplayContent(state) } }
        RollEnterPanelUi { assert { onDisplayContent(state) } }
        NotePanelUi { assert { onDisplayContent(state) } }
    }

}