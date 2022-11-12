package sgtmelon.scriptum.ui.auto.notifications

import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.parts.SnackbarPart
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerItemPart
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.auto.startNotesTest
import sgtmelon.test.cappuccino.utils.await

/**
 * Test for SnackBar in [NotificationsActivity].
 */
class NotificationsSnackbarTest : ParentUiTest() {

    /**
     * Check insets-spacing for snackbar bottom.
     */
    @Test fun displayInsets() = startNotificationListTest {
        repeat(times = 5) {
            itemCancel(last, isWait = true)
            assertSnackbarDismissed()
        }
    }

    @Test fun singleActionClick() = startNotificationListTest(count = 5) {
        val p = it.indices.random()

        itemCancel(p)
        snackbar { clickCancel() }
        assertSnackbarDismissed()
        assertItem(it[p], p)
    }

    @Test fun manyActionClick() = startNotificationListTest(count = 3) {
        repeat(it.size) { itemCancel(p = 0) }
        repeat(it.size) { i ->
            snackbar {
                clickCancel()
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
        snackbar { clickCancel() }
        assertSnackbarDismissed()

        when (val item = it[p]) {
            is NoteItem.Text -> openText(item, p = p) {
                controlPanel { onNotification(isUpdateDate = true) }
            }
            is NoteItem.Roll -> openRoll(item, p = p) {
                controlPanel { onNotification(isUpdateDate = true) }
            }
        }
    }

    @Test fun clearCacheOnDismiss() = startNotificationListTest {
        val p = it.indices.random()

        itemCancel(p)
        snackbar { clickCancel() }
        assertSnackbarDismissed()

        when (val item = it[p]) {
            is NoteItem.Text -> openText(item, p = p) { clickClose() }
            is NoteItem.Roll -> openRoll(item, p = p) { clickClose() }
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

                snackbar { clickCancel().assert() }

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

    @Test fun restoreAfterPause() {
        val list = db.fillNotifications(count = 3)

        startNotesTest {
            openNotifications {
                itemCancel(p = 0)

                snackbar().assert()

                /** Position = 1, because item removed from screen list. */
                when (val it = list[1]) {
                    is NoteItem.Text -> openText(it, p = 0) { clickClose() }
                    is NoteItem.Roll -> openRoll(it, p = 0) { clickClose() }
                }

                snackbar(withAwait = false).assert()

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

                snackbar(withAwait = false) {
                    assert()
                    clickCancel()
                }

                assertList(list)
                repeat(list.size) { itemCancel(p = 0) }
                clickClose()
            }

            openNotifications(isEmpty = true) { assertSnackbarDismissed() }
        }
    }

    @Test fun scrollTopAfterAction() = startNotificationListTest {
        val p = it.indices.first

        itemCancel(p)
        scrollTo(Scroll.END)
        snackbar { clickCancel() }
        await(RecyclerItemPart.SCROLL_TIME)

        assertSnackbarDismissed()
        RecyclerItemPart.PREVENT_SCROLL = true
        assertItem(it[p], p)
    }

    @Test fun scrollBottomAfterAction() = startNotificationListTest {
        val p = it.indices.last

        itemCancel(p)
        scrollTo(Scroll.START)
        snackbar { clickCancel() }
        await(RecyclerItemPart.SCROLL_TIME)

        assertSnackbarDismissed()
        RecyclerItemPart.PREVENT_SCROLL = true
        assertItem(it[p], p)
    }
}