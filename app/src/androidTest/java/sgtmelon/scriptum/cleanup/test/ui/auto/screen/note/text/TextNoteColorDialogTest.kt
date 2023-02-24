package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.preferences.PreferencesImpl
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteFragmentImpl
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.value.ColorCase

/**
 * Test of [PreferencesImpl.defaultColor] setup for [TextNoteFragmentImpl].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteColorDialogTest : ParentUiTest(), ColorCase {

    @Test fun closeAndWork() = db.createText().let {
        launchSplash {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        controlPanel { onColor { softClose() } }.assert()
                        controlPanel { onColor { cancel() } }.assert()
                        controlPanel { onColor { selectAll().select().apply() } }
                    }
                }
            }
        }
    }

    @Test fun lightTheme() = startThemeTest(ThemeDisplayed.LIGHT)

    @Test fun darkTheme() = startThemeTest(ThemeDisplayed.DARK)

    private fun startThemeTest(theme: ThemeDisplayed) = db.createText().let {
        setupTheme(theme)

        launchSplash {
            mainScreen {
                openAddDialog { createText(it) { controlPanel { onColor { assertAll() } } } }
            }
        }
    }

    @Test override fun colorRed() = super.colorRed()

    @Test override fun colorPurple() = super.colorPurple()

    @Test override fun colorIndigo() = super.colorIndigo()

    @Test override fun colorBlue() = super.colorBlue()

    @Test override fun colorTeal() = super.colorTeal()

    @Test override fun colorGreen() = super.colorGreen()

    @Test override fun colorYellow() = super.colorYellow()

    @Test override fun colorOrange() = super.colorOrange()

    @Test override fun colorBrown() = super.colorBrown()

    @Test override fun colorBlueGrey() = super.colorBlueGrey()

    @Test override fun colorWhite() = super.colorWhite()

    /**
     * Check [PreferencesImpl.defaultColor] work.
     */
    override fun startTest(value: Color) {
        preferencesRepo.defaultColor = value

        val item = db.createText()
        launchSplash {
            mainScreen {
                openAddDialog { createText(item) { controlPanel { onColor { assertItem() } } } }
            }
        }
    }
}