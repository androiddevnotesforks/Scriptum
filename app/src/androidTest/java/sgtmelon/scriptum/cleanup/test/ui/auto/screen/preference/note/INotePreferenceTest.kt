package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.note

import sgtmelon.scriptum.cleanup.ui.screen.preference.NotePreferenceScreen
import sgtmelon.scriptum.parent.ui.ParentUiTest
import sgtmelon.scriptum.parent.ui.launch

/**
 * Interface for all [NotePreferenceScreen] tests.
 */
interface INotePreferenceTest {

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: NotePreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreferences { openNote(func) } } }
        }
    }
}