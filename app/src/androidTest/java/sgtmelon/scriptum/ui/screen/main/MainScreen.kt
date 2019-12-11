package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.dialog.AddDialogUi

/**
 * Class for UI control of [MainActivity].
 */
class MainScreen : ParentUi() {

    //region Views

    private val parentContainer = getViewById(R.id.main_parent_container)
    private val toolbarHolder = getViewById(R.id.main_toolbar_holder)
    private val menuNavigation = getViewById(R.id.main_menu_navigation)

    private val rankMenuItem = getViewById(R.id.item_page_rank)
    private val notesMenuItem = getViewById(R.id.item_page_notes)
    private val binMenuItem = getViewById(R.id.item_page_bin)

    private val addFab = getViewById(R.id.main_add_fab)

    //endregion

    private var wasNavigate = false
    private var page = MainPage.NOTES

    fun openPage(page: MainPage, empty: Boolean = false) = when(page) {
        MainPage.RANK -> rankScreen(empty)
        MainPage.NOTES -> notesScreen(empty)
        MainPage.BIN -> binScreen(empty)
    }

    fun rankScreen(empty: Boolean = false, func: RankScreen.() -> Unit = {}) = apply {
        wasNavigate = true
        onNavigateTo(MainPage.RANK)

        RankScreen.invoke(func, empty)
    }

    fun notesScreen(empty: Boolean = false, hide: Boolean = false,
                    func: NotesScreen.() -> Unit = {}) = apply {
        if (wasNavigate) onNavigateTo(MainPage.NOTES)

        NotesScreen.invoke(func, empty, hide)
    }

    fun binScreen(empty: Boolean = false, func: BinScreen.() -> Unit = {}) = apply {
        wasNavigate = true
        onNavigateTo(MainPage.BIN)

        BinScreen.invoke(func, empty)
    }

    fun addDialog(func: AddDialogUi.() -> Unit = {}) = apply {
        addFab.click()
        AddDialogUi.invoke(func)
    }

    fun onNavigateTo(page: MainPage) {
        this.page = page

        when (page) {
            MainPage.RANK -> rankMenuItem.click()
            MainPage.NOTES -> notesMenuItem.click()
            MainPage.BIN -> binMenuItem.click()
        }

        assert(page, fabVisible = page == MainPage.NOTES)
    }

    fun onScrollTop() = waitAfter(SCROLL_TIME) {
        when (page) {
            MainPage.RANK -> rankMenuItem.longClick()
            MainPage.NOTES -> notesMenuItem.longClick()
            MainPage.BIN -> binMenuItem.longClick()
        }
    }


    fun assert(page: MainPage? = null, fabVisible: Boolean? = null) = apply {
        parentContainer.isDisplayed()

        /**
         * TODO #TEST height attr assert
         */
        toolbarHolder.isDisplayed().withBackgroundAttr(R.attr.clPrimary)
        menuNavigation.isDisplayed().withBackgroundAttr(R.attr.clPrimary)

        when (page) {
            MainPage.RANK -> rankMenuItem.isSelected()
            MainPage.NOTES -> notesMenuItem.isSelected()
            MainPage.BIN -> binMenuItem.isSelected()
        }

        if (fabVisible != null) addFab.isDisplayed(fabVisible)
    }

    companion object {
        private const val SCROLL_TIME = 500L

        operator fun invoke(func: MainScreen.() -> Unit) : MainScreen {
            return MainScreen().assert(MainPage.NOTES, fabVisible = true).apply(func)
        }
    }

}