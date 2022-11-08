package sgtmelon.scriptum.ui.auto.notifications

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.parent.ui.ParentUiTest
import sgtmelon.scriptum.parent.ui.launch
import sgtmelon.scriptum.parent.ui.screen.notifications.NotificationsScreen

const val NEXT_HOUR = 60
const val NEXT_DAY = NEXT_HOUR * 24
const val NEXT_WEEK = NEXT_DAY * 7
const val NEXT_MONTH = NEXT_DAY * 30
const val NEXT_YEAR = NEXT_MONTH * 12

inline fun ParentUiTest.startNotificationListTest(
    count: Int = 15,
    crossinline func: NotificationsScreen.(list: MutableList<NoteItem>) -> Unit = {}
) {
    val list = db.fillNotifications(count)
    launch { mainScreen { notesScreen { openNotifications { func(list) } } } }
}

inline fun <T : NoteItem> ParentUiTest.startNotificationItemTest(
    item: T,
    crossinline func: NotificationsScreen.(T) -> Unit
) {
    launch({ db.insertNotification(item) }) {
        mainScreen { notesScreen { openNotifications { func(item) } } }
    }
}