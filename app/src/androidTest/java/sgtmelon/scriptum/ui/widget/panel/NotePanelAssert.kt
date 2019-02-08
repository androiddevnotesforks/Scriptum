package sgtmelon.scriptum.ui.widget.panel

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch

class NotePanelAssert : BasicMatch() {

    fun onDisplayContent(state: STATE) {
        onDisplay(R.id.note_panel_container)

        when(state) {
            STATE.BIN -> {
                onDisplay(R.id.note_panel_bin_container)

                onDisplay(R.id.note_panel_restore_button)
                onDisplay(R.id.note_panel_restore_open_button)
                onDisplay(R.id.note_panel_clear_button)
            }
            STATE.EDIT -> {
                onDisplay(R.id.note_panel_edit_container)

                onDisplay(R.id.note_panel_undo_button)
                onDisplay(R.id.note_panel_redo_button)
                onDisplay(R.id.note_panel_rank_button)
                onDisplay(R.id.note_panel_color_button)

                onDisplay(R.id.note_panel_save_button)
            }
            STATE.READ -> {
                onDisplay(R.id.note_panel_read_container)

                onDisplay(R.id.note_panel_check_button)
                onDisplay(R.id.note_panel_bind_button)
                onDisplay(R.id.note_panel_convert_button)
                onDisplay(R.id.note_panel_delete_button)

                onDisplay(R.id.note_panel_edit_button)
            }
        }
    }

}