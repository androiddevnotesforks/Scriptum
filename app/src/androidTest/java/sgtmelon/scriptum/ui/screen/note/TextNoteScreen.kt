package sgtmelon.scriptum.ui.screen.note

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.withHint
import sgtmelon.scriptum.basic.extension.withText
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.typeText
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.item.InputItem
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.part.panel.NotePanel
import sgtmelon.scriptum.ui.part.toolbar.NoteToolbar


/**
 * Class for UI control of [NoteActivity], [TextNoteFragment].
 */
class TextNoteScreen(override var state: State,
                     override var noteItem: NoteItem,
                     override val isRankEmpty: Boolean
) : ParentUi(), INoteScreen, IPressBack {

    //region Views

    private val parentContainer = getViewById(R.id.text_note_parent_container)

    private val contentCard = getViewById(R.id.text_note_content_card)
    private val contentScroll = getViewById(R.id.text_note_content_scroll)

    private val contentText = getViewById(R.id.text_note_content_text)
    private val contentEnter = getViewById(R.id.text_note_content_enter)

    //endregion

    override var shadowItem: NoteItem = noteItem.deepCopy()

    override val inputControl = InputControl().apply { isEnabled = true }

    override fun fullAssert() {
        assert()
        toolbar { assert() }
        controlPanel { assert() }
    }

    fun toolbar(func: NoteToolbar.() -> Unit) = apply {
        NoteToolbar.invoke(func, callback = this)
    }

    fun controlPanel(func: NotePanel.() -> Unit) = apply {
        NotePanel.invoke(func, callback = this)
    }

    fun onEnterText(text: String = "") = apply {
        contentEnter.typeText(text)

        if (text.isEmpty()) {
            val valueFrom = shadowItem.text
            inputControl.onTextChange(
                    valueFrom, valueTo = "", cursor = InputItem.Cursor(valueFrom.length, 0)
            )
        } else text.forEachIndexed { i, c ->
            val valueFrom = if (i == 0) shadowItem.text else text[i - 1].toString()
            val valueTo = c.toString()

            inputControl.onTextChange(
                    valueFrom, valueTo, InputItem.Cursor(valueFrom.length, valueTo.length)
            )
        }

        shadowItem.text = text
        fullAssert()
    }

    override fun onPressBack() {
        super.onPressBack()

        if (state == State.EDIT || state == State.NEW) {
            if (shadowItem.isSaveEnabled()) {
                state = State.READ
                noteItem = shadowItem.deepCopy()
                inputControl.reset()
                fullAssert()
            } else if (state == State.EDIT) {
                state = State.READ
                shadowItem = noteItem.deepCopy()
                inputControl.reset()
                fullAssert()
            }
        }
    }


    fun assert() {
        parentContainer.isDisplayed()
        contentCard.isDisplayed()
        contentScroll.isDisplayed()

        when (state) {
            State.READ, State.BIN -> {
                contentText.withText(noteItem.text).isDisplayed()
                contentEnter.isDisplayed(visible = false)
            }
            State.EDIT, State.NEW -> {
                contentText.isDisplayed(visible = false)

                val text = shadowItem.text
                contentEnter.isDisplayed().apply {
                    if (text.isNotEmpty()) withText(text) else withHint(R.string.hint_enter_text)
                }
            }
        }
    }

    companion object {
        operator fun invoke(func: TextNoteScreen.() -> Unit, state: State,
                            noteItem: NoteItem, isRankEmpty: Boolean = true): TextNoteScreen {
            if (noteItem.type != NoteType.TEXT) {
                throw IllegalAccessException("Wrong note type!")
            }

            return TextNoteScreen(state, noteItem, isRankEmpty).apply { fullAssert() }.apply(func)
        }
    }

}