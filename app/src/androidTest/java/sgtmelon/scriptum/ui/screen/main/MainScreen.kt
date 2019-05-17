package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.view.main.MainActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.dialog.AddDialogUi

/**
 * Класс для ui контроля экрана [MainActivity]
 *
 * @author SerjantArbuz
 */
class MainScreen : ParentUi() {

    private var wasNavigate = false

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun openRankPage(func: RankScreen.() -> Unit) {
        wasNavigate = true
        onNavigateTo(MainPage.RANK)

        RankScreen().apply(func)
    }

    fun openNotesPage(func: NotesScreen.() -> Unit)  {
        if (wasNavigate) onNavigateTo(MainPage.NOTES)

        NotesScreen().apply(func)
    }

    fun openBinPage(func: BinScreen.() -> Unit)  {
        wasNavigate = true
        onNavigateTo(MainPage.BIN)

        BinScreen().apply(func)
    }

    fun openAddDialog(func: AddDialogUi.() -> Unit = {}) = AddDialogUi().apply {
        action { onClick(R.id.main_add_fab) }
        assert { onDisplayContent() }
        func()
    }

    fun onNavigateTo(page: MainPage) = action {
        onClick(when (page) {
            MainPage.RANK -> R.id.item_page_rank
            MainPage.NOTES -> R.id.item_page_notes
            MainPage.BIN -> R.id.item_page_bin
        })
    }

    fun onScrollTop(page: MainPage) = action {
        waitAfter(time = 500) {
            onLongClick(when (page) {
                MainPage.RANK -> R.id.item_page_rank
                MainPage.NOTES -> R.id.item_page_notes
                MainPage.BIN -> R.id.item_page_bin
            })
        }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent() {
            onDisplay(R.id.main_parent_container)
            onDisplay(R.id.main_toolbar_holder)
            onDisplay(R.id.main_menu_navigation)
        }

        fun onDisplayContent(page: MainPage) = when (page) {
            MainPage.RANK -> isSelected(R.id.item_page_rank)
            MainPage.NOTES -> isSelected(R.id.item_page_notes)
            MainPage.BIN -> isSelected(R.id.item_page_bin)
        }

        fun onDisplayFab(visible: Boolean) =
                if (visible) onDisplay(R.id.main_add_fab) else notDisplay(R.id.main_add_fab)

    }

    companion object {
        operator fun invoke(func: MainScreen.() -> Unit) = MainScreen().apply { func() }
    }

}