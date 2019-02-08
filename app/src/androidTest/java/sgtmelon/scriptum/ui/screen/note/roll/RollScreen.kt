package sgtmelon.scriptum.ui.screen.note.roll

import sgtmelon.scriptum.ui.ParentUi

class RollScreen : ParentUi() {

    companion object {
        operator fun invoke(func: RollScreen.() -> Unit) = RollScreen().apply { func() }
    }

    fun assert(func: RollAssert.() -> Unit) = RollAssert().apply { func() }

}