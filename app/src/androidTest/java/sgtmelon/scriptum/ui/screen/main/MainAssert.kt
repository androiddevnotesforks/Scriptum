package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch

class MainAssert : BasicMatch() {

    fun onDisplayContent(showFab: Boolean) {
        onDisplay(R.id.menu_navigation)

        when(showFab) {
            true -> onDisplay(R.id.add_fab)
            false -> doesNotDisplay(R.id.add_fab)
        }
    }

    fun isSelected(page: Page) = isSelected(when (page) {
        Page.RANK -> R.id.item_page_rank
        Page.NOTES -> R.id.item_page_notes
        Page.BIN -> R.id.item_page_bin
    })

}