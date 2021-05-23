package sgtmelon.scriptum.test.auto.preference.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.screen.preference.NotePreferenceScreen

/**
 * Test for [NotePreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceTest : ParentUiTest() {

    private fun runTest(before: () -> Unit = {}, func: NotePreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openNote(func) } } }
        }
    }

    @Test fun fill() {
        TODO()
    }

    /**
     * TODO
     * - close
     * - assertAll
     *   - on stop - false
     *   - on stop - true
     *   - auto save - false (period not enabled)
     *   - auto save - true (period is enabled)
     *
     * - sort tests
     *   - select all items like in [PreferenceThemeTest]
     *   - close
     *
     * - default color
     *   - select all items like in [PreferenceThemeTest]
     *   - close
     *
     * - on stop click
     * - on auto save click (period enabled change)
     *
     * - period
     *   - select all items like in [PreferenceThemeTest]
     *   - close
     *
     * Rotate:
     * - content
     *   - on stop - false
     *   - on stop - true
     *   - auto save - false (period not enabled)
     *   - auto save - true (period is enabled)
     *
     * - sort dialog
     * - default color dialog
     * - period dialog
     */

}