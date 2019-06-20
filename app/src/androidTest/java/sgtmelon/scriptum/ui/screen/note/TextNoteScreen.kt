package sgtmelon.scriptum.ui.screen.note

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.NoteEntity
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

    // TODO !! сделать передачу состояния заметки, и автоматические assert при вызове

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun toolbar(func: NoteToolbar.() -> Unit) = NoteToolbar.invoke(func)

    fun controlPanel(func: NotePanel.() -> Unit) = NotePanel.invoke(NoteType.TEXT, func)

    fun onEnterText(text: String) = action { onEnter(R.id.text_note_content_enter, text) }

    fun onPressBack() {
        closeSoftKeyboard()
        pressBack()
    }

    companion object {
        operator fun invoke(state: State, noteEntity: NoteEntity, func: TextNoteScreen.() -> Unit) =
                TextNoteScreen().apply {
                    assert {
                        onDisplayContent(state)
                        onDisplayText(state, noteEntity.text)
                    }
                    controlPanel { assert { onDisplayContent(state) } }
                    func()
                }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(state: State) {
            onDisplay(R.id.text_note_parent_container)

            onDisplay(R.id.text_note_content_card)
            onDisplay(R.id.text_note_content_scroll)

            when (state) {
                State.READ, State.BIN -> {
                    notDisplay(R.id.text_note_content_enter)
                    onDisplay(R.id.text_note_content_text)
                }
                State.EDIT, State.NEW -> {
                    onDisplay(R.id.text_note_content_enter)
                    notDisplay(R.id.text_note_content_text)
                }
            }
        }

        fun onDisplayText(state: State, text: String) = when (state) {
            State.READ, State.BIN -> onDisplay(R.id.text_note_content_text, text)
            State.EDIT, State.NEW -> if (text.isNotEmpty()) {
                onDisplay(R.id.text_note_content_enter, text)
            } else {
                onDisplayHint(R.id.text_note_content_enter, R.string.hint_enter_text)
            }
        }

    }

}