package sgtmelon.scriptum.parent.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.parent.ui.parts.ContainerPart
import sgtmelon.scriptum.parent.ui.screen.dialogs.sheet.AddSheetDialogUi
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isSelected
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

    var currentPage = MainPage.NOTES
        private set

    /**
     * Function for "just select page".
     */
    fun openPage(page: MainPage, isEmpty: Boolean = false) = when (page) {
        MainPage.RANK -> openRank(isEmpty)
        MainPage.NOTES -> openNotes(isEmpty)
        MainPage.BIN -> openBin(isEmpty)
    }

    inline fun openRank(isEmpty: Boolean = false, func: RankScreen.() -> Unit = {}) = apply {
        if (currentPage != MainPage.RANK) {
            clickPage(MainPage.RANK)
        }

        RankScreen(func, isEmpty)
    }

    inline fun openNotes(
        isEmpty: Boolean = false,
        isHide: Boolean = false,
        func: NotesScreen.() -> Unit = {}
    ) = apply {
        if (currentPage != MainPage.NOTES) {
            clickPage(MainPage.NOTES)
        }

        NotesScreen(func, isEmpty, isHide)
    }

    inline fun openBin(isEmpty: Boolean = false, func: BinScreen.() -> Unit = {}) = apply {
        if (currentPage != MainPage.BIN) {
            clickPage(MainPage.BIN)
        }

        BinScreen(func, isEmpty)
    }

    fun clickPage(page: MainPage) {
        this.currentPage = page

        when (page) {
            MainPage.RANK -> rankMenuItem.click()
            MainPage.NOTES -> notesMenuItem.click()
            MainPage.BIN -> binMenuItem.click()
        }

        assert(page, isFabVisible = page == MainPage.NOTES)
    }

    fun scrollTop() {
        when (currentPage) {
            MainPage.RANK -> rankMenuItem.click()
            MainPage.NOTES -> notesMenuItem.click()
            MainPage.BIN -> binMenuItem.click()
        }

        await(SCROLL_TIME)
    }

    fun openAddDialog(func: AddSheetDialogUi.() -> Unit = {}) = apply { fab.click(func) }

    /**
     * [page] and [isFabVisible] equals NULL if you want skip assert of related elements.
     */
    fun assert(page: MainPage = currentPage, isFabVisible: Boolean? = null) = apply {
        parentContainer.isDisplayed()

        toolbarHolder.withBackgroundAttr(R.attr.clPrimary)
            .withSizeAttr(heightAttr = android.R.attr.actionBarSize)

        dividerView.isDisplayed()
            .withSize(heightId = R.dimen.layout_1dp)
            .withBackgroundAttr(R.attr.clDivider)

        menuNavigation.isDisplayed().withBackgroundAttr(R.attr.clPrimary)

        rankMenuItem.isSelected(value = MainPage.RANK == page)
        notesMenuItem.isSelected(value = MainPage.NOTES == page)
        binMenuItem.isSelected(value = MainPage.BIN == page)

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