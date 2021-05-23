package sgtmelon.scriptum.test.auto.rotation.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.parent.ParentRotationTest
import sgtmelon.scriptum.ui.screen.preference.PreferenceScreen

/**
 * Test of [PreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class PreferenceRotationTest : ParentRotationTest() {

    private fun runTest(before: () -> Unit = {}, func: PreferenceScreen.() -> Unit) {
        launch(before) { mainScreen { notesScreen(isEmpty = true) { openPreference(func) } } }
    }

    @Test fun content() = runTest {
        automator.rotateSide()
        assert()
    }

    @Test fun themeDialog() = runTest({ preferenceRepo.theme = Theme.LIGHT }) {
        openThemeDialog {
            onClickItem(listOf(Theme.DARK, Theme.SYSTEM).random())
            automator.rotateSide()
            assert()
            onClickApply()
        }
        assert()
    }

    @Test fun aboutDialog() = runTest({ preferenceRepo.isDeveloper = false }) {
        val clickTimes = (1..3).random()

        openAboutDialog {
            repeat(clickTimes) { clickLogo() }
            automator.rotateSide()
            assertEquals(clickTimes, clickCount)
            unlockDeveloper()
        }
        openDeveloper()
    }

    //    // TODO fix
    @Test fun colorDialog() = launch {
        TODO()
        //            val color = preferenceRepo.defaultColor

        //            mainScreen {
        //                notesScreen(isEmpty = true) {
        //                    openPreference {
        //                        TODO()
        //                        openColorDialog(color) {
        //                            automator.rotateSide()
        //                            assert()
        //                        }
        //                    }
        //                }
        //            }
    }
    //
    //    // TODO all dialogs

}