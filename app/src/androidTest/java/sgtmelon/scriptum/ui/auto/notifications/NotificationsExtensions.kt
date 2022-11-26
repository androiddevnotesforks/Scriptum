package sgtmelon.scriptum.ui.auto.notifications

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.parent.ui.screen.notifications.NotificationsScreen
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

inline fun ParentUiTest.startNotificationListTest(
    count: Int = 15,
    crossinline func: NotificationsScreen.(list: MutableList<NoteItem>) -> Unit = {}
) {
    val list = db.fillNotifications(count)
    launchSplash { mainScreen { openNotes { openNotifications { func(list) } } } }
}

inline fun <T : NoteItem> ParentUiTest.startNotificationItemTest(
    item: T,
    crossinline func: NotificationsScreen.(T) -> Unit
) {
    launchSplash({ db.insertNotification(item) }) {
        mainScreen { openNotes { openNotifications { func(item) } } }
    }
}