package sgtmelon.scriptum.ui.widget

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

class NoteToolbar : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun onEnterName(name: String) = action { onEnter(R.id.toolbar_note_enter, name) }

    fun onClickBack() = action { onClickToolbarButton() }

    companion object {
        operator fun invoke(func: NoteToolbar.() -> Unit) = NoteToolbar().apply {
            assert { onDisplayContent() }
            func()
        }
    }

    class Assert : BasicMatch() {

        // TODO (focus on title check)

        fun onDisplayContent() {
            onDisplay(R.id.toolbar_note_container)
            onDisplay(R.id.toolbar_note_scroll)

            if (theme == Theme.dark) {
                onDisplay(R.id.toolbar_note_color_view)
            } else {
                notDisplay(R.id.toolbar_note_color_view)
            }
        }

        fun onDisplayName(state: State, name: String) = when (state) {
            State.READ, State.BIN -> {
                notDisplay(R.id.toolbar_note_enter)

                if (name.isNotEmpty()) {
                    onDisplay(R.id.toolbar_note_text, name)
                } else {
                    onDisplayHint(R.id.toolbar_note_text, R.string.hint_view_name)
                }
            }
            State.EDIT, State.NEW -> {
                if (name.isNotEmpty()) {
                    onDisplay(R.id.toolbar_note_enter, name)
                } else {
                    onDisplayHint(R.id.toolbar_note_enter, R.string.hint_enter_name)
                }

                notDisplay(R.id.toolbar_note_text)
            }
        }

    }

}