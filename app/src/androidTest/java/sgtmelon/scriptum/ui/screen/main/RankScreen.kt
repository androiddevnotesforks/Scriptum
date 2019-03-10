package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.basic.BasicMatch

class RankScreen : ParentRecyclerScreen(R.id.rank_recycler) {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun onLongClickItem(position: Int = positionRandom) =
            action { onLongClick(recyclerId, position) }

    companion object {
        operator fun invoke(func: RankScreen.() -> Unit) = RankScreen().apply { func() }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(empty: Boolean) {
            onDisplay(R.id.rank_parent_container)

            onDisplayToolbar()

            if (empty) {
                onDisplay(R.id.info_title_text, R.string.info_rank_title)
                onDisplay(R.id.info_details_text, R.string.info_rank_details)
                doesNotDisplay(R.id.rank_recycler)
            } else {
                doesNotDisplay(R.id.info_title_text, R.string.info_rank_title)
                doesNotDisplay(R.id.info_details_text, R.string.info_notes_details)
                onDisplay(R.id.rank_recycler)
            }
        }

        private fun onDisplayToolbar() {
            onDisplay(R.id.toolbar_rank_container)

            onDisplay(R.id.toolbar_rank_cancel_button)
            onDisplay(R.id.toolbar_rank_enter)
            onDisplay(R.id.toolbar_rank_add_button)
        }

    }

}