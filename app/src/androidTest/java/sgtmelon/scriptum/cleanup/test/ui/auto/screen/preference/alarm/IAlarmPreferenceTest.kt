package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.alarm

import sgtmelon.scriptum.parent.ui.screen.preference.alarm.AlarmPreferenceLogic
import sgtmelon.scriptum.parent.ui.screen.preference.alarm.AlarmPreferenceScreen

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Interface for all [AlarmPreferenceScreen] tests.
 */
interface IAlarmPreferenceTest {

    fun getLogic() = AlarmPreferenceLogic()

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: AlarmPreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { openNotes(isEmpty = true) { openPreferences { openAlarm(func) } } }
        }
    }
}