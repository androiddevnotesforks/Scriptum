package sgtmelon.scriptum.ui.screen.note

import android.view.View
import android.view.inputmethod.EditorInfo
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.adapter.RollAdapter
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.note.RollNoteViewModel.Companion.getCorrectPosition
import sgtmelon.scriptum.presentation.screen.vm.impl.note.RollNoteViewModel.Companion.hide
import sgtmelon.scriptum.presentation.screen.vm.impl.note.RollNoteViewModel.Companion.isVisible
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentRecyclerItem
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.part.info.RollNoteInfoContainer
import sgtmelon.scriptum.ui.part.panel.NotePanel
import sgtmelon.scriptum.ui.part.panel.RollEnterPanel
import sgtmelon.scriptum.ui.part.toolbar.NoteToolbar

/**
 * Class for UI control of [NoteActivity], [RollNoteFragment].
 */
class RollNoteScreen(
        override var state: State,
        override var noteItem: NoteItem,
        override val isRankEmpty: Boolean
) : ParentRecyclerScreen(R.id.roll_note_recycler),
        INoteScreen<RollNoteScreen>,
        NoteToolbar.ImeCallback,
        INoteAfterConvert<TextNoteScreen>,
        IPressBack {

    //region Views

    private val toolbarHolder = getViewById(R.id.note_toolbar_holder)
    private val panelHolder = getViewById(R.id.note_panel_holder)
    private val fragmentContainer = getViewById(R.id.note_fragment_container)

    private val visibleMenuItem = getViewById(R.id.item_visible)

    private fun getInfoContainer(): RollNoteInfoContainer? {
        val isListEmpty = noteItem.rollList.size == 0
        val isListHide = !isVisible && noteItem.rollList.hide().size == 0

        return RollNoteInfoContainer(isListEmpty, isListHide)
    }

    private val parentContainer = getViewById(R.id.roll_note_parent_container)
    private val progressBar = getViewById(R.id.roll_note_progress)

    private fun getItem(position: Int) = Item(recyclerView, position, state)

    /**
     * Cause of [RollEnterPanel.enterText] is local variable, need return
     * singleton in [enterPanel].
     */
    private var enterPanel: RollEnterPanel<RollNoteScreen>? = null

    fun toolbar(func: NoteToolbar<RollNoteScreen>.() -> Unit) = apply {
        NoteToolbar(func, callback = this, imeCallback = this)
    }

    fun enterPanel(func: RollEnterPanel<RollNoteScreen>.() -> Unit) = apply {
        enterPanel?.apply(func) ?: RollEnterPanel(func, callback = this).also {
            enterPanel = it
        }
    }

    fun controlPanel(func: NotePanel<RollNoteScreen>.() -> Unit) = apply {
        NotePanel(func, callback = this)
    }

    //endregion

    override var shadowItem: NoteItem = noteItem.deepCopy()

    override val inputControl = InputControl().apply { isEnabled = true }

    override fun fullAssert() = apply {
        assert()
        toolbar { assert() }
        controlPanel { assert() }
        enterPanel { assert() }
    }

    fun onEnterText(text: String = "", p: Int? = random) = apply {
        if (p == null) return@apply

        throwOnWrongState(State.EDIT, State.NEW) {
            getItem(p).rollText.typeText(text)

            val correctPosition = getCorrectPosition(p, noteItem)

            val item = shadowItem.rollList[correctPosition]
            item.text = text

            getItem(p).assert(item)
        }
    }

    fun onImeOptionText(p: Int? = random) = apply {
        if (p == null) return@apply

        throwOnWrongState(State.EDIT, State.NEW) {
            getItem(p).rollText.imeOption()
            enterPanel { assertFocus() }
        }
    }

    fun onClickVisible() = apply {
        visibleMenuItem.click()

        fullAssert()
        onAssertAll()
    }

    fun onClickCheck(p: Int? = random) = apply {
        if (p == null) return@apply

        when(state) {
            State.READ, State.BIN -> {
                getItem(p).clickButton.click()

                val correctPosition = getCorrectPosition(p, noteItem)

                noteItem.onItemCheck(correctPosition)

                if (isVisible) {
                    getItem(p).assert(noteItem.rollList[correctPosition])
                }
            }
            State.EDIT, State.NEW -> throw IllegalAccessException(STATE_ERROR_TEXT)
        }
    }

    fun onLongClickCheck(p: Int? = random) = apply {
        if (p == null) return@apply

        when(state) {
            State.READ, State.BIN -> {
                getItem(p).clickButton.longClick()

                noteItem.onItemLongCheck()

                onAssertAll()
            }
            State.EDIT, State.NEW -> throw IllegalAccessException(STATE_ERROR_TEXT)
        }
    }

    fun onSwipeAll() {
        repeat(times = count) { onSwipe() }
    }

    fun onSwipe(p: Int? = random) {
        if (p == null) return

        waitAfter(SWIPE_TIME) { recyclerView.swipeItem(p) }

        val correctPosition = getCorrectPosition(p, noteItem)

        shadowItem.rollList.apply {
            removeAt(correctPosition)
            forEachIndexed { i, item -> item.position = i }
        }

        assert()
    }


    override fun assertToolbarIme() = throwOnWrongState(State.EDIT, State.NEW) {
        enterPanel { assertFocus() }
    }

    override fun afterConvert(func: TextNoteScreen.() -> Unit) {
        TextNoteScreen(func, State.READ, noteItem, isRankEmpty)
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

    fun onAssertAll() {
        val list = when(state) {
            State.READ, State.BIN -> noteItem.rollList
            State.EDIT, State.NEW -> shadowItem.rollList
        }

        if (isVisible) {
            list.forEachIndexed { p, item -> getItem(p).assert(item) }
        } else {
            list.hide().forEachIndexed { p, item -> getItem(p).assert(item) }
        }
    }


    fun assert() {
        toolbarHolder.withBackgroundAppColor(theme, noteItem.color, needDark = false)
                .withSizeAttr(heightAttr = android.R.attr.actionBarSize)
        panelHolder.withBackgroundAttr(R.attr.clPrimary)
                .withSize(heightId = R.dimen.note_panel_height)

        fragmentContainer.isDisplayed()

        getInfoContainer()?.assert(when(state) {
            State.READ, State.BIN -> noteItem.rollList
            State.EDIT, State.NEW -> shadowItem.rollList
        }.let {
            if (isVisible) it.size == 0 else it.hide().size == 0
        })

        toolbar {
            val value = isVisible

            val itemIcon = if (value) R.drawable.ic_visible_enter else R.drawable.ic_visible_exit
            val itemTint = if (!value && theme == Theme.DARK) R.attr.clAccent else R.attr.clContent
            val itemTitle = if (value) R.string.menu_roll_visible else R.string.menu_roll_invisible

            contentContainer
                    .withMenuDrawable(R.id.item_visible, itemIcon, itemTint)
                    .withMenuTitle(R.id.item_visible, itemTitle)
        }

        parentContainer.isDisplayed()
        progressBar.isDisplayed(visible = state == State.READ || state == State.BIN) {
            withSize(heightId = R.dimen.layout_4dp)
            withProgress(noteItem.getCheck(), noteItem.rollList.size)
        }

        recyclerView.isDisplayed()
    }

    /**
     * Class for UI control of [RollAdapter].
     */
    class Item(listMatcher: Matcher<View>, position: Int, private val state: State) :
            ParentRecyclerItem<RollItem>(listMatcher, position) {

        private val parentCard by lazy {
            getChild(getViewById(when (state) {
                State.READ, State.BIN -> R.id.roll_read_parent_card
                State.EDIT, State.NEW -> R.id.roll_write_parent_card
            }))
        }

        private val checkBox by lazy {
            getChild(getViewById(when (state) {
                State.READ, State.BIN -> R.id.roll_read_check
                State.EDIT, State.NEW -> R.id.roll_write_check
            }))
        }

        val clickButton by lazy {
            getChild(getViewById(when (state) {
                State.READ, State.BIN -> R.id.roll_read_click_button
                State.EDIT, State.NEW -> R.id.roll_write_drag_button
            }))
        }

        val rollText by lazy {
            getChild(getViewById(when (state) {
                State.READ, State.BIN -> R.id.roll_read_text
                State.EDIT, State.NEW -> R.id.roll_write_enter
            }))
        }

        override fun assert(item: RollItem) {
            parentCard.isDisplayed().withCardBackground(R.attr.clBackgroundView)

            val textColor = if (!item.isCheck) R.attr.clContent else R.attr.clContrast

            when(state) {
                State.READ, State.BIN -> {
                    checkBox.isDisplayed().isChecked(item.isCheck)

                    val description = context.getString(if (item.isCheck) {
                        R.string.description_item_roll_uncheck
                    } else {
                        R.string.description_item_roll_check
                    }).plus(other = " ").plus(item.text)

                    clickButton.isDisplayed(visible = state != State.BIN)
                            .withContentDescription(description)

                    rollText.isDisplayed().withText(item.text, textColor, R.dimen.text_18sp)
                            .withBackgroundColor(android.R.color.transparent)
                }
                State.EDIT, State.NEW -> {
                    checkBox.isDisplayed(visible = false)

                    val color =  if (item.isCheck) R.attr.clAccent else R.attr.clContent
                    val description = context.getString(R.string.description_item_roll_move).plus(other = " ").plus(item.text)
                    clickButton.isDisplayed()
                            .withDrawableAttr(R.drawable.ic_move, color)
                            .withContentDescription(description)

                    rollText.isDisplayed()
                            .withImeAction(EditorInfo.IME_ACTION_NEXT)
                            .withBackgroundColor(android.R.color.transparent)
                            .apply {
                                if (item.text.isNotEmpty()) {
                                    withText(item.text, textColor, R.dimen.text_18sp)
                                } else {
                                    withHint(R.string.hind_enter_roll_empty, R.attr.clDisable, R.dimen.text_18sp)
                                }
                            }
                }
            }
        }

    }

    companion object {
        private const val SWIPE_TIME = 150L

        private const val STATE_ERROR_TEXT = "Wrong note state"
        private const val TYPE_ERROR_TEXT = "Wrong note type"

        operator fun invoke(func: RollNoteScreen.() -> Unit, state: State,
                            noteItem: NoteItem, isRankEmpty: Boolean): RollNoteScreen {
            if (noteItem.type != NoteType.ROLL) {
                throw IllegalAccessException(TYPE_ERROR_TEXT)
            }

            return RollNoteScreen(state, noteItem, isRankEmpty).fullAssert().apply(func)
        }
    }

}