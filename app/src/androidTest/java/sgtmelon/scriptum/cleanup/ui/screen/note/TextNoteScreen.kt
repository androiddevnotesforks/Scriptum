package sgtmelon.scriptum.cleanup.ui.screen.note

import sgtmelon.extensions.getCalendarText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.withBackgroundAppColor
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.cleanup.testData.DbDelegator
import sgtmelon.scriptum.cleanup.ui.IKeyboardClose
import sgtmelon.scriptum.cleanup.ui.ParentScreen
import sgtmelon.scriptum.cleanup.ui.part.panel.NotePanel
import sgtmelon.scriptum.cleanup.ui.part.toolbar.NoteToolbar
import sgtmelon.scriptum.parent.ui.feature.BackPress
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.test.cappuccino.utils.imeOption
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isFocused
import sgtmelon.test.cappuccino.utils.typeText
import sgtmelon.test.cappuccino.utils.withBackgroundAttr
import sgtmelon.test.cappuccino.utils.withBackgroundColor
import sgtmelon.test.cappuccino.utils.withCard
import sgtmelon.test.cappuccino.utils.withCursor
import sgtmelon.test.cappuccino.utils.withHint
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withSizeAttr
import sgtmelon.test.cappuccino.utils.withText

/**
 * Class for UI control of [NoteActivity], [TextNoteFragment].
 */
class TextNoteScreen(
    override var state: NoteState,
    override var item: NoteItem.Text,
    override val isRankEmpty: Boolean
) : ParentScreen(),
    INoteScreen<TextNoteScreen, NoteItem.Text>,
    NoteToolbar.ImeCallback,
    INoteAfterConvert<RollNoteScreen>,
    IKeyboardClose,
    BackPress {

    //region Views

    private val toolbarHolder = getViewById(R.id.toolbar_holder)
    private val panelHolder = getViewById(R.id.panel_holder)
    private val fragmentContainer = getViewById(R.id.fragment_container)

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
        throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
            contentEnter.imeOption()
        }
    }

    //region Common callback functions

    override fun assertToolbarIme() = assertFocus()

    override fun afterConvert(func: RollNoteScreen.() -> Unit) {
        RollNoteScreen(func, NoteState.READ, item.onConvert(), isRankEmpty)
    }

    override fun pressBack() {
        super.pressBack()

        if (state == NoteState.EDIT || state == NoteState.NEW) {
            if (shadowItem.isSaveEnabled()) {
                state = NoteState.READ
                item = shadowItem.deepCopy()
                inputControl.reset()
                fullAssert()
            } else if (state == NoteState.EDIT) {
                state = NoteState.READ
                shadowItem = item.deepCopy()
                inputControl.reset()
                fullAssert()
            }
        }
    }

    //endregion

    //region Assertion

    fun assertFocus() = throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
        contentEnter.isFocused().withCursor(shadowItem.text.length)
    }

    fun assert() {
        toolbarHolder.withBackgroundAppColor(appTheme, item.color, needDark = false)
            .withSizeAttr(heightAttr = android.R.attr.actionBarSize)
        panelHolder.withBackgroundAttr(R.attr.clPrimary)
            .withSize(heightId = R.dimen.note_panel_height)

        fragmentContainer.isDisplayed()

        parentContainer.isDisplayed()
        contentCard.isDisplayed().withCard(
            R.attr.clBackgroundView,
            R.dimen.text_card_radius,
            R.dimen.text_card_elevation
        )
        contentScroll.isDisplayed()

        when (state) {
            NoteState.READ, NoteState.BIN -> {
                contentText.isDisplayed()
                    .withBackgroundColor(android.R.color.transparent)
                    .withText(item.text, R.attr.clContent, R.dimen.text_18sp)

                contentEnter.isDisplayed(value = false)
            }
            NoteState.EDIT, NoteState.NEW -> {
                contentText.isDisplayed(value = false)

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
        inline operator fun invoke(
            func: TextNoteScreen.() -> Unit,
            state: NoteState,
            item: NoteItem.Text,
            isRankEmpty: Boolean
        ): TextNoteScreen {
            /**
             * Was assertion error in tests where time difference was 1 minute. I think it was
             * happened when calendar time was ~00:59 on note create inside [DbDelegator]. But time
             * of actual note creation was ~01:.. (after [DbDelegator] note was created).
             */
            if (state == NoteState.NEW) {
                item.create = getCalendarText()
            }

            return TextNoteScreen(state, item, isRankEmpty).fullAssert().apply(func)
        }
    }
}