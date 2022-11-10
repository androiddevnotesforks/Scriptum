package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.main

import sgtmelon.scriptum.cleanup.ui.screen.preference.MenuPreferenceScreen
import sgtmelon.scriptum.parent.ui.launch
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Interface for all [MenuPreferenceScreen] tests.
 */
interface IPreferenceTest {

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: MenuPreferenceScreen.() -> Unit) {
        launch(before) { mainScreen { notesScreen(isEmpty = true) { openPreferences(func) } } }
    }
}