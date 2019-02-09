package sgtmelon.scriptum.ui.widget.note.panel

import sgtmelon.scriptum.ui.ParentUi

class RollEnterPanelUi : ParentUi() {

    // TODO Доступ через Text/Roll Note

    companion object {
        operator fun invoke(func: RollEnterPanelUi.() -> Unit) = RollEnterPanelUi().apply { func() }
    }

    fun assert(func: RollEnterPanelAssert.() -> Unit) = RollEnterPanelAssert().apply { func() }

}