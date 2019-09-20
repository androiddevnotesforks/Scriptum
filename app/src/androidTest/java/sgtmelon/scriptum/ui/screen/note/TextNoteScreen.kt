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
import sgtmelon.scriptum.basic.isDisplayed
import sgtmelon.scriptum.basic.typeText


/**
 * Class for UI control of [NoteActivity], [TextNoteFragment]
 */
class TextNoteScreen(override var state: State,
                     override var noteModel: NoteModel,
                     override val isRankEmpty: Boolean
) : ParentUi(), INoteScreen {

    //region Views

    private val parentContainer = getViewById(R.id.text_note_parent_container)

    private val contentCard = getViewById(R.id.text_note_content_card)
    private val contentScroll = getViewById(R.id.text_note_content_scroll)

    private val contentText = getViewById(R.id.text_note_content_text)
    private val contentEnter = getViewById(R.id.text_note_content_enter)

    //endregion

    override var shadowModel = NoteModel(noteModel)

    override val inputControl = InputControl().apply { isEnabled = true }

    override fun fullAssert() {
        assert()
        toolbar { assert() }
        controlPanel { assert() }
    }

    fun toolbar(func: NoteToolbar.() -> Unit) = NoteToolbar.invoke(func, callback = this)

    fun controlPanel(func: NotePanel.() -> Unit) = NotePanel.invoke(func, callback = this)

    fun onEnterText(text: String = "") {
        contentEnter.typeText(text)

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


    fun assert() {
        parentContainer.isDisplayed()
        contentCard.isDisplayed()
        contentScroll.isDisplayed()

        when (state) {
            State.READ, State.BIN -> {
                contentText.withText(noteModel.noteEntity.text).isDisplayed()
                contentEnter.isDisplayed(visible = false)
            }
            State.EDIT, State.NEW -> {
                contentText.isDisplayed(visible = false)

                if (shadowModel.noteEntity.text.isEmpty()) {
                    contentEnter.withHint(R.string.hint_enter_text).isDisplayed()
                } else {
                    contentEnter.withText(shadowModel.noteEntity.text).isDisplayed()
                }
            }
        }
    }

    companion object {
        operator fun invoke(func: TextNoteScreen.() -> Unit, state: State,
                            noteModel: NoteModel, isRankEmpty: Boolean = true) =
                TextNoteScreen(state, noteModel, isRankEmpty).apply { fullAssert() }.apply(func)
    }

}