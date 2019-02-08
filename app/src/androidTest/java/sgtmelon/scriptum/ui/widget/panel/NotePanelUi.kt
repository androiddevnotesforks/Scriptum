package sgtmelon.scriptum.ui.widget.panel

import sgtmelon.scriptum.ui.ParentUi

class NotePanelUi : ParentUi() {

    // TODO Доступ через Text/Roll Note

    companion object {
        operator fun invoke(func: NotePanelUi.() -> Unit) = NotePanelUi().apply { func() }
    }

    fun assert(func: NotePanelAssert.() -> Unit) = NotePanelAssert().apply { func() }

}