package sgtmelon.scriptum.ui.screen.main.rank

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentRecyclerScreen

class RankScreen : ParentRecyclerScreen(R.id.rank_recycler) {

    companion object {
        operator fun invoke(func: RankScreen.() -> Unit) = RankScreen().apply { func() }
    }

    fun assert(func: RankAssert.() -> Unit) = RankAssert().apply { func() }

    fun onLongClickItem(position: Int = positionRandom) =
            action { onLongClick(recyclerId, position) }

}