package sgtmelon.scriptum.cleanup.ui.screen.note

import sgtmelon.extensions.getCalendarText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.testData.DbDelegator
import sgtmelon.scriptum.cleanup.ui.item.RollItemUi
import sgtmelon.scriptum.cleanup.ui.part.panel.NotePanel
import sgtmelon.scriptum.cleanup.ui.part.panel.RollEnterPanel
import sgtmelon.scriptum.cleanup.ui.part.toolbar.NoteToolbar
import sgtmelon.scriptum.data.noteHistory.NoteHistoryImpl
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragment
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.note.deepCopy
import sgtmelon.scriptum.infrastructure.utils.extensions.note.getCheckCount
import sgtmelon.scriptum.infrastructure.utils.extensions.note.hideChecked
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onConvert
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onItemCheck
import sgtmelon.scriptum.source.ui.basic.withSmoothProgress
import sgtmelon.scriptum.source.ui.feature.BackPress
import sgtmelon.scriptum.source.ui.feature.KeyboardClose
import sgtmelon.scriptum.source.ui.feature.ToolbarBack
import sgtmelon.scriptum.source.ui.model.key.InfoCase
import sgtmelon.scriptum.source.ui.model.key.NoteState
import sgtmelon.scriptum.source.ui.parts.ContainerPart
import sgtmelon.scriptum.source.ui.parts.info.InfoContainerPart
import sgtmelon.scriptum.source.ui.parts.recycler.RecyclerPart
import sgtmelon.scriptum.source.ui.parts.toolbar.ToolbarPart
import sgtmelon.scriptum.source.ui.screen.note.NoteScreen
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.imeOption
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.swipeItem
import sgtmelon.test.cappuccino.utils.typeText
import sgtmelon.test.cappuccino.utils.withMenuDrawable
import sgtmelon.test.cappuccino.utils.withMenuTitle
import sgtmelon.test.cappuccino.utils.withSize

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
) : ContainerPart(TestViewTag.ROLL_NOTE),
    RecyclerPart<RollItem, RollItemUi>,
    INoteScreen<RollNoteScreen, NoteItem.Roll>,
    NoteToolbar.ImeCallback,
    INoteAfterConvert<TextNoteScreen>,
    KeyboardClose,
    ToolbarBack,
    BackPress {

    //region Views

    private val visibleMenuItem = getView(R.id.item_visible)

    override val recyclerView = getView(R.id.recycler_view)

    private fun getInfoPart(): InfoContainerPart {
        val list = when (state) {
            NoteState.READ, NoteState.BIN -> item.list
            NoteState.EDIT, NoteState.NEW -> shadowItem.list
        }

        val isListEmpty = list.isEmpty()
        val isListHide = !item.isVisible && list.hideChecked().isEmpty()

        return InfoContainerPart(parentContainer, InfoCase.Roll(isListEmpty, isListHide))
    }

    private val doneProgress = getView(R.id.done_progress)

    override fun getItem(p: Int) = RollItemUi(recyclerView, p, state)

    /**
     * Cause of [RollEnterPanel.enterText] is local variable, need return
     * singleton in [enterPanel].
     */
    private var enterPanel: RollEnterPanel<RollNoteScreen>? = null

    override val toolbar: ToolbarPart get() = toolbar()

    fun toolbar(
        func: NoteToolbar<RollNoteScreen, NoteItem.Roll>.() -> Unit = {}
    ): NoteToolbar<*, *> {
        return NoteToolbar(func, parentContainer, callback = this, imeCallback = this)
    }

    fun enterPanel(func: RollEnterPanel<RollNoteScreen>.() -> Unit) = apply {
        enterPanel?.apply(func) ?: RollEnterPanel(func, parentContainer, callback = this).also {
            enterPanel = it
        }
    }

    fun controlPanel(func: NotePanel<RollNoteScreen, NoteItem.Roll>.() -> Unit) = apply {
        NotePanel(parentContainer, func, callback = this)
    }

    //endregion

    override var shadowItem: NoteItem.Roll = item.deepCopy()

    override val history = NoteHistoryImpl()

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

        recyclerView.swipeItem(p)
        await(SWIPE_TIME)

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
        NoteScreen().openText(func, NoteState.READ, item.onConvert(), isRankEmpty)
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

    fun onAssertAll() {
        val list = when (state) {
            NoteState.READ, NoteState.BIN -> item.list
            NoteState.EDIT, NoteState.NEW -> shadowItem.list
        }
        val resultList = if (item.isVisible) list else list.hideChecked()

        for ((i, it) in resultList.withIndex()) {
            getItem(i).assert(it)
        }
    }

    fun assert() {
        parentContainer.isDisplayed()

        val isEmpty = when (state) {
            NoteState.READ, NoteState.BIN -> item.list
            NoteState.EDIT, NoteState.NEW -> shadowItem.list
        }.let {
            if (item.isVisible) it.isEmpty() else it.hideChecked().isEmpty()
        }

        getInfoPart().assert(isEmpty)
        recyclerView.isDisplayed(!isEmpty)

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

            toolbar
                .withMenuDrawable(R.id.item_visible, itemIcon, itemTint)
                .withMenuTitle(R.id.item_visible, itemTitle)
        }

        doneProgress.isDisplayed(value = state == NoteState.READ || state == NoteState.BIN) {
            withSize(heightId = R.dimen.layout_4dp)
            withSmoothProgress(item.list.getCheckCount(), item.list.size)
        }
    }

    //endregion

    /**
     * @Test - duplicate of original function in [RollNoteViewModelImpl].
     */
    private fun getCorrectPosition(p: Int, list: List<RollItem>): Int {
        return if (item.isVisible) p else list.indexOf(list.hideChecked()[p])
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