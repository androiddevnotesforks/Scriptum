package sgtmelon.scriptum.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.preferences.PreferencesImpl
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IColorTest

/**
 * Test of [PreferencesImpl.defaultColor] setup for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteColorDialogTest : ParentUiTest(), IColorTest {

    @Test fun closeAndWork() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        controlPanel { onColor { onCloseSoft() } }.assert()
                        controlPanel { onColor { onClickCancel() } }.assert()
                        controlPanel { onColor { onClickAll().onClickItem().onClickApply() } }
                    }
                }
            }
        }
    }

    @Test fun lightTheme() = startThemeTest(Theme.LIGHT)

    @Test fun darkTheme() = startThemeTest(Theme.DARK)

    private fun startThemeTest(@Theme theme: Int) = data.createRoll().let {
        setupTheme(theme)

        launch {
            mainScreen {
                openAddDialog { createRoll(it) { controlPanel { onColor { onAssertAll() } } } }
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
    override fun startTest(@Color value: Int) {
        preferences.defaultColor = value

        val item = data.createRoll()
        launch {
            mainScreen {
                openAddDialog { createRoll(item) { controlPanel { onColor { onAssertItem() } } } }
            }
        }
    }
}