package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.room.entity.NoteEntity
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
        NotificationScreen.invoke(func, empty)
    }

    fun openPreference(func: PreferenceScreen.() -> Unit = {}) {
        action { onClick(R.id.item_preference) }
        PreferenceScreen.invoke(func)
    }

    fun openNoteDialog(noteEntity: NoteEntity, p: Int = 0, func: NoteDialogUi.() -> Unit = {}) {
        action { onLongClick(recyclerId, p) }
        NoteDialogUi.invoke(func, noteEntity)
    }

    fun openTextNote(noteEntity: NoteEntity, p: Int = 0, func: TextNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        TextNoteScreen.invoke(func, State.READ, noteEntity)
    }

    fun openRollNote(noteEntity: NoteEntity, p: Int = 0, func: RollNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        RollNoteScreen.invoke(func, State.READ, noteEntity)
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