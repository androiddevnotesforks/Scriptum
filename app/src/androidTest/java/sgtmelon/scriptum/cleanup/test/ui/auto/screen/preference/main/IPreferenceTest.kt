package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.main

import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest
import sgtmelon.scriptum.cleanup.ui.screen.preference.PreferenceScreen

/**
 * Interface for all [PreferenceScreen] tests.
 */
interface IPreferenceTest {

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: PreferenceScreen.() -> Unit) {
        launch(before) { mainScreen { notesScreen(isEmpty = true) { openPreference(func) } } }
    }
}