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

    fun isSelected(page: PAGE) = isSelected(when (page) {
        PAGE.RANK -> R.id.page_rank_item
        PAGE.NOTES -> R.id.page_notes_item
        PAGE.BIN -> R.id.page_bin_item
    })

}