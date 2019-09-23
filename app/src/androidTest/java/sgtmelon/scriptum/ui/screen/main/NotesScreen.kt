package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.longClick
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.NoteDialogUi
import sgtmelon.scriptum.ui.screen.NotificationScreen
import sgtmelon.scriptum.ui.screen.PreferenceScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [NotesFragment]
 */
class NotesScreen(hide: Boolean) : ParentRecyclerScreen(R.id.notes_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.notes_parent_container)
    private val toolbar = getToolbar(R.string.title_notes)

    private val notificationMenuItem = getViewById(R.id.item_notification)
    private val preferenceMenuItem = getViewById(R.id.item_preference)

    private val infoTitleText = getViewById(R.id.info_title_text).withText(
            if (hide) R.string.info_notes_hide_title else R.string.info_notes_empty_title
    )

    private val infoDetailsText = getViewById(R.id.info_details_text).withText(if (hide) {
        R.string.info_notes_hide_details
    } else {
        R.string.info_notes_empty_details
    })

    //endregion

    fun openNotification(empty: Boolean = false, func: NotificationScreen.() -> Unit = {}) {
        notificationMenuItem.click()
        NotificationScreen.invoke(func, empty)
    }

    fun openPreference(func: PreferenceScreen.() -> Unit = {}) {
        preferenceMenuItem.click()
        PreferenceScreen.invoke(func)
    }

    fun openNoteDialog(noteModel: NoteModel, p: Int = random,
                       func: NoteDialogUi.() -> Unit = {}) {
        recyclerView.longClick(p)
        NoteDialogUi.invoke(func, noteModel)
    }

    fun openTextNote(noteModel: NoteModel, p: Int = random,
                     func: TextNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        TextNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun openRollNote(noteModel: NoteModel, p: Int = random,
                     func: RollNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        RollNoteScreen.invoke(func, State.READ, noteModel)
    }


    fun assert(empty: Boolean) {
        parentContainer.isDisplayed()
        toolbar.isDisplayed()

        notificationMenuItem.isDisplayed()
        preferenceMenuItem.isDisplayed()

        infoTitleText.isDisplayed(empty)
        infoDetailsText.isDisplayed(empty)
        recyclerView.isDisplayed(!empty)
    }

    companion object {
        operator fun invoke(func: NotesScreen.() -> Unit, empty: Boolean, hide: Boolean) =
                NotesScreen(hide).apply { assert(empty) }.apply(func)
    }

}