package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch

class MainAssert : BasicMatch() {

    fun isSelected(page: Page) = isSelected(when (page) {
        Page.RANK -> R.id.page_rank_item
        Page.NOTES -> R.id.page_notes_item
        Page.BIN -> R.id.page_bin_item
    })

}