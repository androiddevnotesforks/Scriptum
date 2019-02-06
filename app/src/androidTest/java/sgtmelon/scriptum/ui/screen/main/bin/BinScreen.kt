package sgtmelon.scriptum.ui.screen.main.bin

import sgtmelon.scriptum.basic.BasicMatch

class BinScreen : BasicMatch() {

    companion object {
        operator fun invoke(func: BinScreen.() -> Unit) = BinScreen().apply { func() }
    }

    fun assert(func: BinAssert.() -> Unit) = BinAssert().apply { func() }

}