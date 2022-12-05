package sgtmelon.scriptum.parent.ui.screen.notifications

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.parent.ui.feature.BackPress
import sgtmelon.scriptum.parent.ui.feature.ListSnackbarWork
import sgtmelon.scriptum.parent.ui.feature.OpenNote
import sgtmelon.scriptum.parent.ui.feature.ToolbarBack
import sgtmelon.scriptum.parent.ui.model.exception.EmptyListException
import sgtmelon.scriptum.parent.ui.model.key.InfoCase
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.scriptum.parent.ui.parts.ContainerPart
import sgtmelon.scriptum.parent.ui.parts.SnackbarPart
import sgtmelon.scriptum.parent.ui.parts.info.InfoContainerPart
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerPart
import sgtmelon.scriptum.parent.ui.parts.toolbar.TitleToolbarPart
import sgtmelon.scriptum.parent.ui.screen.item.NotificationItemUi
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.isDisplayed

/**
 * Class for UI control of [NotificationsActivity].
 */
class NotificationsScreen : ContainerPart(TestViewTag.NOTIFICATIONS),
    RecyclerPart<NoteItem, NotificationItemUi>,
    OpenNote,
    ListSnackbarWork,
    ToolbarBack,
    BackPress {

    //region Views

    override val toolbar = TitleToolbarPart(parentContainer, R.string.title_notification)

    override val recyclerView = getView(R.id.recycler_view)

    private val infoContainer = InfoContainerPart(parentContainer, InfoCase.Notifications)

    override val snackbarMessage = R.string.snackbar_message_notification
    override val snackbarAction = R.string.snackbar_action_cancel

    override fun getItem(p: Int) = NotificationItemUi(recyclerView, p)

    override val openNoteState: NoteState = NoteState.READ

    //endregion

    override fun itemCancel(p: Int?, isWait: Boolean) {
        if (p == null) throw EmptyListException()

        getItem(p).cancel()
        snackbar { assert() }

        if (isWait) {
            await(SnackbarPart.DISMISS_TIME)
        }
    }

    fun assert(isEmpty: Boolean) = apply {
        parentContainer.isDisplayed()
        toolbar.assert()
        infoContainer.assert(isEmpty)
        recyclerView.isDisplayed(!isEmpty)
    }

    companion object {
        inline operator fun invoke(
            func: NotificationsScreen.() -> Unit,
            isEmpty: Boolean
        ): NotificationsScreen {
            return NotificationsScreen().assert(isEmpty).apply(func)
        }
    }
}