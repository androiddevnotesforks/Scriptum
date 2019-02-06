package sgtmelon.scriptum.ui.screen.main.rank

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch

class RankAssert : BasicMatch() {

    fun onDisplayContent() { // TODO(Отображение списка/информации от bool)
//        onDisplayToolbar(R.id.toolbar, R.string.title_rank)
    }

    fun onDisplayList() = onDisplay(R.id.rank_recycler)

    fun doesNotDisplayList() = doesNotDisplay(R.id.rank_recycler)

    fun onDisplayInfo() {
        onDisplay(R.id.info_title_text, R.string.info_rank_title)
        onDisplay(R.id.info_details_text, R.string.info_rank_details)
    }

    fun doesNotDisplayInfo() {
        doesNotDisplay(R.id.info_title_text, R.string.info_rank_title)
        doesNotDisplay(R.id.info_details_text, R.string.info_notes_details)
    }

}