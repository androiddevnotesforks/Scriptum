package sgtmelon.scriptum.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IColorTest

/**
 * Test of [PreferenceRepo.defaultColor] setup for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteColorDialogTest : ParentUiTest(), IColorTest {

    @Test fun closeAndWork() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) {
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

    private fun startThemeTest(@Theme theme: Int) = data.createText().let {
        setupTheme(theme)

        launch {
            mainScreen {
                openAddDialog { createText(it) { controlPanel { onColor { onAssertAll() } } } }
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
     * Check [PreferenceRepo.defaultColor] work.
     */
    override fun startTest(@Color value: Int) {
        preferenceRepo.defaultColor = value

        val item = data.createText()
        launch {
            mainScreen {
                openAddDialog { createText(item) { controlPanel { onColor { onAssertItem() } } } }
            }
        }
    }
}