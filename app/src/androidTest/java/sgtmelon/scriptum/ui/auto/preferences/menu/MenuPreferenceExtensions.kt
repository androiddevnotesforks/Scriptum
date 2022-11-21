package sgtmelon.scriptum.ui.auto.preferences.menu

import sgtmelon.scriptum.parent.ui.screen.preference.MenuPreferenceScreen
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

inline fun ParentUiTest.startMenuPreferenceTest(
    before: () -> Unit = {},
    crossinline func: MenuPreferenceScreen.() -> Unit
) {
    launch(before) { mainScreen { openNotes(isEmpty = true) { openPreferences { func() } } } }
}