package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi

class MainScreen : ParentUi() {

    companion object {
        operator fun invoke(func: MainScreen.() -> Unit) = MainScreen().apply { func() }
    }

    fun assert(func: MainAssert.() -> Unit) = MainAssert().apply { func() }

    fun onClickAdd() = action { onClick(R.id.add_fab) }

    fun navigateTo(page: Page) = action {
        onClick(when (page) {
            Page.RANK -> R.id.item_page_rank
            Page.NOTES -> R.id.item_page_notes
            Page.BIN -> R.id.item_page_bin
        })
    }

}