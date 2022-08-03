package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.note

import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest
import sgtmelon.scriptum.cleanup.ui.screen.preference.NotePreferenceScreen

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