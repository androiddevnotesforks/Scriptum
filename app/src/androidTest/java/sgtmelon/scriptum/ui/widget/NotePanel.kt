package sgtmelon.scriptum.ui.widget

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Часть UI абстракции для [TextNoteScreen] и [RollNoteScreen]
 *
 * @author SerjantArbuz
 */
class NotePanel(private val callback: INoteScreen) : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    /**
     * Будет возврат на экран [BinScreen]
     */
    fun onClickRestore() = callback.throwOnWrongState(State.BIN) {
        action { onClick(R.id.note_panel_restore_button) }
    }

    fun onClickRestoreOpen() = callback.throwOnWrongState(State.BIN) {
        action { onClick(R.id.note_panel_restore_open_button) }
        callback.state = State.READ
    }

    /**
     * Будет возврат на экран [BinScreen]
     */
    fun onClickClear() = callback.throwOnWrongState(State.BIN) {
        action { onClick(R.id.note_panel_clear_button) }
    }

    fun onClickUndo() = callback.throwOnWrongState(State.EDIT) {
        action { onClick(R.id.note_panel_undo_button) }
    }

    fun onClickRedo() = callback.throwOnWrongState(State.EDIT) {
        action { onClick(R.id.note_panel_redo_button) }
    }

    fun onClickRank() = callback.throwOnWrongState(State.EDIT) {
        action { onClick(R.id.note_panel_rank_button) }
    }

    fun onClickColor() = callback.throwOnWrongState(State.EDIT) {
        action { onClick(R.id.note_panel_color_button) }
    }

    fun onClickSave() = callback.throwOnWrongState(State.EDIT) {
        action { onClick(R.id.note_panel_save_button) }
    }

    fun onClickBind() = callback.throwOnWrongState(State.READ, State.NEW) {
        action { onClick(R.id.note_panel_bind_button) }
    }

    fun onClickConvert() = callback.throwOnWrongState(State.READ, State.NEW) {
        action { onClick(R.id.note_panel_convert_button) }
    }

    fun onClickDelete() = callback.throwOnWrongState(State.READ, State.NEW) {
        action { onClick(R.id.note_panel_delete_button) }
    }

    fun onClickEdit() = callback.throwOnWrongState(State.READ, State.NEW) {
        action { onClick(R.id.note_panel_edit_button) }
    }

    companion object {
        operator fun invoke(func: NotePanel.() -> Unit, callback: INoteScreen) = NotePanel(callback).apply(func)
    }

    class Assert() : BasicMatch() {

        fun isEnabledUndo(enabled: Boolean) = isEnabled(R.id.note_panel_undo_button, enabled)
        fun isEnabledRedo(enabled: Boolean) = isEnabled(R.id.note_panel_redo_button, enabled)
        fun isEnabledRank(enabled: Boolean) = isEnabled(R.id.note_panel_rank_button, enabled)
        fun isEnabledSave(enabled: Boolean) = isEnabled(R.id.note_panel_save_button, enabled)

        fun onDisplayContent(state: State) {
            onDisplay(R.id.note_panel_container)

            when (state) {
                State.READ -> {
                    onDisplay(R.id.note_panel_read_container)

                    onDisplay(R.id.note_panel_notification)
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