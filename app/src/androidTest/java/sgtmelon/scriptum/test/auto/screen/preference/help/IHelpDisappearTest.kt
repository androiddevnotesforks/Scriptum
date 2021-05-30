package sgtmelon.scriptum.test.auto.screen.preference.help

import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.screen.preference.help.HelpDisappearScreen

/**
 * Interface for all [HelpDisappearScreen] tests.
 */
interface IHelpDisappearTest {

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: HelpDisappearScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openHelp { openNotificationDisappear(func) } } } }
        }
    }
}