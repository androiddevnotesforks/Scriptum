package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.toolbar

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.ui.cases.note.NoteUIColorTest

/**
 * Test for [NoteActivity] UI colors with [ThemeDisplayed.DARK].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteUIColorLightTest : NoteUIColorTest(ThemeDisplayed.LIGHT, NoteType.ROLL) {

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

}