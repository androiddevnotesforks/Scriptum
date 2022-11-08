package sgtmelon.scriptum.ui.auto

import sgtmelon.scriptum.cleanup.ui.screen.main.NotesScreen
import sgtmelon.scriptum.parent.ui.ParentUiTest
import sgtmelon.scriptum.parent.ui.launch
import sgtmelon.scriptum.parent.ui.screen.notifications.NotificationsScreen

// TODO try make parameter inline (without crossinline)
inline fun ParentUiTest.startNotesTest(crossinline func: NotesScreen.() -> Unit) {
    launch { mainScreen { notesScreen { func() } } }
}

inline fun ParentUiTest.startNotificationsTest(crossinline func: NotificationsScreen.() -> Unit) {
    startNotesTest { openNotifications { func() } }
}