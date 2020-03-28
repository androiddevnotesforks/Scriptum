package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.InfoPage
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.NoteDialogUi
import sgtmelon.scriptum.ui.item.NoteItemUi
import sgtmelon.scriptum.ui.part.InfoContainer
import sgtmelon.scriptum.ui.part.toolbar.SimpleToolbar
import sgtmelon.scriptum.ui.screen.NotificationScreen
import sgtmelon.scriptum.ui.screen.PreferenceScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [NotesFragment].
 */
class NotesScreen(hide: Boolean) : ParentRecyclerScreen(R.id.notes_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.notes_parent_container)
    private val toolbar = SimpleToolbar(R.string.title_notes, withBack = false)

    private val notificationMenuItem = getViewById(R.id.item_notification)
    private val preferenceMenuItem = getViewById(R.id.item_preference)

    private val infoContainer = InfoContainer(InfoPage.NOTES, hide)

    private fun getItem(p: Int) = NoteItemUi(recyclerView, p)

    //endregion

    fun openNotification(empty: Boolean = false, func: NotificationScreen.() -> Unit = {}) {
        notificationMenuItem.click()
        NotificationScreen(func, empty)
    }

    fun openPreference(func: PreferenceScreen.() -> Unit = {}) {
        preferenceMenuItem.click()
        PreferenceScreen(func)
    }

    fun openNoteDialog(noteItem: NoteItem, p: Int = random,
                       func: NoteDialogUi.() -> Unit = {}) = apply {
        getItem(p).view.longClick()
        NoteDialogUi(func, noteItem)
    }

    fun openTextNote(noteItem: NoteItem, p: Int = random, isRankEmpty: Boolean = true,
                     func: TextNoteScreen.() -> Unit = {}) = apply {
        getItem(p).view.click()
        TextNoteScreen(func, State.READ, noteItem, isRankEmpty)
    }

    fun openRollNote(noteItem: NoteItem, p: Int = random, isRankEmpty: Boolean = true,
                     func: RollNoteScreen.() -> Unit = {}) = apply {
        getItem(p).view.click()
        RollNoteScreen(func, State.READ, noteItem, isRankEmpty)
    }


    fun onAssertItem(noteItem: NoteItem, p: Int = random) {
        getItem(p).assert(noteItem)
    }

    fun assert(empty: Boolean) = apply {
        parentContainer.isDisplayed()

        toolbar.assert()
        toolbar.contentContainer
                .withMenuItemDrawable(R.id.item_notification, R.drawable.ic_notifications)
                .withMenuTitle(R.id.item_notification, R.string.menu_notifications)
                .withMenuItemDrawable(R.id.item_preference, R.drawable.ic_preference)
                .withMenuTitle(R.id.item_preference, R.string.menu_preference)

        notificationMenuItem.isDisplayed()
        preferenceMenuItem.isDisplayed()

        infoContainer.assert(empty)
        recyclerView.isDisplayed(!empty)
    }

    companion object {
        operator fun invoke(func: NotesScreen.() -> Unit,
                            empty: Boolean, hide: Boolean): NotesScreen {
            return NotesScreen(hide).assert(empty).apply(func)
        }
    }

}