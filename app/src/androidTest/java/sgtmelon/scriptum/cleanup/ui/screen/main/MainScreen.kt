package sgtmelon.scriptum.cleanup.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.waitAfter
import sgtmelon.scriptum.cleanup.ui.dialog.sheet.AddSheetDialogUi
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.ui.testing.parent.screen.ContainerPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isSelected
import sgtmelon.test.cappuccino.utils.longClick
import sgtmelon.test.cappuccino.utils.withBackgroundAttr
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withSizeAttr

/**
 * Class for UI control of [MainActivity].
 */
class MainScreen : ContainerPart(TestViewTag.MAIN) {

    //region Views

    private val toolbarHolder = getView(R.id.toolbar_holder)
    private val dividerView = getView(R.id.divider_view)
    private val menuNavigation = getView(R.id.menu_navigation)

    private val rankMenuItem = getView(R.id.item_page_rank)
    private val notesMenuItem = getView(R.id.item_page_notes)
    private val binMenuItem = getView(R.id.item_page_bin)

    private val fab = MainFabPart()

    //endregion

    @Deprecated("May be somehow without it?")
    private var wasNavigate = false

    private var page = MainPage.NOTES

    fun openPage(page: MainPage, isEmpty: Boolean = false) = when (page) {
        MainPage.RANK -> rankScreen(isEmpty)
        MainPage.NOTES -> notesScreen(isEmpty)
        MainPage.BIN -> binScreen(isEmpty)
    }

    fun rankScreen(isEmpty: Boolean = false, func: RankScreen.() -> Unit = {}) = apply {
        wasNavigate = true
        onNavigateTo(MainPage.RANK)

        RankScreen(func, isEmpty)
    }

    fun notesScreen(
        isEmpty: Boolean = false,
        isHide: Boolean = false,
        func: NotesScreen.() -> Unit = {}
    ) = apply {
        if (wasNavigate) onNavigateTo(MainPage.NOTES)

        NotesScreen(func, isEmpty, isHide)
    }

    fun binScreen(isEmpty: Boolean = false, func: BinScreen.() -> Unit = {}) = apply {
        wasNavigate = true
        onNavigateTo(MainPage.BIN)

        BinScreen(func, isEmpty)
    }

    fun openAddDialog(func: AddSheetDialogUi.() -> Unit = {}) = apply {
        fab.click()
        AddSheetDialogUi(func)
    }

    fun onNavigateTo(page: MainPage) {
        this.page = page

        when (page) {
            MainPage.RANK -> rankMenuItem.click()
            MainPage.NOTES -> notesMenuItem.click()
            MainPage.BIN -> binMenuItem.click()
        }

        assert(page, isFabVisible = page == MainPage.NOTES)
    }

    fun onScrollTop() = waitAfter(SCROLL_TIME) {
        when (page) {
            MainPage.RANK -> rankMenuItem.longClick()
            MainPage.NOTES -> notesMenuItem.longClick()
            MainPage.BIN -> binMenuItem.longClick()
        }
    }

    // TODO when isFabVisible == null?
    fun assert(page: MainPage? = null, isFabVisible: Boolean? = null) = apply {
        parentContainer.isDisplayed()

        toolbarHolder.withBackgroundAttr(R.attr.clPrimary)
            .withSizeAttr(heightAttr = android.R.attr.actionBarSize)

        dividerView.isDisplayed()
            .withSize(heightId = R.dimen.layout_1dp)
            .withBackgroundAttr(R.attr.clDivider)

        menuNavigation.isDisplayed().withBackgroundAttr(R.attr.clPrimary)

        if (page != null) when (page) {
            MainPage.RANK -> rankMenuItem.isSelected()
            MainPage.NOTES -> notesMenuItem.isSelected()
            MainPage.BIN -> binMenuItem.isSelected()
        }

        if (isFabVisible != null) {
            fab.assert(isFabVisible)
        }
    }

    companion object {
        private const val SCROLL_TIME = 500L

        inline operator fun invoke(func: MainScreen.() -> Unit): MainScreen {
            return MainScreen().assert(MainPage.NOTES, isFabVisible = true).apply(func)
        }
    }
}