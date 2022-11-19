package sgtmelon.scriptum.cleanup.test.ui.auto.screen.alarm

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.screen.AlarmScreen
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

inline fun <T : NoteItem> ParentUiTest.startAlarmTest(
    item: T,
    crossinline func: AlarmScreen.(T) -> Unit
) {
    launchAlarm(item) { alarmScreen(item) { func(item) } }
}

/**
 * [AlarmScreen] must be closed after calling of [func], and this extension will check it.
 */
inline fun <T : NoteItem> ParentUiTest.startAlarmCloseTest(
    item: T,
    crossinline func: AlarmScreen.(T) -> Unit
) {
    launchAlarm(item) {
        alarmScreen(item) { func(item) }
        mainScreen()
    }
}