package sgtmelon.scriptum.test.auto.screen.preference.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IColorTest

/**
 * Test of [PreferenceRepo.defaultColor] setup for [PreferenceFragment]
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceColorTest : ParentUiTest(),
    INotePreferenceTest,
    IColorTest {

    @Test fun dialogClose() = runTest {
        val check = preferenceRepo.defaultColor

        openColorDialog(check) { onClickCancel() }
        assert()
        openColorDialog(check) { onCloseSoft() }
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

    override fun startTest(@Color value: Int) {
        val initValue = switchValue(value)

        assertNotEquals(initValue, value)

        runTest {
            openColorDialog(initValue) { onAssertItem().onClickItem(value).onClickApply() }
            assert()
        }

        assertEquals(value, preferenceRepo.defaultColor)
    }

    /**
     * Switch [Color] to another one.
     */
    private fun switchValue(@Color value: Int): Int {
        val list = Color.list
        var initValue: Int

        do {
            initValue = list.random()
            preferenceRepo.defaultColor = initValue
        } while (preferenceRepo.defaultColor == value)

        return initValue
    }
}