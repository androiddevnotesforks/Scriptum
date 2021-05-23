package sgtmelon.scriptum.test.auto.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IColorTest

/**
 * Test of [PreferenceRepo.defaultColor] setup for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteDefaultColorTest : ParentUiTest(), IColorTest {

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

        val item = data.createText()
        launch {
            mainScreen {
                openAddDialog { createText(item) { controlPanel { onColor { onAssertItem() } } } }
            }
        }
    }
}