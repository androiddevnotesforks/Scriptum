package sgtmelon.scriptum.ui.screen.note

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
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
class TextNoteScreen(override var state: State, override val noteModel: NoteModel)
    : ParentUi(), INoteScreen {

    override val inputControl = InputControl()

    fun assert(func: Assert.() -> Unit) = Assert(state, noteModel).apply { func() }

    fun toolbar(func: NoteToolbar.() -> Unit) = NoteToolbar.invoke(func, callback = this)

    fun controlPanel(func: NotePanel.() -> Unit) = NotePanel.invoke(func, callback = this)

    fun onEnterText(text: String) = action { onEnter(R.id.text_note_content_enter, text) }

    fun onPressBack() {
        closeSoftKeyboard()
        pressBack()
    }

    companion object {
        operator fun invoke(func: TextNoteScreen.() -> Unit, state: State, noteModel: NoteModel) =
                TextNoteScreen(state, noteModel).apply {
                    assert { onDisplayContent() }
                    controlPanel { assert { onDisplayContent(state) } }
                    func()
                }
    }

    class Assert(private val state: State, private val noteModel: NoteModel) : BasicMatch() {

        fun onDisplayContent() = with(noteModel) {
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

            when (state) {
                State.READ, State.BIN -> onDisplay(R.id.text_note_content_text, noteEntity.text)
                State.EDIT, State.NEW -> if (noteEntity.text.isNotEmpty()) {
                    onDisplay(R.id.text_note_content_enter, noteEntity.text)
                } else {
                    onDisplayHint(R.id.text_note_content_enter, R.string.hint_enter_text)
                }
            }
        }

    }

}