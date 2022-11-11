package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.note

import sgtmelon.scriptum.cleanup.ui.screen.preference.NotePreferenceScreen

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Interface for all [NotePreferenceScreen] tests.
 */
interface INotePreferenceTest {

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: NotePreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { openNotes(isEmpty = true) { openPreferences { openNote(func) } } }
        }
    }
}