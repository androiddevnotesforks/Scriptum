package sgtmelon.scriptum.test.auto.screen.preference.help

import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.screen.preference.help.HelpPreferenceScreen

/**
 * Interface for all [HelpPreferenceScreen] tests.
 */
interface IHelpPreferenceTest {

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: HelpPreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openHelp(func) } } }
        }
    }
}