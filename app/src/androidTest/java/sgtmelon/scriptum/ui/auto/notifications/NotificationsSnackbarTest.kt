package sgtmelon.scriptum.ui.auto.notifications

import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.parts.SnackbarPart
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerItemPart
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.auto.startNotesTest
import sgtmelon.scriptum.ui.cases.ListCancelSnackbarCase
import sgtmelon.test.cappuccino.utils.await

/**
 * Test for SnackBar in [NotificationsActivity].
 */
class NotificationsSnackbarTest : ParentUiTest(),
    ListCancelSnackbarCase {

    @Test override fun displayInsets() = startNotificationListTest {
        startDisplayInserts(screen = this)
    }

    @Test override fun singleActionClick() = startNotificationListTest(count = 5) {
        val p = it.indices.random()

        itemCancel(p)
        snackbar { action() }
        assertSnackbarDismissed()
        assertItem(it[p], p)
    }

    @Test override fun manyActionClick() = startNotificationListTest(count = 3) {
        repeat(it.size) { itemCancel(p = 0) }
        repeat(it.size) { i ->
            snackbar {
                action()
                if (i != it.lastIndex) {
                    assert()
                }
            }
        }

        assertSnackbarDismissed()
        assertList(it)
    }

    @Test fun actionClickWithNoteCheck() = startNotificationListTest(count = 5) {
        val p = it.indices.random()

        itemCancel(p)
        snackbar { action() }
        assertSnackbarDismissed()

        when (val item = it[p]) {
            is NoteItem.Text -> openText(item, p) {
                controlPanel { onNotification(isUpdateDate = true) }
            }
            is NoteItem.Roll -> openRoll(item, p) {
                controlPanel { onNotification(isUpdateDate = true) }
            }
        }
    }

    @Test override fun scrollTopAfterAction() = startNotificationListTest {
        val p = it.indices.first

        itemCancel(p)
        scrollTo(Scroll.END)
        snackbar { action() }

        await(RecyclerItemPart.SCROLL_TIME)
        RecyclerItemPart.PREVENT_SCROLL = true

        assertSnackbarDismissed()
        assertItem(it[p], p)
    }

    @Test override fun scrollBottomAfterAction() = startNotificationListTest {
        val p = it.lastIndex

        itemCancel(p)
        scrollTo(Scroll.START)
        snackbar { action() }

        await(RecyclerItemPart.SCROLL_TIME)
        RecyclerItemPart.PREVENT_SCROLL = true

        assertSnackbarDismissed()
        assertItem(it[p], p)
    }

    @Test override fun restoreAfterPause() {
        val list = db.fillNotifications(count = 3)

        startNotesTest {
            openNotifications {
                itemCancel(p = 0)

                snackbar { assert() }

                /** Position = 1, because item removed from screen list. */
                when (val it = list[1]) {
                    is NoteItem.Text -> openText(it, p = 0) { clickClose() }
                    is NoteItem.Roll -> openRoll(it, p = 0) { clickClose() }
                }

                snackbar { assert() }

                /** Position = 1, because item removed from screen list. */
                when (val it = list[1]) {
                    is NoteItem.Text -> openText(it, p = 0) {
                        await(time = SnackbarPart.DISMISS_TIME * 2)
                        clickClose()
                    }
                    is NoteItem.Roll -> openRoll(it, p = 0) {
                        await(time = SnackbarPart.DISMISS_TIME * 2)
                        clickClose()
                    }
                }

                snackbar {
                    assert()
                    action()
                }

                assertList(list)
                repeat(list.size) { itemCancel(p = 0) }
                clickClose()
            }

            openNotifications(isEmpty = true) { assertSnackbarDismissed() }
        }
    }

    @Test override fun clearCacheOnDismiss() = startNotificationListTest {
        val p = it.indices.random()

        itemCancel(p)
        snackbar { action() }
        assertSnackbarDismissed()

        when (val item = it[p]) {
            is NoteItem.Text -> openText(item, p) { clickClose() }
            is NoteItem.Roll -> openRoll(item, p) { clickClose() }
        }

        assertSnackbarDismissed()
    }

    @Test override fun dismissTimeout() = startNotificationListTest {
        val removePosition = it.indices.random()

        itemCancel(removePosition)
        it.removeAt(removePosition)
        await(SnackbarPart.DISMISS_TIME)
        assertSnackbarDismissed()

        val p = it.indices.random()
        when (val item = it[p]) {
            is NoteItem.Text -> openText(item, p) { clickClose() }
            is NoteItem.Roll -> openRoll(item, p) { clickClose() }
        }

        assertSnackbarDismissed()
    }

    @Test fun clearCacheOnScreenClose() {
        val list = db.fillNotifications(count = 5)
        val removePosition = 1
        val actionPosition = 2
        val actionShiftPosition = actionPosition - 1

        startNotesTest {
            openNotifications {
                itemCancel(removePosition)
                itemCancel(actionShiftPosition)

                snackbar { action().assert() }

                assertItem(list[actionPosition], actionShiftPosition)
                clickClose()
            }

            list.removeAt(removePosition)

            openNotifications {
                assertSnackbarDismissed()
                assertList(list)
            }
        }
    }

}