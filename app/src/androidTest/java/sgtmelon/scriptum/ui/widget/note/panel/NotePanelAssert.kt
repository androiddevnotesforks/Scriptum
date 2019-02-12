package sgtmelon.scriptum.ui.widget.note.panel

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.ui.widget.note.State

class NotePanelAssert : BasicMatch() {

    fun onDisplayContent(state: State, type: NoteType) {
        onDisplay(R.id.note_panel_container)

        when (state) {
            State.READ -> {
                onDisplay(R.id.note_panel_read_container)

                when (type) {
                    NoteType.TEXT -> doesNotDisplay(R.id.note_panel_check_button)
                    NoteType.ROLL -> onDisplay(R.id.note_panel_check_button)
                }

                onDisplay(R.id.note_panel_bind_button)
                onDisplay(R.id.note_panel_convert_button)
                onDisplay(R.id.note_panel_delete_button)

                onDisplay(R.id.note_panel_edit_button)
            }
            State.BIN -> {
                onDisplay(R.id.note_panel_bin_container)

                onDisplay(R.id.note_panel_restore_button)
                onDisplay(R.id.note_panel_restore_open_button)
                onDisplay(R.id.note_panel_clear_button)
            }
            State.EDIT, State.NEW -> {
                onDisplay(R.id.note_panel_edit_container)

                onDisplay(R.id.note_panel_undo_button)
                onDisplay(R.id.note_panel_redo_button)
                onDisplay(R.id.note_panel_rank_button)
                onDisplay(R.id.note_panel_color_button)

                onDisplay(R.id.note_panel_save_button)
            }
        }
    }

}