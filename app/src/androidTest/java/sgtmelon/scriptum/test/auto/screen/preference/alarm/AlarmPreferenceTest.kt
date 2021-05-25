package sgtmelon.scriptum.test.auto.screen.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.screen.preference.AlarmPreferenceScreen

/**
 * Test for [AlarmPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceTest : ParentUiTest() {

    private fun runTest(before: () -> Unit = {}, func: AlarmPreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openAlarm(func) } } }
        }
    }

    @Test fun close() = runTest { onClickClose() }

}