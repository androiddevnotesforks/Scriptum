package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.room.entity.NoteItem
import sgtmelon.scriptum.screen.view.main.NotesFragment
import sgtmelon.scriptum.ui.NotificationScreen
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

    fun openNotification(empty: Boolean, func: NotificationScreen.() -> Unit = {}) {
        action {onClick(R.id.item_notification)}
        NotificationScreen.invoke(empty, func)
    }

    fun openPreference(func: PreferenceScreen.() -> Unit) = PreferenceScreen().apply {
        action { onClick(R.id.item_preference) }
        func()
    }

    fun openNoteDialog(noteItem: NoteItem, p: Int = 0, func: NoteDialogUi.() -> Unit = {}) = NoteDialogUi().apply {
        action { onLongClick(recyclerId, p) }
        assert { onDisplayContent(noteItem) }
        func()
    }

    fun openTextNote(p: Int = 0, func: TextNoteScreen.() -> Unit = {}) = TextNoteScreen().apply {
        onClickItem(p)
        assert { onDisplayContent(State.READ) }
        func()
    }

    fun openRollNote(p: Int = 0, func: RollNoteScreen.() -> Unit = {}) = RollNoteScreen().apply {
        onClickItem(p)
        assert { onDisplayContent(State.READ) }
        func()
    }

    companion object {
        operator fun invoke(func: NotesScreen.() -> Unit) = NotesScreen().apply { func() }
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