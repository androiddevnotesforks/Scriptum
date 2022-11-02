package sgtmelon.scriptum.cleanup.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.testData.State
import sgtmelon.scriptum.cleanup.ui.ParentRecyclerScreen
import sgtmelon.scriptum.cleanup.ui.dialog.NoteDialogUi
import sgtmelon.scriptum.cleanup.ui.item.NoteItemUi
import sgtmelon.scriptum.cleanup.ui.part.info.NotesInfoContainer
import sgtmelon.scriptum.cleanup.ui.part.toolbar.SimpleToolbar
import sgtmelon.scriptum.cleanup.ui.screen.NotificationsScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.preference.MenuPreferenceScreen
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.longClick
import sgtmelon.test.cappuccino.utils.withMenuDrawable
import sgtmelon.test.cappuccino.utils.withMenuTitle

/**
 * Class for UI control of [NotesFragment].
 *
 * [isHide] - have hide notes or not.
 */
class NotesScreen(private val isHide: Boolean) : ParentRecyclerScreen(R.id.notes_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.notes_parent_container)
    private val toolbar = SimpleToolbar(R.string.title_notes, withBack = false)

    private val notificationsMenuItem = getViewById(R.id.item_notifications)
    private val preferencesMenuItem = getViewById(R.id.item_preferences)

    private val infoContainer = NotesInfoContainer(isHide)

    private fun getItem(p: Int) = NoteItemUi(recyclerView, p)

    //endregion

    fun openNotifications(isEmpty: Boolean = false, func: NotificationsScreen.() -> Unit = {}) {
        notificationsMenuItem.click()
        NotificationsScreen(func, isEmpty)
    }

    fun openPreferences(func: MenuPreferenceScreen.() -> Unit = {}) {
        preferencesMenuItem.click()
        MenuPreferenceScreen(func)
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
            .withMenuDrawable(
                R.id.item_notifications,
                R.drawable.ic_notifications,
                R.attr.clContent
            )
            .withMenuTitle(R.id.item_notifications, R.string.menu_notifications)
            .withMenuDrawable(R.id.item_preferences, R.drawable.ic_preference, R.attr.clContent)
            .withMenuTitle(R.id.item_preferences, R.string.menu_preference)

        notificationsMenuItem.isDisplayed()
        preferencesMenuItem.isDisplayed()

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