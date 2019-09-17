package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.dialog.NoteDialogUi
import sgtmelon.scriptum.ui.screen.NotificationScreen
import sgtmelon.scriptum.ui.screen.PreferenceScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [NotesFragment]
 */
class NotesScreen(var hide: Boolean) : ParentRecyclerScreen(R.id.notes_recycler) {

    fun assert(empty: Boolean) = Assert(empty, hide)

    fun openNotification(empty: Boolean = false, func: NotificationScreen.() -> Unit = {}) {
        action { onClick(R.id.item_notification) }
        NotificationScreen.invoke(func, empty)
    }

    fun openPreference(func: PreferenceScreen.() -> Unit = {}) {
        action { onClick(R.id.item_preference) }
        PreferenceScreen.invoke(func)
    }

    fun openNoteDialog(noteModel: NoteModel, p: Int = positionRandom,
                       func: NoteDialogUi.() -> Unit = {}) {
        action { onLongClick(recyclerId, p) }
        NoteDialogUi.invoke(func, noteModel)
    }

    fun openTextNote(noteModel: NoteModel, p: Int = positionRandom,
                     func: TextNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        TextNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun openRollNote(noteModel: NoteModel, p: Int = positionRandom,
                     func: RollNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        RollNoteScreen.invoke(func, State.READ, noteModel)
    }


    class Assert(empty: Boolean, hide: Boolean) : BasicMatch() {
        init {
            onDisplay(R.id.notes_parent_container)

            onDisplayToolbar(R.id.toolbar_container, R.string.title_notes)
            onDisplay(R.id.item_notification)
            onDisplay(R.id.item_preference)

            if (empty) {
                onDisplay(R.id.info_title_text,
                        if (hide) R.string.info_notes_hide_title else R.string.info_notes_empty_title
                )
                onDisplay(R.id.info_details_text,
                        if (hide) R.string.info_notes_hide_details else R.string.info_notes_empty_details
                )

                notDisplay(R.id.notes_recycler)
            } else {
                notDisplay(R.id.info_title_text,
                        if (hide) R.string.info_notes_hide_title else R.string.info_notes_empty_title
                )
                notDisplay(R.id.info_details_text,
                        if (hide) R.string.info_notes_hide_details else R.string.info_notes_empty_details
                )

                onDisplay(R.id.notes_recycler)
            }
        }
    }

    companion object {
        operator fun invoke(func: NotesScreen.() -> Unit, empty: Boolean, hide: Boolean) =
                NotesScreen(hide).apply {
                    assert(empty)
                    func()
                }
    }

}