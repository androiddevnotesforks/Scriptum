package sgtmelon.scriptum.cleanup.ui.screen.note

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.isDisplayed
import sgtmelon.scriptum.cleanup.basic.extension.isFocused
import sgtmelon.scriptum.cleanup.basic.extension.withBackgroundAppColor
import sgtmelon.scriptum.cleanup.basic.extension.withBackgroundAttr
import sgtmelon.scriptum.cleanup.basic.extension.withBackgroundColor
import sgtmelon.scriptum.cleanup.basic.extension.withCardBackground
import sgtmelon.scriptum.cleanup.basic.extension.withCursor
import sgtmelon.scriptum.cleanup.basic.extension.withHint
import sgtmelon.scriptum.cleanup.basic.extension.withSize
import sgtmelon.scriptum.cleanup.basic.extension.withSizeAttr
import sgtmelon.scriptum.cleanup.basic.extension.withText
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.cleanup.testData.State
import sgtmelon.scriptum.cleanup.ui.IKeyboardClose
import sgtmelon.scriptum.cleanup.ui.IPressBack
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.scriptum.cleanup.ui.part.panel.NotePanel
import sgtmelon.scriptum.cleanup.ui.part.toolbar.NoteToolbar
import sgtmelon.test.cappuccino.utils.imeOption
import sgtmelon.test.cappuccino.utils.typeText

/**
 * Class for UI control of [NoteActivity], [TextNoteFragment].
 */
class TextNoteScreen(
    override var state: State,
    override var item: NoteItem.Text,
    override val isRankEmpty: Boolean
) : ParentUi(),
    INoteScreen<TextNoteScreen, NoteItem.Text>,
    NoteToolbar.ImeCallback,
    INoteAfterConvert<RollNoteScreen>,
    IKeyboardClose,
    IPressBack {

    //region Views

    private val toolbarHolder = getViewById(R.id.note_toolbar_holder)
    private val panelHolder = getViewById(R.id.note_panel_holder)
    private val fragmentContainer = getViewById(R.id.note_fragment_container)

    private val parentContainer = getViewById(R.id.text_note_parent_container)

    private val contentCard = getViewById(R.id.text_note_content_card)
    private val contentScroll = getViewById(R.id.text_note_content_scroll)

    private val contentText = getViewById(R.id.text_note_content_text)
    private val contentEnter = getViewById(R.id.text_note_content_enter)

    fun toolbar(func: NoteToolbar<TextNoteScreen, NoteItem.Text>.() -> Unit) = apply {
        NoteToolbar(func, callback = this, imeCallback = this)
    }

    fun controlPanel(func: NotePanel<TextNoteScreen, NoteItem.Text>.() -> Unit) = apply {
        NotePanel(func, callback = this)
    }

    //endregion

    override var shadowItem: NoteItem.Text = item.deepCopy()

    override val inputControl = InputControl()

    override fun fullAssert() = apply {
        assert()
        toolbar { assert() }
        controlPanel { assert() }
    }


    fun onEnterText(text: String = "") = apply {
        contentEnter.typeText(text)

        if (text.isEmpty()) {
            val valueFrom = shadowItem.text
            inputControl.onTextChange(
                valueFrom, valueTo = "", cursor = InputItem.Cursor(valueFrom.length, 0)
            )
        } else {
            for ((i, c) in text.withIndex()) {
                val valueFrom = if (i == 0) shadowItem.text else text[i - 1].toString()
                val valueTo = c.toString()

                inputControl.onTextChange(
                    valueFrom, valueTo, InputItem.Cursor(valueFrom.length, valueTo.length)
                )
            }
        }

        shadowItem.text = text
        fullAssert()
    }

    /**
     * TODO improve ime option
     */
    fun onImeOptionText() = apply {
        throwOnWrongState(State.EDIT, State.NEW) {
            contentEnter.imeOption()
        }
    }

    //region Common callback functions

    override fun assertToolbarIme() = assertFocus()

    override fun afterConvert(func: RollNoteScreen.() -> Unit) {
        RollNoteScreen(func, State.READ, item.onConvert(), isRankEmpty)
    }

    override fun onPressBack() {
        super.onPressBack()

        if (state == State.EDIT || state == State.NEW) {
            if (shadowItem.isSaveEnabled()) {
                state = State.READ
                item = shadowItem.deepCopy()
                inputControl.reset()
                fullAssert()
            } else if (state == State.EDIT) {
                state = State.READ
                shadowItem = item.deepCopy()
                inputControl.reset()
                fullAssert()
            }
        }
    }

    //endregion

    //region Assertion

    fun assertFocus() = throwOnWrongState(State.EDIT, State.NEW) {
        contentEnter.isFocused().withCursor(shadowItem.text.length)
    }

    fun assert() {
        toolbarHolder.withBackgroundAppColor(appTheme, item.color, needDark = false)
            .withSizeAttr(heightAttr = android.R.attr.actionBarSize)
        panelHolder.withBackgroundAttr(R.attr.clPrimary)
            .withSize(heightId = R.dimen.note_panel_height)

        fragmentContainer.isDisplayed()

        parentContainer.isDisplayed()
        contentCard.isDisplayed().withCardBackground(
            R.attr.clBackgroundView,
            R.dimen.text_card_radius,
            R.dimen.text_card_elevation
        )
        contentScroll.isDisplayed()

        when (state) {
            State.READ, State.BIN -> {
                contentText.isDisplayed()
                    .withBackgroundColor(android.R.color.transparent)
                    .withText(item.text, R.attr.clContent, R.dimen.text_18sp)

                contentEnter.isDisplayed(isVisible = false)
            }
            State.EDIT, State.NEW -> {
                contentText.isDisplayed(isVisible = false)

                /**
                 * TODO not work with: withImeAction(EditorInfo.IME_ACTION_UNSPECIFIED)
                 */
                val text = shadowItem.text
                contentEnter.isDisplayed()
                    .withBackgroundColor(android.R.color.transparent)
                    .apply {
                        if (text.isNotEmpty()) {
                            withText(text, R.attr.clContent, R.dimen.text_18sp)
                        } else {
                            withHint(R.string.hint_enter_text, R.attr.clDisable, R.dimen.text_18sp)
                        }
                    }
            }
        }
    }

    //endregion

    companion object {
        operator fun invoke(
            func: TextNoteScreen.() -> Unit,
            state: State,
            item: NoteItem.Text,
            isRankEmpty: Boolean
        ): TextNoteScreen {
            return TextNoteScreen(state, item, isRankEmpty).fullAssert().apply(func)
        }
    }
}