package sgtmelon.scriptum.ui.widget.note.panel

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi

class NotePanelUi : ParentUi() {

    // TODO Доступ через Text/Roll Note

    fun assert(func: NotePanelAssert.() -> Unit) = NotePanelAssert().apply { func() }

    fun onClickSave() = action { onClick(R.id.note_panel_save_button) }

    fun onClickEdit() = action { onClick(R.id.note_panel_edit_button) }

    companion object {
        operator fun invoke(func: NotePanelUi.() -> Unit) = NotePanelUi().apply { func() }
    }

}