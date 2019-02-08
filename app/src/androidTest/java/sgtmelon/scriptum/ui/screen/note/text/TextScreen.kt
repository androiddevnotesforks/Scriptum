package sgtmelon.scriptum.ui.screen.note.text

import sgtmelon.scriptum.ui.ParentUi

class TextScreen : ParentUi() {

    companion object {
        operator fun invoke(func: TextScreen.() -> Unit) = TextScreen().apply { func() }
    }

    fun assert(func: TextAssert.() -> Unit) = TextAssert().apply { func() }

}