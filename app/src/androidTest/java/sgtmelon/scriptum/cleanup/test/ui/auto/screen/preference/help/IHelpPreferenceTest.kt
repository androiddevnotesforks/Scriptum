package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.help

import sgtmelon.scriptum.cleanup.ui.screen.preference.help.HelpPreferenceScreen
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest
import sgtmelon.scriptum.ui.testing.parent.launch

/**
 * Interface for all [HelpPreferenceScreen] tests.
 */
interface IHelpPreferenceTest {

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: HelpPreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreferences { openHelp(func) } } }
        }
    }
}