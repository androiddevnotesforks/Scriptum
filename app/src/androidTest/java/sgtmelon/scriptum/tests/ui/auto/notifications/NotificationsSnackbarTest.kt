package sgtmelon.scriptum.tests.ui.auto.notifications

import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.source.cases.list.ListCancelSnackbarCase
import sgtmelon.scriptum.source.ui.model.key.Scroll
import sgtmelon.scriptum.source.ui.parts.SnackbarPart
import sgtmelon.scriptum.source.ui.parts.recycler.RecyclerItemPart
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchNotes
import sgtmelon.scriptum.source.ui.tests.launchNotificationsItem
import sgtmelon.scriptum.source.ui.tests.launchNotificationsList
import sgtmelon.test.cappuccino.utils.await

/**
 * Test for SnackBar in [NotificationsActivity].
 */
class NotificationsSnackbarTest : ParentUiRotationTest(),
    ListCancelSnackbarCase {

    @Test override fun displayInsets() = launchNotificationsList {
        startDisplayInsets(screen = this)
    }

    @Test override fun singleActionClick() = launchNotificationsList(count = 5) {
        val p = it.indices.random()

        itemCancel(p)
        snackbar { action() }
        assertSnackbarDismissed()
        assertItem(it[p], p)
    }

    @Test override fun manyActionClick() = launchNotificationsList(count = 3) {
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

    @Test fun actionClickWithNoteCheck() = launchNotificationsList(count = 5) {
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

    @Test override fun scrollTopAfterAction() = launchNotificationsList {
        val p = it.indices.first

        itemCancel(p)
        scrollTo(Scroll.END)
        snackbar { action() }

        await(RecyclerItemPart.SCROLL_TIME)
        RecyclerItemPart.PREVENT_SCROLL = true

        assertSnackbarDismissed()
        assertItem(it[p], p)
    }

    @Test override fun scrollBottomAfterAction() = launchNotificationsList {
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

        launchNotes {
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

    @Test override fun clearCacheOnDismiss() = launchNotificationsList {
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

    @Test override fun dismissTimeout() = launchNotificationsList {
        val removePosition = it.indices.random()

        itemCancel(removePosition, isWait = true)
        it.removeAt(removePosition)
        assertSnackbarDismissed()

        val p = it.indices.random()
        when (val item = it[p]) {
            is NoteItem.Text -> openText(item, p) { clickClose() }
            is NoteItem.Roll -> openRoll(item, p) { clickClose() }
        }

        assertSnackbarDismissed()
    }

    @Test override fun workAfterRotation() = launchNotificationsItem(db.insertNote()) {
        repeat(times = 3) { _ ->
            assertItem(it)
            itemCancel()
            assert(isEmpty = true)
            rotate.switch()
            assert(isEmpty = true)
            snackbar { action() }
            assert(isEmpty = false)
            assertItem(it)
        }
    }

    @Test fun clearCacheOnScreenClose() {
        val list = db.fillNotifications(count = 5)
        val removePosition = 1
        val actionPosition = 2
        val actionShiftPosition = actionPosition - 1

        launchNotes {
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