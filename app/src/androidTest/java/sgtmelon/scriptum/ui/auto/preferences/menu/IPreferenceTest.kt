package sgtmelon.scriptum.ui.auto.preferences.menu

import sgtmelon.scriptum.parent.ui.screen.preference.MenuPreferenceScreen

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Interface for all [MenuPreferenceScreen] tests.
 */
interface IPreferenceTest {

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: MenuPreferenceScreen.() -> Unit) {
        launch(before) { mainScreen { openNotes(isEmpty = true) { openPreferences(func) } } }
    }
}