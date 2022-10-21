package sgtmelon.scriptum.cleanup.ui.screen.main

import android.view.ViewGroup.LayoutParams
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.waitAfter
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.scriptum.cleanup.ui.dialog.sheet.AddSheetDialogUi
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.isSelected
import sgtmelon.test.cappuccino.utils.longClick
import sgtmelon.test.cappuccino.utils.withBackgroundAttr
import sgtmelon.test.cappuccino.utils.withCardElevation
import sgtmelon.test.cappuccino.utils.withCardRadius
import sgtmelon.test.cappuccino.utils.withContentDescription
import sgtmelon.test.cappuccino.utils.withDrawableAttr
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withSizeAttr
import sgtmelon.test.cappuccino.utils.withSizeCode

/**
 * Class for UI control of [MainActivity].
 */
class MainScreen : ParentUi() {

    //region Views

    private val parentContainer = getViewById(R.id.parent_container)
    private val toolbarHolder = getViewById(R.id.toolbar_holder)
    private val dividerView = getViewById(R.id.divider_view)
    private val menuNavigation = getViewById(R.id.menu_navigation)

    private val rankMenuItem = getViewById(R.id.item_page_rank)
    private val notesMenuItem = getViewById(R.id.item_page_notes)
    private val binMenuItem = getViewById(R.id.item_page_bin)

    private val fabCard = getViewById(R.id.gradient_fab_card)
    private val fabClick = getViewById(R.id.gradient_fab_click)
    private val fabIcon = getViewById(R.id.gradient_fab_icon)

    //endregion

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
        fabClick.click()
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
            fabCard.isDisplayed(isFabVisible) {
                withSize(R.dimen.gradient_fab_size, R.dimen.gradient_fab_size)
                withCardRadius(R.dimen.gradient_fab_radius)
                withCardElevation(R.dimen.gradient_fab_elevation)

                fabClick.isDisplayed()
                    .isEnabled()
                    .withSizeCode(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

                fabIcon.isDisplayed()
                    .withSize(R.dimen.gradient_fab_image_size, R.dimen.gradient_fab_image_size)
                    .withDrawableAttr(R.drawable.ic_add, R.attr.clBackgroundView)
                    .withContentDescription(R.string.description_add_note)
            }.isEnabled(isFabVisible)
        }
    }

    companion object {
        private const val SCROLL_TIME = 500L

        operator fun invoke(func: MainScreen.() -> Unit): MainScreen {
            return MainScreen().assert(MainPage.NOTES, isFabVisible = true).apply(func)
        }
    }
}