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
            assertSnackbarDismiss()
        }
    }

    @Test fun singleActionClick() = startNotificationListTest(count = 5) {
        val p = it.indices.random()

        itemCancel(p)
        getSnackbar { clickCancel() }
        assertSnackbarDismiss()
        assertItem(it[p], p)
    }

    @Test fun manyActionClick() = startNotificationListTest(count = 3) {
        repeat(it.size) { itemCancel(p = 0) }
        repeat(it.size) { i ->
            getSnackbar {
                clickCancel()
                if (i != it.lastIndex) {
                    assert()
                }
            }
        }

        assertSnackbarDismiss()
        assertList(it)
    }

    @Test fun actionClickWithNoteCheck() = startNotificationListTest(count = 5) {
        val p = it.indices.random()

        itemCancel(p)
        getSnackbar { clickCancel() }
        assertSnackbarDismiss()

        when (val item = it[p]) {
            is NoteItem.Text -> openText(item, p) {
                controlPanel { onNotification(isUpdateDate = true) }
            }
            is NoteItem.Roll -> openRoll(item, p) {
                controlPanel { onNotification(isUpdateDate = true) }
            }
        }
    }

    @Test fun clearCacheOnDismiss() = startNotificationListTest {
        val p = it.indices.random()

        itemCancel(p)
        getSnackbar { clickCancel() }
        assertSnackbarDismiss()

        when (val item = it[p]) {
            is NoteItem.Text -> openText(item, p) { clickClose() }
            is NoteItem.Roll -> openRoll(item, p) { clickClose() }
        }

        assertSnackbarDismiss()
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

                getSnackbar { clickCancel().assert() }

                assertItem(list[actionPosition], actionShiftPosition)
                clickClose()
            }

            list.removeAt(removePosition)

            openNotifications {
                assertSnackbarDismiss()
                assertList(list)
            }
        }
    }

    @Test fun restoreAfterPause() {
        val list = db.fillNotifications(count = 3)

        startNotesTest {
            openNotifications {
                itemCancel(p = 0)

                getSnackbar { assert() }

                /** Position = 1, because item removed from screen list. */
                when (val it = list[1]) {
                    is NoteItem.Text -> openText(it, p = 0) { clickClose() }
                    is NoteItem.Roll -> openRoll(it, p = 0) { clickClose() }
                }

                getSnackbar { assert() }

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

                getSnackbar {
                    assert()
                    clickCancel()
                }

                assertList(list)
                repeat(list.size) { itemCancel(p = 0) }
                clickClose()
            }

            openNotifications(isEmpty = true) { assertSnackbarDismiss() }
        }
    }

    @Test fun scrollTopAfterAction() = startNotificationListTest {
        val p = it.indices.first

        itemCancel(p)
        scrollTo(Scroll.END)
        getSnackbar { clickCancel() }
        await(RecyclerItemPart.SCROLL_TIME)

        assertSnackbarDismiss()
        RecyclerItemPart.PREVENT_SCROLL = true
        assertItem(it[p], p)
    }

    @Test fun scrollBottomAfterAction() = startNotificationListTest {
        val p = it.indices.last

        itemCancel(p)
        scrollTo(Scroll.START)
        getSnackbar { clickCancel() }
        await(RecyclerItemPart.SCROLL_TIME)

        assertSnackbarDismiss()
        RecyclerItemPart.PREVENT_SCROLL = true
        assertItem(it[p], p)
    }
}