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


/**
 * Класс для ui контроля экрана [NoteActivity], [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
class TextNoteScreen(override var state: State,
                     override val noteModel: NoteModel,
                     override val isRankEmpty: Boolean
) : ParentUi(), INoteScreen {

    fun assert(func: Assert.() -> Unit) = Assert(callback = this).apply { func() }

    fun toolbar(func: NoteToolbar.() -> Unit) = NoteToolbar.invoke(func, callback = this)

    fun controlPanel(func: NotePanel.() -> Unit) = NotePanel.invoke(func, callback = this)

    override val inputControl = InputControl()

    override fun fullAssert() {
        assert { onDisplayContent() }
        toolbar { assert { onDisplayContent() } }
        controlPanel { assert { onDisplayContent() } }
    }

    fun onEnterText(text: String) {
        action { onEnter(R.id.text_note_content_enter, text) }
        noteModel.noteEntity.text = text
        fullAssert()
    }

    // TODO #TEST возврат данных, контроль выхода с экрана
    fun onPressBack() {
        closeSoftKeyboard()
        pressBack()
    }

    companion object {
        operator fun invoke(func: TextNoteScreen.() -> Unit, state: State,
                            noteModel: NoteModel, isRankEmpty: Boolean = true) =
                TextNoteScreen(state, noteModel, isRankEmpty).apply {
                    fullAssert()
                    func()
                }
    }

    class Assert(private val callback: INoteScreen) : BasicMatch() {

        fun onDisplayContent(): Unit = with(callback.noteModel) {
            onDisplay(R.id.text_note_parent_container)

            onDisplay(R.id.text_note_content_card)
            onDisplay(R.id.text_note_content_scroll)

            when (callback.state) {
                State.READ, State.BIN -> {
                    notDisplay(R.id.text_note_content_enter)
                    onDisplay(R.id.text_note_content_text)
                }
                State.EDIT, State.NEW -> {
                    onDisplay(R.id.text_note_content_enter)
                    notDisplay(R.id.text_note_content_text)
                }
            }

            when (callback.state) {
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