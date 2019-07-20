package sgtmelon.scriptum.ui.screen.note

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.item.InputItem
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch


/**
 * Класс для ui контроля экрана [NoteActivity], [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
class TextNoteScreen(override var state: State,
                     override var noteModel: NoteModel,
                     override val isRankEmpty: Boolean
) : ParentUi(), INoteScreen {

    override var shadowModel = NoteModel(noteModel)

    override val inputControl = InputControl().apply { isEnabled = true }

    override fun fullAssert() {
        assert()
        toolbar { assert() }
        controlPanel { assert() }
    }

    fun assert() = Assert(callback = this)
    fun toolbar(func: NoteToolbar.() -> Unit) = NoteToolbar.invoke(func, callback = this)
    fun controlPanel(func: NotePanel.() -> Unit) = NotePanel.invoke(func, callback = this)

    fun onEnterText(text: String) {
        action { onEnter(R.id.text_note_content_enter, text) }

        if (text.isEmpty()) {
            val valueFrom = shadowModel.noteEntity.text
            inputControl.onTextChange(
                    valueFrom, valueTo = "", cursor = InputItem.Cursor(valueFrom.length, 0)
            )
        } else text.forEachIndexed { i, c ->
            val valueFrom = if (i == 0) shadowModel.noteEntity.text else text[i - 1].toString()
            val valueTo = c.toString()

            inputControl.onTextChange(
                    valueFrom, valueTo, InputItem.Cursor(valueFrom.length, valueTo.length)
            )
        }

        shadowModel.noteEntity.text = text
        fullAssert()
    }

    fun onPressBack() {
        closeSoftKeyboard()
        pressBack()

        if (state == State.EDIT || state == State.NEW) {
            if (shadowModel.isSaveEnabled()) {
                state = State.READ
                noteModel = NoteModel(shadowModel)
                inputControl.reset()
                fullAssert()
            } else if (state == State.EDIT) {
                state = State.READ
                shadowModel = NoteModel(noteModel)
                inputControl.reset()
                fullAssert()
            }
        }
    }

    companion object {
        operator fun invoke(func: TextNoteScreen.() -> Unit, state: State,
                            noteModel: NoteModel, isRankEmpty: Boolean = true) =
                TextNoteScreen(state, noteModel, isRankEmpty).apply {
                    fullAssert()
                    func()
                }
    }

    class Assert(callback: INoteScreen) : BasicMatch() {

        init {
            with(callback) {
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
                    State.READ, State.BIN -> {
                        onDisplay(R.id.text_note_content_text, noteModel.noteEntity.text)
                    }
                    State.EDIT, State.NEW -> {
                        if (shadowModel.noteEntity.text.isNotEmpty()) {
                            onDisplay(R.id.text_note_content_enter, shadowModel.noteEntity.text)
                        } else {
                            onDisplayHint(R.id.text_note_content_enter, R.string.hint_enter_text)
                        }
                    }
                }
            }
        }

    }

}