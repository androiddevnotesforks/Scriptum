package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.dialog.AddDialogUi

class MainScreen : ParentUi() {

    companion object {
        operator fun invoke(func: MainScreen.() -> Unit) = MainScreen().apply { func() }
    }

    fun assert(func: MainAssert.() -> Unit) = MainAssert().apply { func() }
    fun addDialog(func: AddDialogUi.() -> Unit) = AddDialogUi().apply { func() }

    fun navigateTo(page: PAGE) = action {
        onClick(when (page) {
            PAGE.RANK -> R.id.item_page_rank
            PAGE.NOTES -> R.id.item_page_notes
            PAGE.BIN -> R.id.item_page_bin
        })
    }

}