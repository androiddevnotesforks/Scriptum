package sgtmelon.scriptum.ui.widget

import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

class NotePanel : ParentUi() {

    // TODO Доступ через Text/Roll Note

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun onClickSave() = action { onClick(R.id.note_panel_save_button) }

    fun onClickEdit() = action { onClick(R.id.note_panel_edit_button) }

    companion object {
        operator fun invoke(func: NotePanel.() -> Unit) = NotePanel().apply { func() }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(state: State, type: NoteType) {
            onDisplay(R.id.note_panel_container)

            when (state) {
                State.READ -> {
                    onDisplay(R.id.note_panel_read_container)

                    if (type == NoteType.ROLL) onDisplay(R.id.note_panel_check_button)

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

}