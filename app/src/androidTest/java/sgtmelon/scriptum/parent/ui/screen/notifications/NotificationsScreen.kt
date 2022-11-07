package sgtmelon.scriptum.parent.ui.screen.notifications

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.testData.InfoPage
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.parent.ui.feature.BackPress
import sgtmelon.scriptum.parent.ui.feature.ToolbarBack
import sgtmelon.scriptum.parent.ui.model.exception.EmptyListException
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.scriptum.parent.ui.parts.ContainerPart
import sgtmelon.scriptum.parent.ui.parts.SnackbarPart
import sgtmelon.scriptum.parent.ui.parts.info.InfoPart
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerPart
import sgtmelon.scriptum.parent.ui.parts.toolbar.TitleToolbarPart
import sgtmelon.scriptum.parent.ui.screen.item.NotificationItemUi
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed

/**
 * Class for UI control of [NotificationsActivity].
 */
class NotificationsScreen : ContainerPart(TestViewTag.NOTIFICATIONS),
    RecyclerPart,
    ToolbarBack,
    BackPress {

    //region Views

    override val toolbar = TitleToolbarPart(R.string.title_notification)

    override val recyclerView = getView(R.id.recycler_view)

    private val infoContainer = InfoPart(InfoPage.Notifications)

    inline fun getSnackbar(func: SnackbarPart.() -> Unit = {}): SnackbarPart {
        val message = R.string.snackbar_message_notification
        val action = R.string.snackbar_action_cancel

        return SnackbarPart(message, action, func)
    }

    fun getItem(p: Int) = NotificationItemUi(recyclerView, p)

    //endregion

    inline fun openText(
        item: NoteItem.Text,
        p: Int? = random,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) {
        if (p == null) throw EmptyListException()

        getItem(p).view.click()
        TextNoteScreen(func, NoteState.READ, item, isRankEmpty)
    }

    inline fun openRoll(
        item: NoteItem.Roll,
        p: Int? = random,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) {
        if (p == null) throw EmptyListException()

        getItem(p).view.click()
        RollNoteScreen(func, NoteState.READ, item, isRankEmpty)
    }

    fun itemCancel(p: Int? = random, isWait: Boolean = false) = apply {
        if (p == null) throw EmptyListException()

        getItem(p).clickCancel()
        getSnackbar { assert() }

        if (isWait) {
            await(SnackbarPart.DISMISS_TIME)
        }
    }

    fun assertItem(p: Int, item: NoteItem) = getItem(p).assert(item)

    fun assertList(list: List<NoteItem>) {
        for ((p, item) in list.withIndex()) {
            assertItem(p, item)
        }
    }

    fun assert(isEmpty: Boolean) = apply {
        parentContainer.isDisplayed()
        toolbar.assert()

        infoContainer.assert(isEmpty)
        recyclerView.isDisplayed(!isEmpty)
    }

    fun assertSnackbarDismiss() = getSnackbar().assertDismiss()

    companion object {
        inline operator fun invoke(
            func: NotificationsScreen.() -> Unit,
            isEmpty: Boolean
        ): NotificationsScreen {
            return NotificationsScreen().assert(isEmpty).apply(func)
        }
    }
}