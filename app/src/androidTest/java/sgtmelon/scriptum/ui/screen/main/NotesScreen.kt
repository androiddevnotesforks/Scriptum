package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.NoteDialogUi
import sgtmelon.scriptum.ui.item.NoteItemUi
import sgtmelon.scriptum.ui.part.info.NotesInfoContainer
import sgtmelon.scriptum.ui.part.toolbar.SimpleToolbar
import sgtmelon.scriptum.ui.screen.NotificationScreen
import sgtmelon.scriptum.ui.screen.PreferenceScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [NotesFragment].
 *
 * [isHide] - have hide notes or not.
 */
class NotesScreen(private val isHide: Boolean) : ParentRecyclerScreen(R.id.notes_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.notes_parent_container)
    private val toolbar = SimpleToolbar(R.string.title_notes, withBack = false)

    private val notificationMenuItem = getViewById(R.id.item_notification)
    private val preferenceMenuItem = getViewById(R.id.item_preference)

    private val infoContainer = NotesInfoContainer(isHide)

    private fun getItem(p: Int) = NoteItemUi(recyclerView, p)

    //endregion

    fun openNotification(isEmpty: Boolean = false, func: NotificationScreen.() -> Unit = {}) {
        notificationMenuItem.click()
        NotificationScreen(func, isEmpty)
    }

    fun openPreference(func: PreferenceScreen.() -> Unit = {}) {
        preferenceMenuItem.click()
        PreferenceScreen(func)
    }

    fun openNoteDialog(
        item: NoteItem,
        p: Int? = random,
        func: NoteDialogUi.() -> Unit = {}
    ) = apply {
        if (p == null) return@apply

        getItem(p).view.longClick()
        NoteDialogUi(func, item)
    }

    fun openTextNote(
        item: NoteItem.Text,
        p: Int? = random,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) = apply {
        if (p == null) return@apply

        getItem(p).view.click()
        TextNoteScreen(func, State.READ, item, isRankEmpty)
    }

    fun openRollNote(
        item: NoteItem.Roll,
        p: Int? = random,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) = apply {
        if (p == null) return@apply

        getItem(p).view.click()
        RollNoteScreen(func, State.READ, item, isRankEmpty)
    }


    fun onAssertItem(item: NoteItem, p: Int? = random) {
        if (p == null) return

        getItem(p).assert(item)
    }

    fun assert(isEmpty: Boolean) = apply {
        parentContainer.isDisplayed()

        toolbar.assert()
        toolbar.contentContainer
            .withMenuDrawable(R.id.item_notification, R.drawable.ic_notifications)
            .withMenuTitle(R.id.item_notification, R.string.menu_notifications)
            .withMenuDrawable(R.id.item_preference, R.drawable.ic_preference)
            .withMenuTitle(R.id.item_preference, R.string.menu_preference)

        notificationMenuItem.isDisplayed()
        preferenceMenuItem.isDisplayed()

        infoContainer.assert(isEmpty)
        recyclerView.isDisplayed(!isEmpty)
    }

    companion object {

        /**
         * Even if all list [isHide], need pass [isEmpty] = true.
         */
        operator fun invoke(
            func: NotesScreen.() -> Unit,
            isEmpty: Boolean,
            isHide: Boolean
        ): NotesScreen {
            return NotesScreen(isHide).assert(isEmpty).apply(func)
        }
    }
}