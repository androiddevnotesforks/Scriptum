package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.view.main.NotesFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.dialog.NoteDialogUi
import sgtmelon.scriptum.ui.screen.PreferenceScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Класс для ui контроля экрана [NotesFragment]
 *
 * @author SerjantArbuz
 */
class NotesScreen : ParentRecyclerScreen(R.id.notes_recycler) {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun preferenceScreen(func: PreferenceScreen.() -> Unit) = PreferenceScreen().apply {
        action { onClick(R.id.item_preference) }
        func()
    }

    fun noteDialogUi(noteItem: NoteItem, p: Int = 0, func: NoteDialogUi.() -> Unit = {}) = NoteDialogUi().apply {
        action { onLongClick(recyclerId, p) }
        assert { onDisplayContent(noteItem) }
        func()
    }

    fun textNoteScreen(state: State, p: Int = 0, func: TextNoteScreen.() -> Unit = {}) = TextNoteScreen().apply {
        onClickItem(p)
        assert { onDisplayContent(state) }
        func()
    }

    fun rollNoteScreen(state: State, p: Int = 0, func: RollNoteScreen.() -> Unit = {}) = RollNoteScreen().apply {
        onClickItem(p)
        assert { onDisplayContent(state) }
        func()
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(empty: Boolean) {
            onDisplay(R.id.notes_parent_container)

            onDisplayToolbar(R.id.toolbar_container, R.string.title_notes)
            onDisplay(R.id.item_preference)

            if (empty) {
                onDisplay(R.id.info_title_text, R.string.info_notes_title)
                onDisplay(R.id.info_details_text, R.string.info_notes_details)
                notDisplay(R.id.notes_recycler)
            } else {
                notDisplay(R.id.info_title_text, R.string.info_notes_title)
                notDisplay(R.id.info_details_text, R.string.info_notes_details)
                onDisplay(R.id.notes_recycler)
            }
        }

    }

}