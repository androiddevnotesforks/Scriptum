package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.screen.ParentScreen

class MainScreen : ParentScreen() {

    companion object {
        operator fun invoke(func: MainScreen.() -> Unit) = MainScreen().apply { func() }
    }

    fun assert(func: MainAssert.() -> Unit) = MainAssert().apply { func() }

    fun onNavigate(page: Page) = action {
        onClick(when (page) {
            Page.RANK -> R.id.page_rank_item
            Page.NOTES -> R.id.page_notes_item
            Page.BIN -> R.id.page_bin_item
        })
    }

}