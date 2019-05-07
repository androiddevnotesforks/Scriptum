package sgtmelon.scriptum.ui.screen.note

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.view.note.NoteActivity
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.widget.NotePanel
import sgtmelon.scriptum.ui.widget.NoteToolbar


/**
 * Класс для ui контроля экрана [NoteActivity], [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
class TextNoteScreen : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun toolbar(func: NoteToolbar.() -> Unit) = NoteToolbar().apply {
        assert { onDisplayContent() }
        func()
    }

    fun controlPanel(func: NotePanel.() -> Unit) = NotePanel().apply { func() }

    fun onEnterText(text: String) = action { onEnter(sgtmelon.scriptum.R.id.text_note_content_enter, text) }

    fun onCloseNote() {
        if (Math.random() < 0.5) toolbar { onClickBack() } else onPressBack()
    }

    fun onPressBack() {
        closeSoftKeyboard()
        pressBack()
    }

    companion object {
        operator fun invoke(func: TextNoteScreen.() -> Unit) = TextNoteScreen().apply { func() }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(state: State) {
            onDisplay(sgtmelon.scriptum.R.id.text_note_parent_container)

            onDisplay(sgtmelon.scriptum.R.id.text_note_content_card)
            onDisplay(sgtmelon.scriptum.R.id.text_note_content_scroll)

            when (state) {
                State.READ, State.BIN -> {
                    notDisplay(sgtmelon.scriptum.R.id.text_note_content_enter)
                    onDisplay(sgtmelon.scriptum.R.id.text_note_content_text)
                }
                State.EDIT, State.NEW -> {
                    onDisplay(sgtmelon.scriptum.R.id.text_note_content_enter)
                    notDisplay(sgtmelon.scriptum.R.id.text_note_content_text)
                }
            }

            NotePanel { assert { onDisplayContent(state, NoteType.TEXT) } }
        }

    }

}