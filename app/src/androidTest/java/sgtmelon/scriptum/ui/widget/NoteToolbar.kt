package sgtmelon.scriptum.ui.widget

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.office.annot.def.ThemeDef
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

class NoteToolbar : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun enterName(name: String) = action { onEnter(R.id.toolbar_note_enter, name) }

    companion object {
        operator fun invoke(func: NoteToolbar.() -> Unit) = NoteToolbar().apply { func() }
    }

    class Assert : BasicMatch() {

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

}