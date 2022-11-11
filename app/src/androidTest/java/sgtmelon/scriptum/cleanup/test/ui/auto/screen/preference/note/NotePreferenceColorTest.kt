package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.test.parent.situation.IColorTest
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.preferences.PreferencesImpl
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test of [PreferencesImpl.defaultColor] setup for [MenuPreferenceFragment]
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceColorTest : ParentUiTest(),
    INotePreferenceTest,
    IColorTest {

    @Test fun dialogClose() = runTest {
        val color = preferencesRepo.defaultColor

        openColorDialog(color) { onClickCancel() }
        assert()
        openColorDialog(color) { softClose() }
        assert()
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
        val initValue = switchValue(value)

        assertNotEquals(initValue, value)

        runTest {
            openColorDialog(initValue) { onAssertItem().onClickItem(value).onClickApply() }
            assert()
        }

        assertEquals(value, preferencesRepo.defaultColor)
    }

    /**
     * Switch [Color] to another one. Setup defaultColor for application which not equals [value].
     */
    private fun switchValue(value: Color): Color {
        val list = Color.values()
        var initValue: Color

        do {
            initValue = list.random()
            preferencesRepo.defaultColor = initValue
        } while (initValue == value)

        return initValue
    }
}