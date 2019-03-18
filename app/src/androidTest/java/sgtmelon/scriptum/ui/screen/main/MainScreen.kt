package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.key.MainPage
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

class MainScreen : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun onClickFab() =  action { onClick(R.id.main_add_fab) }

    fun navigateTo(page: MainPage.Name) = action {
        onClick(when (page) {
            MainPage.Name.RANK -> R.id.item_page_rank
            MainPage.Name.NOTES -> R.id.item_page_notes
            MainPage.Name.BIN -> R.id.item_page_bin
        })
    }

    fun scrollTop(page: MainPage.Name) = action {
        onLongClick(when (page) {
            MainPage.Name.RANK -> R.id.item_page_rank
            MainPage.Name.NOTES -> R.id.item_page_notes
            MainPage.Name.BIN -> R.id.item_page_bin
        })
    }

    companion object {
        operator fun invoke(func: MainScreen.() -> Unit) = MainScreen().apply { func() }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent() {
            onDisplay(R.id.main_parent_container)
            onDisplay(R.id.main_toolbar_holder)
            onDisplay(R.id.main_menu_navigation)
        }

        fun onDisplayContent(page: MainPage.Name) {
            when (page) {
                MainPage.Name.RANK -> {
                    isSelected(R.id.item_page_rank)
                    RankScreen { assert { onDisplayContent(empty = count == 0) } }
                }
                MainPage.Name.NOTES -> {
                    isSelected(R.id.item_page_notes)
                    NotesScreen { assert { onDisplayContent(empty = count == 0) } }
                }
                MainPage.Name.BIN -> {
                    isSelected(R.id.item_page_bin)
                    BinScreen { assert { onDisplayContent(empty = count == 0) } }
                }
            }
        }

        fun onDisplayFab(visible: Boolean) =
                if (visible) onDisplay(R.id.main_add_fab) else doesNotDisplay(R.id.main_add_fab)

    }

}