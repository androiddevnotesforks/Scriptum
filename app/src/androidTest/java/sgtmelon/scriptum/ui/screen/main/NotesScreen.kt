package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.longClick
import sgtmelon.scriptum.data.InfoPage
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.NoteDialogUi
import sgtmelon.scriptum.ui.item.NoteItem
import sgtmelon.scriptum.ui.part.InfoContainer
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

    private val infoContainer = InfoContainer(InfoPage.NOTES, hide)

    private fun getItem(p: Int) = NoteItem(recyclerView, p)

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
        getItem(p).view.longClick()
        NoteDialogUi.invoke(func, noteModel)
    }

    fun openTextNote(noteModel: NoteModel, p: Int = random,
                     func: TextNoteScreen.() -> Unit = {}) {
        getItem(p).view.click()
        TextNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun openRollNote(noteModel: NoteModel, p: Int = random,
                     func: RollNoteScreen.() -> Unit = {}) {
        getItem(p).view.click()
        RollNoteScreen.invoke(func, State.READ, noteModel)
    }


    fun onAssertItem(noteModel: NoteModel, p: Int = random) {
        getItem(p).assert(noteModel)
    }

    fun assert(empty: Boolean) {
        parentContainer.isDisplayed()
        toolbar.isDisplayed()

        notificationMenuItem.isDisplayed()
        preferenceMenuItem.isDisplayed()

        infoContainer.assert(empty)
        recyclerView.isDisplayed(!empty)
    }

    companion object {
        operator fun invoke(func: NotesScreen.() -> Unit, empty: Boolean, hide: Boolean) =
                NotesScreen(hide).apply { assert(empty) }.apply(func)
    }

}