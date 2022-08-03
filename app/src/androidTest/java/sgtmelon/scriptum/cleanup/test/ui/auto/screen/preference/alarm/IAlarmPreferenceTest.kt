package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.alarm

import sgtmelon.scriptum.cleanup.ui.logic.preference.AlarmPreferenceLogic
import sgtmelon.scriptum.cleanup.ui.screen.preference.AlarmPreferenceScreen
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Interface for all [AlarmPreferenceScreen] tests.
 */
interface IAlarmPreferenceTest {

    fun getLogic() = AlarmPreferenceLogic()

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: AlarmPreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openAlarm(func) } } }
        }
    }
}