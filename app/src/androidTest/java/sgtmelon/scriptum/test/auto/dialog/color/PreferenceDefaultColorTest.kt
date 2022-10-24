package sgtmelon.scriptum.test.auto.dialog.color

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.IColorTest
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test of [PreferenceRepo.defaultColor] setup for [PreferenceFragment]
 */
@RunWith(AndroidJUnit4::class)
class PreferenceDefaultColorTest : ParentUiTest(), IColorTest {

    // TODO all tests fail
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

    override fun startTest(@Color color: Int) {
        preferenceRepo.defaultColor = color

        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openPreference { openColorDialog(color) { onAssertItem() } }
                }
            }
        }
    }
}