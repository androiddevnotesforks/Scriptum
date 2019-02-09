package sgtmelon.scriptum.ui.screen.main.bin

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentRecyclerScreen

class BinScreen : ParentRecyclerScreen(R.id.bin_recycler) {

    companion object {
        operator fun invoke(func: BinScreen.() -> Unit) = BinScreen().apply { func() }
    }

    fun assert(func: BinAssert.() -> Unit) = BinAssert().apply { func() }

}