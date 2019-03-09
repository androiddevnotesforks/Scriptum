package sgtmelon.scriptum.ui.screen.main.bin

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentRecyclerScreen

class BinScreen : ParentRecyclerScreen(R.id.bin_recycler) {

    fun assert(func: BinAssert.() -> Unit) = BinAssert().apply { func() }

    fun onClickClearBin() = action { onClick(R.id.item_clear) }

    fun onLongClickItem(position: Int = positionRandom) =
            action { onLongClick(recyclerId, position) }

    companion object {
        operator fun invoke(func: BinScreen.() -> Unit) = BinScreen().apply { func() }
    }

}