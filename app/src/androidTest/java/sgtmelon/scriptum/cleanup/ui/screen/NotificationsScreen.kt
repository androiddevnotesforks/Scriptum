package sgtmelon.scriptum.cleanup.ui.screen

import org.junit.Assert.assertTrue
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.waitAfter
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.testData.SimpleInfoPage
import sgtmelon.scriptum.cleanup.testData.State
import sgtmelon.scriptum.cleanup.ui.IPressBack
import sgtmelon.scriptum.cleanup.ui.ParentRecyclerScreen
import sgtmelon.scriptum.cleanup.ui.item.NotificationItemUi
import sgtmelon.scriptum.cleanup.ui.part.info.SimpleInfoContainer
import sgtmelon.scriptum.cleanup.ui.part.panel.SnackbarPanel
import sgtmelon.scriptum.cleanup.ui.part.toolbar.SimpleToolbar
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed

/**
 * Class for UI control of [NotificationsActivity].
 */
class NotificationsScreen : ParentRecyclerScreen(R.id.recycler_view), IPressBack {

    //region Views

    private val parentContainer = getViewById(R.id.parent_container)
    private val toolbar = SimpleToolbar(R.string.title_notification, withBack = true)

    private val infoContainer = SimpleInfoContainer(SimpleInfoPage.NOTIFICATION)

    fun getSnackbar(func: SnackbarPanel.() -> Unit = {}): SnackbarPanel {
        val message = R.string.snackbar_message_notification
        val action = R.string.snackbar_action_cancel

        return SnackbarPanel(message, action, func)
    }

    private fun getItem(p: Int) = NotificationItemUi(recyclerView, p)

    //endregion

    fun onClickClose() {
        toolbar.getToolbarButton().click()
    }

    fun openText(
        item: NoteItem.Text,
        p: Int? = random,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) {
        if (p == null) return

        getItem(p).view.click()
        TextNoteScreen(func, State.READ, item, isRankEmpty)
    }

    fun openRoll(
        item: NoteItem.Roll,
        p: Int? = random,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) {
        if (p == null) return

        getItem(p).view.click()
        RollNoteScreen(func, State.READ, item, isRankEmpty)
    }

    fun onClickCancel(p: Int? = random, isWait: Boolean = false) = apply {
        if (p == null) return@apply

        waitAfter(time = if (isWait) SNACK_BAR_TIME else 0) {
            getItem(p).cancelButton.click()
            getSnackbar { assert() }
        }
    }

    //region Assertion

    fun onAssertItem(p: Int, item: NoteItem) = getItem(p).assert(item)

    fun assert(isEmpty: Boolean) = apply {
        parentContainer.isDisplayed()
        toolbar.assert()

        infoContainer.assert(isEmpty)
        recyclerView.isDisplayed(!isEmpty)
    }

    fun assertSnackbarDismiss() {
        assertTrue(try {
            getSnackbar().assert()
            false
        } catch (e: Throwable) {
            true
        })
    }

    //endregion

    companion object {
        operator fun invoke(
            func: NotificationsScreen.() -> Unit,
            isEmpty: Boolean
        ): NotificationsScreen {
            return NotificationsScreen().assert(isEmpty).apply(func)
        }
    }
}