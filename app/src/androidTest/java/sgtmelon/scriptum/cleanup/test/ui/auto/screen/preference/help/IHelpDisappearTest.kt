package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.help

import sgtmelon.scriptum.cleanup.ui.screen.preference.help.HelpDisappearScreen
import sgtmelon.scriptum.parent.ui.launch
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Interface for all [HelpDisappearScreen] tests.
 */
interface IHelpDisappearTest {

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: HelpDisappearScreen.() -> Unit) {
        launch(before) {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openPreferences {
                        openHelp { openNotificationDisappear(func) }
                    }
                }
            }
        }
    }
}