package sgtmelon.scriptum.ui.auto

import sgtmelon.scriptum.parent.ui.screen.main.NotesScreen
import sgtmelon.scriptum.parent.ui.screen.notifications.NotificationsScreen
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

inline fun ParentUiTest.startNotesTest(func: NotesScreen.() -> Unit) {
    launchSplash { mainScreen { openNotes(func = func) } }
}

inline fun ParentUiTest.startNotificationsTest(crossinline func: NotificationsScreen.() -> Unit) {
    startNotesTest { openNotifications { func() } }
}