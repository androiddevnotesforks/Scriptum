package sgtmelon.scriptum.ui.widget

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

class NotePanel(private val type: NoteType) : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert(type).apply { func() }

    fun onClickRestore() = action { onClick(R.id.note_panel_restore_button) }
    fun onClickRestoreOpen() = action { onClick(R.id.note_panel_restore_open_button) }
    fun onClickClear() = action { onClick(R.id.note_panel_clear_button) }

    fun onClickUndo() = action { onClick(R.id.note_panel_undo_button) }
    fun onClickRedo() = action { onClick(R.id.note_panel_redo_button) }
    fun onClickRank() = action { onClick(R.id.note_panel_rank_button) }
    fun onClickColor() = action { onClick(R.id.note_panel_color_button) }
    fun onClickSave() = action { onClick(R.id.note_panel_save_button) }

    fun onClickCheck() = action { onClick(R.id.note_panel_check_button) }
    fun onClickBind() = action { onClick(R.id.note_panel_bind_button) }
    fun onClickConvert() = action { onClick(R.id.note_panel_convert_button) }
    fun onClickDelete() = action { onClick(R.id.note_panel_delete_button) }
    fun onClickEdit() = action { onClick(R.id.note_panel_edit_button) }

    class Assert(private val type: NoteType) : BasicMatch() {

        fun isEnabledUndo(enabled : Boolean) = isEnabled(R.id.note_panel_undo_button, enabled)
        fun isEnabledRedo(enabled : Boolean) = isEnabled(R.id.note_panel_redo_button, enabled)
        fun isEnabledRank(enabled : Boolean) = isEnabled(R.id.note_panel_rank_button, enabled)
        fun isEnabledSave(enabled : Boolean) = isEnabled(R.id.note_panel_save_button, enabled)

        fun onDisplayContent(state: State) {
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