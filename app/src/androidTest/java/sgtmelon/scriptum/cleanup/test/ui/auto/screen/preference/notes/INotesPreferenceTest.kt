package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.notes

import sgtmelon.scriptum.cleanup.ui.screen.preference.NotesPreferenceScreen

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Interface for all [NotesPreferenceScreen] tests.
 */
interface INotesPreferenceTest {

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: NotesPreferenceScreen.() -> Unit) {
        launchSplash(before) {
            mainScreen { openNotes(isEmpty = true) { openPreferences { openNotes(func) } } }
        }
    }
}