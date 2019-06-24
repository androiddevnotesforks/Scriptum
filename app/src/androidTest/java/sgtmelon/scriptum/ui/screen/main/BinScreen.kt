package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.view.main.BinFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.dialog.ClearDialogUi
import sgtmelon.scriptum.ui.dialog.NoteDialogUi
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Класс для ui контроля экрана [BinFragment]
 *
 * @author SerjantArbuz
 */
class BinScreen : ParentRecyclerScreen(R.id.bin_recycler) {

    fun assert(empty: Boolean) = Assert(empty)

    fun openClearDialog(func: ClearDialogUi.() -> Unit = {}) {
        action { onClick(R.id.item_clear) }
        ClearDialogUi.invoke(func)
    }

    fun openNoteDialog(noteModel: NoteModel, p: Int = positionRandom,
                       func: NoteDialogUi.() -> Unit = {}) {
        action { onLongClick(recyclerId, p) }
        NoteDialogUi.invoke(func, noteModel)
    }

    fun openTextNote(noteModel: NoteModel, p: Int = positionRandom,
                     func: TextNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        TextNoteScreen.invoke(func, State.BIN, noteModel)
    }

    fun openRollNote(noteModel: NoteModel, p: Int = positionRandom,
                     func: RollNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        RollNoteScreen.invoke(func, State.BIN, noteModel)
    }

    companion object {
        operator fun invoke(func: BinScreen.() -> Unit, empty: Boolean) = BinScreen().apply {
            assert(empty)
            func()
        }
    }

    class Assert(empty: Boolean) : BasicMatch() {

        init {
            onDisplay(R.id.bin_parent_container)

            onDisplayToolbar(R.id.toolbar_container, R.string.title_bin)

            if (empty) {
                onDisplay(R.id.info_title_text, R.string.info_bin_title)
                onDisplay(R.id.info_details_text, R.string.info_bin_details)
                notDisplay(R.id.bin_recycler)
            } else {
                onDisplay(R.id.item_clear)

                notDisplay(R.id.info_title_text, R.string.info_bin_title)
                notDisplay(R.id.info_details_text, R.string.info_bin_details)
                onDisplay(R.id.bin_recycler)
            }
        }

    }

}