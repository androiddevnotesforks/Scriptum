package sgtmelon.scriptum.ui.testing.screen.notifications

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.waitAfter
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.testData.SimpleInfoPage
import sgtmelon.scriptum.cleanup.testData.State
import sgtmelon.scriptum.cleanup.ui.item.NotificationItemUi
import sgtmelon.scriptum.cleanup.ui.part.info.SimpleInfoContainer
import sgtmelon.scriptum.cleanup.ui.part.panel.SnackbarPanel
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.ui.testing.exception.EmptyListException
import sgtmelon.scriptum.ui.testing.parent.screen.ContainerPart
import sgtmelon.scriptum.ui.testing.parent.screen.RecyclerPart
import sgtmelon.scriptum.ui.testing.parent.screen.feature.BackPress
import sgtmelon.scriptum.ui.testing.parent.screen.feature.ToolbarBack
import sgtmelon.scriptum.ui.testing.parent.screen.toolbar.TitleToolbar
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

    override val toolbar = TitleToolbar(R.string.title_notification)

    override val recyclerView = getView(R.id.recycler_view)

    private val infoContainer = SimpleInfoContainer(SimpleInfoPage.NOTIFICATION)

    inline fun getSnackbar(func: SnackbarPanel.() -> Unit = {}): SnackbarPanel {
        val message = R.string.snackbar_message_notification
        val action = R.string.snackbar_action_cancel

        return SnackbarPanel(message, action, func)
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
        TextNoteScreen(func, State.READ, item, isRankEmpty)
    }

    inline fun openRoll(
        item: NoteItem.Roll,
        p: Int? = random,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) {
        if (p == null) throw EmptyListException()

        getItem(p).view.click()
        RollNoteScreen(func, State.READ, item, isRankEmpty)
    }

    fun itemCancel(p: Int? = random, isWait: Boolean = false) = apply {
        if (p == null) throw EmptyListException()

        waitAfter(time = if (isWait) SnackbarPanel.DISMISS_TIME else 0) {
            getItem(p).cancelButton.click()
            getSnackbar { assert() }
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