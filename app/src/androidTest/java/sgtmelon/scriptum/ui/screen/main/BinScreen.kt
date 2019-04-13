package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.screen.view.main.BinFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.dialog.ClearDialog
import sgtmelon.scriptum.ui.dialog.NoteDialog

/**
 * Класс для ui контроля экрана [BinFragment]
 *
 * @author SerjantArbuz
 */
class BinScreen : ParentRecyclerScreen(R.id.bin_recycler) {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun clearDialog(func: ClearDialog.() -> Unit) = ClearDialog().apply { func() }
    fun noteDialog(func: NoteDialog.() -> Unit) = NoteDialog().apply { func() }

    fun onClickClearBin() = action { onClick(R.id.item_clear) }

    fun onLongClickItem(position: Int = positionRandom) =
            action { onLongClick(recyclerId, position) }

    class Assert : BasicMatch() {

        fun onDisplayContent(empty: Boolean) {
            onDisplay(R.id.bin_parent_container)

            onDisplayToolbar(R.id.toolbar_container, R.string.title_bin)

            if (empty) {
                onDisplay(R.id.info_title_text, R.string.info_bin_title)
                onDisplay(R.id.info_details_text, R.string.info_bin_details)
                notDisplay(R.id.bin_recycler)
            } else {
                onDisplay(R.id.item_clear)

                notDisplay(R.id.info_title_text, R.string.info_bin_title)
                notDisplay(R.id.info_details_text, R.string.info_bin_details)
                onDisplay(R.id.bin_recycler)
            }
        }

    }

}