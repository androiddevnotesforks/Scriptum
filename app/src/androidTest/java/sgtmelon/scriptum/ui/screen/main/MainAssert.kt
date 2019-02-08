package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch

class MainAssert : BasicMatch() {

    fun onDisplayContent(page: PAGE) {
        onDisplay(R.id.menu_navigation)

        when(page) {
            PAGE.NOTES -> onDisplay(R.id.add_fab)
            else -> doesNotDisplay(R.id.add_fab)
        }
    }

    fun isSelected(page: PAGE) = isSelected(when (page) {
        PAGE.RANK -> R.id.item_page_rank
        PAGE.NOTES -> R.id.item_page_notes
        PAGE.BIN -> R.id.item_page_bin
    })

}