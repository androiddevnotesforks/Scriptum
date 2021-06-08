package sgtmelon.scriptum.test.ui.auto.screen.preference.note

import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.screen.preference.NotePreferenceScreen

/**
 * Interface for all [NotePreferenceScreen] tests.
 */
interface INotePreferenceTest {

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: NotePreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openNote(func) } } }
        }
    }
}