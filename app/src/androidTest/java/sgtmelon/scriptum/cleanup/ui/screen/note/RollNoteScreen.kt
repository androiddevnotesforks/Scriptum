package sgtmelon.scriptum.cleanup.ui.screen.note

import sgtmelon.extensions.getCalendarText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.waitAfter
import sgtmelon.scriptum.cleanup.basic.extension.withBackgroundAppColor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.hide
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.RollNoteViewModel
import sgtmelon.scriptum.cleanup.testData.DbDelegator
import sgtmelon.scriptum.cleanup.ui.ParentRecyclerScreen
import sgtmelon.scriptum.cleanup.ui.item.RollItemUi
import sgtmelon.scriptum.cleanup.ui.part.panel.NotePanel
import sgtmelon.scriptum.cleanup.ui.part.panel.RollEnterPanel
import sgtmelon.scriptum.cleanup.ui.part.toolbar.NoteToolbar
import sgtmelon.scriptum.parent.ui.feature.BackPress
import sgtmelon.scriptum.parent.ui.feature.KeyboardClose
import sgtmelon.scriptum.parent.ui.model.key.InfoCase
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.scriptum.parent.ui.parts.info.InfoContainerPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.imeOption
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.swipeItem
import sgtmelon.test.cappuccino.utils.typeText
import sgtmelon.test.cappuccino.utils.withBackgroundAttr
import sgtmelon.test.cappuccino.utils.withMenuDrawable
import sgtmelon.test.cappuccino.utils.withMenuTitle
import sgtmelon.test.cappuccino.utils.withProgress
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withSizeAttr

/**
 * Class for UI control of [NoteActivity], [RollNoteFragment].
 *
 * Note:
 *  Call [NoteItem.Roll.isVisible] only from [INoteScreen.item] because it's a save way.
 *  It always set correct only on [INoteScreen.item].
 */
class RollNoteScreen(
    override var state: NoteState,
    override var item: NoteItem.Roll,
    override val isRankEmpty: Boolean
) : ParentRecyclerScreen(R.id.roll_note_recycler),
    INoteScreen<RollNoteScreen, NoteItem.Roll>,
    NoteToolbar.ImeCallback,
    INoteAfterConvert<TextNoteScreen>,
    KeyboardClose,
    BackPress {

    //region Views

    private val toolbarHolder = getViewById(R.id.toolbar_holder)
    private val panelHolder = getViewById(R.id.panel_holder)
    private val fragmentContainer = getViewById(R.id.fragment_container)

    private val visibleMenuItem = getViewById(R.id.item_visible)

    private fun getInfoPart(): InfoContainerPart {
        val list = when (state) {
            NoteState.READ, NoteState.BIN -> item.list
            NoteState.EDIT, NoteState.NEW -> shadowItem.list
        }

        val isListEmpty = list.size == 0
        val isListHide = !item.isVisible && list.hide().size == 0

        return InfoContainerPart(parentContainer, InfoCase.Roll(isListEmpty, isListHide))
    }

    private val parentContainer = getViewById(R.id.roll_note_parent_container)
    private val progressBar = getViewById(R.id.roll_note_progress)

    private fun getItem(p: Int) = RollItemUi(recyclerView, p, state)

    /**
     * Cause of [RollEnterPanel.enterText] is local variable, need return
     * singleton in [enterPanel].
     */
    private var enterPanel: RollEnterPanel<RollNoteScreen>? = null

    fun toolbar(func: NoteToolbar<RollNoteScreen, NoteItem.Roll>.() -> Unit) = apply {
        NoteToolbar(func, callback = this, imeCallback = this)
    }

    fun enterPanel(func: RollEnterPanel<RollNoteScreen>.() -> Unit) = apply {
        enterPanel?.apply(func) ?: RollEnterPanel(func, callback = this).also {
            enterPanel = it
        }
    }

    fun controlPanel(func: NotePanel<RollNoteScreen, NoteItem.Roll>.() -> Unit) = apply {
        NotePanel(func, callback = this)
    }

    //endregion

    override var shadowItem: NoteItem.Roll = item.deepCopy()

    override val inputControl = InputControl()

    override fun fullAssert() = apply {
        assert()
        toolbar { assert() }
        controlPanel { assert() }
        enterPanel { assert() }
    }


    fun onEnterText(text: String = "", p: Int? = random) = apply {
        if (p == null) return@apply

        throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
            getItem(p).rollText.typeText(text)

            val correctPosition = getCorrectPosition(p, item.list)

            val item = shadowItem.list[correctPosition]
            item.text = text

            getItem(p).assert(item)
        }
    }

    fun onImeOptionText(p: Int? = random) = apply {
        if (p == null) return@apply

        throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
            getItem(p).rollText.imeOption()
            enterPanel { assertFocus() }
        }
    }

    fun onClickVisible() = apply {
        visibleMenuItem.click()

        item.isVisible = !item.isVisible

        fullAssert()
        onAssertAll()
    }

    fun onClickCheck(p: Int? = random) = apply {
        if (p == null) return@apply

        when (state) {
            NoteState.READ, NoteState.BIN -> {
                getItem(p).clickButton.click()

                val correctPosition = getCorrectPosition(p, item.list)

                item.onItemCheck(correctPosition)

                if (item.isVisible) {
                    getItem(p).assert(item.list[correctPosition])
                }
            }
            NoteState.EDIT, NoteState.NEW -> throw IllegalAccessException(STATE_ERROR_TEXT)
        }
    }

    fun onSwipeAll() {
        repeat(times = count) { onSwipe() }
    }

    fun onSwipe(p: Int? = random) {
        if (p == null) return

        waitAfter(SWIPE_TIME) { recyclerView.swipeItem(p) }

        val correctPosition = getCorrectPosition(p, shadowItem.list)

        shadowItem.list.apply {
            removeAt(correctPosition)

            for ((i, item) in withIndex()) {
                item.position = i
            }
        }

        assert()
    }

    //region Common callback functions

    override fun assertToolbarIme() = throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
        enterPanel { assertFocus() }
    }

    override fun afterConvert(func: TextNoteScreen.() -> Unit) {
        TextNoteScreen(func, NoteState.READ, item.onConvert(), isRankEmpty)
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

    fun onAssertAll() {
        val list = when (state) {
            NoteState.READ, NoteState.BIN -> item.list
            NoteState.EDIT, NoteState.NEW -> shadowItem.list
        }
        val resultList = if (item.isVisible) list else list.hide()

        for ((i, it) in resultList.withIndex()) {
            getItem(i).assert(it)
        }
    }

    fun assert() {
        toolbarHolder.withBackgroundAppColor(appTheme, item.color, needDark = false)
            .withSizeAttr(heightAttr = android.R.attr.actionBarSize)
        panelHolder.withBackgroundAttr(R.attr.clPrimary)
            .withSize(heightId = R.dimen.note_panel_height)

        fragmentContainer.isDisplayed()

        getInfoPart().assert(
            when (state) {
                NoteState.READ, NoteState.BIN -> item.list
                NoteState.EDIT, NoteState.NEW -> shadowItem.list
            }.let {
                if (item.isVisible) it.size == 0 else it.hide().size == 0
            })

        toolbar {
            val value = item.isVisible
            // TODO do something with ic name (and search in all test ui classes)
            val itemIcon = if (value) {
                sgtmelon.iconanim.R.drawable.ic_visible_enter
            } else {
                sgtmelon.iconanim.R.drawable.ic_visible_exit
            }
            val itemTint = if (value) R.attr.clContent else R.attr.clIndicator
            val itemTitle = if (value) R.string.menu_roll_visible else R.string.menu_roll_invisible

            contentContainer
                .withMenuDrawable(R.id.item_visible, itemIcon, itemTint)
                .withMenuTitle(R.id.item_visible, itemTitle)
        }

        parentContainer.isDisplayed()
        progressBar.isDisplayed(value = state == NoteState.READ || state == NoteState.BIN) {
            withSize(heightId = R.dimen.layout_4dp)
            withProgress(item.getCheck(), item.list.size)
        }

        recyclerView.isDisplayed()
    }

    //endregion

    /**
     * @Test - duplicate of original function in [RollNoteViewModel].
     */
    private fun getCorrectPosition(p: Int, list: List<RollItem>): Int {
        return if (item.isVisible) p else list.indexOf(list.hide()[p])
    }

    companion object {
        private const val SWIPE_TIME = 150L

        private const val STATE_ERROR_TEXT = "Wrong note state"

        inline operator fun invoke(
            func: RollNoteScreen.() -> Unit,
            state: NoteState,
            item: NoteItem.Roll,
            isRankEmpty: Boolean
        ): RollNoteScreen {
            /**
             * Was assertion error in tests where difference between times 1 minute. I think it was
             * happened when calendar time was around ~00:59 on note creation inside [DbDelegator].
             * But the time of actual creation was ~01:.. (after [DbDelegator] note was created).
             */
            if (state == NoteState.NEW) {
                item.create = getCalendarText()
            }
            return RollNoteScreen(state, item, isRankEmpty).fullAssert().apply(func)
        }
    }
}