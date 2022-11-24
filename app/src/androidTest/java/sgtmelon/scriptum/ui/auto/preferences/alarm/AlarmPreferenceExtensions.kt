package sgtmelon.scriptum.ui.auto.preferences.alarm

import sgtmelon.scriptum.parent.ui.screen.preference.alarm.AlarmPreferenceScreen
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

inline fun ParentUiTest.startAlarmPreferenceTest(
    before: () -> Unit = {},
    crossinline func: AlarmPreferenceScreen.() -> Unit
) {
    launch(before) {
        mainScreen { openNotes(isEmpty = true) { openPreferences { openAlarm { func() } } } }
    }
}