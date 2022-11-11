package sgtmelon.scriptum.ui.auto

import sgtmelon.scriptum.parent.ui.screen.main.NotesScreen
import sgtmelon.scriptum.parent.ui.screen.notifications.NotificationsScreen
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

// TODO try make parameter inline (without crossinline)
inline fun ParentUiTest.startNotesTest(crossinline func: NotesScreen.() -> Unit) {
    launch { mainScreen { openNotes { func() } } }
}

inline fun ParentUiTest.startNotificationsTest(crossinline func: NotificationsScreen.() -> Unit) {
    startNotesTest { openNotifications { func() } }
}