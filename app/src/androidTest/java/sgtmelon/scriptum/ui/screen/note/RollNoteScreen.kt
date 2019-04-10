package sgtmelon.scriptum.ui.screen.note

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.widget.NotePanel
import sgtmelon.scriptum.ui.widget.NoteToolbar
import sgtmelon.scriptum.ui.widget.RollEnterPanel

class RollNoteScreen : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    companion object {
        operator fun invoke(func: RollNoteScreen.() -> Unit) = RollNoteScreen().apply { func() }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(state: State) {
            onDisplay(R.id.roll_note_parent_container)
            onDisplay(R.id.roll_note_recycler)

            NoteToolbar { assert { onDisplayContent(state) } }
            RollEnterPanel { assert { onDisplayContent(state) } }
            NotePanel { assert { onDisplayContent(state, NoteType.ROLL) } }
        }

    }

}