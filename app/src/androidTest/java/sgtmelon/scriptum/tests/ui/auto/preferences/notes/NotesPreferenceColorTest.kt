package sgtmelon.scriptum.tests.ui.auto.preferences.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.preferences.PreferencesImpl
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.cases.value.ColorCase
import sgtmelon.scriptum.source.ui.screen.dialogs.ColorDialogUi
import sgtmelon.scriptum.source.ui.screen.preference.notes.NotesPreferenceScreen
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchNotesPreference
import sgtmelon.test.common.getDifferentValues
import sgtmelon.test.common.oneFourthChance

/**
 * Test of [PreferencesImpl.defaultColor] setup for [MenuPreferenceFragment]
 */
@RunWith(AndroidJUnit4::class)
class NotesPreferenceColorTest : ParentUiRotationTest(),
    DialogCloseCase,
    ColorCase,
    DialogRotateCase {

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

    override fun startTest(value: Color) = runWorkTest(value) {
        if (oneFourthChance()) selectLong(it) else select(it)
    }

    @Test override fun rotateClose() {
        val color = Color.values().random()

        launchNotesPreference(before = { preferencesRepo.defaultColor = color }) {
            assertRotationClose(color) { softClose() }
            assertRotationClose(color) { cancel() }
        }
    }

    /** Allow to [closeDialog] in different ways. */
    private fun NotesPreferenceScreen.assertRotationClose(
        color: Color,
        closeDialog: ColorDialogUi.() -> Unit
    ) {
        openColorDialog(color) {
            rotate.switch()
            assert()
            closeDialog(this)
        }
        assert()
    }

    @Test override fun rotateWork() = runWorkTest { assertRotationClick(it) }

    /** Allow to click different [value] and rotate+check after that. */
    private fun ColorDialogUi.assertRotationClick(value: Color) {
        select(value)
        rotate.switch()
        assert()
    }

    /** Allow to run work test with different [action]. */
    private fun runWorkTest(value: Color? = null, action: ColorDialogUi.(value: Color) -> Unit) {
        val (setValue, initValue) = Color.values().getDifferentValues(value)
        preferencesRepo.defaultColor = initValue

        launchNotesPreference {
            openColorDialog(initValue) {
                assertItem()
                action(setValue)
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.defaultColor)
    }
}