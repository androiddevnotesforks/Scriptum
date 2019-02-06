package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.screen.ParentScreen

class MainScreen : ParentScreen() {

    companion object {
        operator fun invoke(func: MainScreen.() -> Unit) = MainScreen().apply { func() }
    }

    fun assert(func: MainAssert.() -> Unit) = MainAssert().apply { func() }

    fun navigateTo(page: PAGE) = action {
        onClick(when (page) {
            PAGE.RANK -> R.id.page_rank_item
            PAGE.NOTES -> R.id.page_notes_item
            PAGE.BIN -> R.id.page_bin_item
        })
    }

}