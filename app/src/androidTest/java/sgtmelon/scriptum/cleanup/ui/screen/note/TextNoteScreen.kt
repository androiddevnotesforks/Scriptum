package sgtmelon.scriptum.cleanup.ui.screen.note

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.part.panel.NotePanel
import sgtmelon.scriptum.cleanup.ui.part.toolbar.NoteToolbar
import sgtmelon.scriptum.data.noteHistory.NoteHistoryImpl
import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.data.noteHistory.model.HistoryChange
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.note.deepCopy
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onConvert
import sgtmelon.scriptum.parent.ui.feature.BackPress
import sgtmelon.scriptum.parent.ui.feature.KeyboardClose
import sgtmelon.scriptum.parent.ui.feature.ToolbarBack
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.scriptum.parent.ui.parts.ContainerPart
import sgtmelon.scriptum.parent.ui.parts.toolbar.ToolbarPart
import sgtmelon.scriptum.parent.ui.screen.note.NoteScreen
import sgtmelon.test.cappuccino.utils.imeOption
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isFocused
import sgtmelon.test.cappuccino.utils.typeText
import sgtmelon.test.cappuccino.utils.withBackgroundColor
import sgtmelon.test.cappuccino.utils.withCard
import sgtmelon.test.cappuccino.utils.withCursor
import sgtmelon.test.cappuccino.utils.withHint
import sgtmelon.test.cappuccino.utils.withText

/**
 * Class for UI control of [NoteActivity], [TextNoteFragmentImpl].
 */
class TextNoteScreen(
    override var state: NoteState,
    override var item: NoteItem.Text,
    override val isRankEmpty: Boolean
) : ContainerPart(TestViewTag.TEXT_NOTE),
    INoteScreen<TextNoteScreen, NoteItem.Text>,
    NoteToolbar.ImeCallback,
    INoteAfterConvert<RollNoteScreen>,
    KeyboardClose,
    ToolbarBack,
    BackPress {

    //region Views

    private val contentCard = getView(R.id.content_card)
    private val contentScroll = getView(R.id.content_scroll)

    private val textEnter = getView(R.id.text_enter)
    private val textRead = getView(R.id.text_read)

    override val toolbar: ToolbarPart get() = toolbar()

    fun toolbar(
        func: NoteToolbar<TextNoteScreen, NoteItem.Text>.() -> Unit = {}
    ): NoteToolbar<*, *> {
        return NoteToolbar(func, parentContainer, callback = this, imeCallback = this)
    }

    fun controlPanel(func: NotePanel<TextNoteScreen, NoteItem.Text>.() -> Unit) = apply {
        NotePanel(func, callback = this)
    }

    //endregion

    override var shadowItem: NoteItem.Text = item.deepCopy()

    override val history = NoteHistoryImpl()

    override fun fullAssert() = apply {
        assert()
        toolbar { assert() }
        controlPanel { assert() }
    }


    fun onEnterText(text: String = "") = apply {
        textEnter.typeText(text)

        if (text.isEmpty()) {
            val valueFrom = shadowItem.text

            /** Remember what text isEmpty - valueTo="", cursorTo=0 */
            val action = HistoryAction.Text.Enter(
                HistoryChange(valueFrom, text),
                HistoryChange(valueFrom.length, text.length)
            )
            history.add(action)
        } else {
            for ((i, c) in text.withIndex()) {
                val valueFrom = if (i == 0) shadowItem.text else text[i - 1].toString()
                val valueTo = c.toString()

                val action = HistoryAction.Text.Enter(
                    HistoryChange(valueFrom, valueTo),
                    HistoryChange(valueFrom.length, valueTo.length)
                )
                history.add(action)
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
            textEnter.imeOption()
        }
    }

    //region Common callback functions

    override fun assertToolbarIme() = assertFocus()

    override fun afterConvert(func: RollNoteScreen.() -> Unit) {
        NoteScreen().openRoll(func, NoteState.READ, item.onConvert(), isRankEmpty)
    }

    override fun pressBack() {
        super.pressBack()

        if (state == NoteState.EDIT || state == NoteState.NEW) {
            if (shadowItem.isSaveEnabled) {
                state = NoteState.READ
                item = shadowItem.deepCopy()
                history.reset()
                fullAssert()
            } else if (state == NoteState.EDIT) {
                state = NoteState.READ
                shadowItem = item.deepCopy()
                history.reset()
                fullAssert()
            }
        }
    }

    //endregion

    //region Assertion

    fun assertFocus() = throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
        textEnter.isFocused().withCursor(shadowItem.text.length)
    }

    fun assert() {
        parentContainer.isDisplayed()

        contentCard.isDisplayed().withCard(
            R.attr.clBackgroundView,
            R.dimen.item_card_radius,
            R.dimen.item_card_elevation
        )
        contentScroll.isDisplayed()

        when (state) {
            NoteState.READ, NoteState.BIN -> {
                textRead.isDisplayed()
                    .withBackgroundColor(android.R.color.transparent)
                    .withText(item.text, R.attr.clContent, R.dimen.text_18sp)

                textEnter.isDisplayed(value = false)
            }
            NoteState.EDIT, NoteState.NEW -> {
                textRead.isDisplayed(value = false)

                /**
                 * TODO not work with: withImeAction(EditorInfo.IME_ACTION_UNSPECIFIED)
                 */
                val text = shadowItem.text
                textEnter.isDisplayed()
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
            return TextNoteScreen(state, item, isRankEmpty).fullAssert().apply(func)
        }
    }
}