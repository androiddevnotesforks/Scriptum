package sgtmelon.scriptum.tests.ui.auto.preferences.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.preferences.PreferencesImpl
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.value.ColorCase
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchNotesPreference
import sgtmelon.test.common.getDifferentValues
import sgtmelon.test.common.oneFourthChance

/**
 * Test of [PreferencesImpl.defaultColor] setup for [MenuPreferenceFragment]
 */
@RunWith(AndroidJUnit4::class)
class NotesPreferenceColorTest : ParentUiTest(),
    DialogCloseCase,
    ColorCase {

    @Test override fun close() {
        val color = Color.values().random()

        launchNotesPreference(before = { preferencesRepo.defaultColor = color }) {
            openColorDialog(color) { cancel() }
            assert()
            openColorDialog(color) { softClose() }
            assert()
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

    override fun startTest(value: Color) {
        val (setValue, initValue) = Color.values().getDifferentValues(value)
        preferencesRepo.defaultColor = initValue

        launchNotesPreference {
            openColorDialog(initValue) {
                assertItem()
                if (oneFourthChance()) selectLong(setValue) else select(setValue)
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.defaultColor)
    }
}