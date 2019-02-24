package sgtmelon.scriptum.ui.screen.main.bin

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.basic.BasicMatch

class BinAssert : BasicMatch() {

    fun onDisplayContent(count: Int) {
        onDisplay(R.id.bin_parent_container)

        onDisplayToolbar(R.id.toolbar_container, R.string.title_bin)

        if (count == 0) {
            onDisplay(R.id.info_title_text, R.string.info_bin_title)
            onDisplay(R.id.info_details_text, R.string.info_bin_details)
            doesNotDisplay(R.id.bin_recycler)
        } else {
            onDisplay(R.id.item_clear)

            doesNotDisplay(R.id.info_title_text, R.string.info_bin_title)
            doesNotDisplay(R.id.info_details_text, R.string.info_bin_details)
            onDisplay(R.id.bin_recycler)
        }
    }

}