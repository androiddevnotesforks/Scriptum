package sgtmelon.scriptum.source.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.item.NoteItemUi
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.source.ui.feature.OpenNote
import sgtmelon.scriptum.source.ui.feature.OpenNoteDialog
import sgtmelon.scriptum.source.ui.model.key.InfoCase
import sgtmelon.scriptum.source.ui.model.key.NoteState
import sgtmelon.scriptum.source.ui.parts.ContainerPart
import sgtmelon.scriptum.source.ui.parts.info.InfoContainerPart
import sgtmelon.scriptum.source.ui.parts.recycler.RecyclerPart
import sgtmelon.scriptum.source.ui.parts.toolbar.TitleToolbarPart
import sgtmelon.scriptum.source.ui.parts.toolbar.ToolbarItem
import sgtmelon.scriptum.source.ui.screen.notifications.NotificationsScreen
import sgtmelon.scriptum.source.ui.screen.preference.menu.MenuPreferenceScreen
import sgtmelon.test.cappuccino.utils.isDisplayed

/**
 * Class for UI control of [NotesFragment].
 *
 * [isHidden] - have hidden notes or not.
 */
class NotesScreen(private val isHidden: Boolean) : ContainerPart(TestViewTag.NOTES),
    RecyclerPart<NoteItem, NoteItemUi>,
    OpenNote,
    OpenNoteDialog {

    //region Views

    private val toolbar = TitleToolbarPart(parentContainer, R.string.title_notes, withBack = false)
    private val notificationsItem = ToolbarItem(
        R.id.item_notifications, R.drawable.ic_notifications, R.string.menu_notifications
    )
    private val preferencesItem = ToolbarItem(
        R.id.item_preferences, R.drawable.ic_preference, R.string.menu_preference
    )

    override val recyclerView = getView(R.id.recycler_view)

    private val infoContainer = InfoContainerPart(parentContainer, InfoCase.Notes(isHidden))

    override fun getItem(p: Int) = NoteItemUi(recyclerView, p)

    override val openNoteState: NoteState = NoteState.READ

    //endregion

    fun openNotifications(isEmpty: Boolean = false, func: NotificationsScreen.() -> Unit = {}) {
        notificationsItem.click()
        NotificationsScreen(func, isEmpty)
    }

    fun openPreferences(func: MenuPreferenceScreen.() -> Unit = {}) {
        preferencesItem.click()
        MenuPreferenceScreen(func)
    }

    fun assert(isEmpty: Boolean) = apply {
        parentContainer.isDisplayed()
        toolbar.assert()
            .assertItem(notificationsItem)
            .assertItem(preferencesItem)
        infoContainer.assert(isEmpty)
        recyclerView.isDisplayed(!isEmpty)
    }

    companion object {

        /**
         * Even if all list [isHide], need pass [isEmpty] = true.
         */
        inline operator fun invoke(
            func: NotesScreen.() -> Unit,
            isEmpty: Boolean,
            isHide: Boolean
        ): NotesScreen {
            return NotesScreen(isHide).assert(isEmpty).apply(func)
        }
    }
}