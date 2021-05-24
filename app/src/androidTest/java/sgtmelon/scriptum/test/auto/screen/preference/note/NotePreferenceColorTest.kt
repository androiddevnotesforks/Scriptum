package sgtmelon.scriptum.test.auto.screen.preference.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IColorTest
import sgtmelon.scriptum.ui.screen.preference.NotePreferenceScreen

/**
 * Test of [PreferenceRepo.defaultColor] setup for [PreferenceFragment]
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceColorTest : ParentUiTest(), IColorTest {

    private fun runTest(before: () -> Unit = {}, func: NotePreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openNote(func) } } }
        }
    }

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

    override fun startTest(@Color color: Int) {
        switchValue(color)

        val initColor = preferenceRepo.defaultColor

        runTest {
            openColorDialog(initColor) {
                onAssertItem().onClickItem(color).onClickApply()
            }
            assert()
        }
    }

    /**
     * Switch [Color] to another one.
     */
    private fun switchValue(@Color color: Int) {
        val list = Color.list

        while (preferenceRepo.defaultColor == color) {
            preferenceRepo.defaultColor = list.random()
        }
    }
}