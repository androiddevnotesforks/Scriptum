package sgtmelon.scriptum.tests.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.cases.value.RepeatCase
import sgtmelon.scriptum.source.ui.screen.dialogs.select.RepeatDialogUi
import sgtmelon.scriptum.source.ui.screen.preference.alarm.AlarmPreferenceScreen
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchAlarmPreference
import sgtmelon.test.common.getDifferentValues

/**
 * Test for [AlarmPreferenceFragment] and [RepeatDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceRepeatTest : ParentUiRotationTest(),
    DialogCloseCase,
    RepeatCase,
    DialogRotateCase {

    @Test override fun close() = launchAlarmPreference {
        openRepeatDialog { softClose() }
        assert()
        openRepeatDialog { cancel() }
        assert()
    }

    @Test override fun repeatMin10() = super.repeatMin10()

    @Test override fun repeatMin30() = super.repeatMin30()

    @Test override fun repeatMin60() = super.repeatMin60()

    @Test override fun repeatMin180() = super.repeatMin180()

    @Test override fun repeatMin1440() = super.repeatMin1440()

    override fun startTest(value: Repeat) {
        val (setValue, initValue) = Repeat.values().getDifferentValues(value)

        launchAlarmPreference({ preferencesRepo.repeat = initValue }) {
            openRepeatDialog {
                click(setValue)
                click(initValue)
                click(setValue)
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.repeat)
    }

    @Test override fun rotateClose() = launchAlarmPreference {
        assertRotationClose { softClose() }
        assertRotationClose { cancel() }
    }

    /** Allow to [closeDialog] in different ways. */
    private fun AlarmPreferenceScreen.assertRotationClose(closeDialog: RepeatDialogUi.() -> Unit) {
        openRepeatDialog {
            rotate.switch()
            assert()
            closeDialog(this)
        }
        assert()
    }

    @Test override fun rotateWork() {
        val (setValue, initValue) = Repeat.values().getDifferentValues()

        launchAlarmPreference({ preferencesRepo.repeat = initValue }) {
            openRepeatDialog {
                assertRotationClick(setValue)
                assertRotationClick(initValue)
                assertRotationClick(setValue)
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.repeat)
    }

    /** Allow to click different [value] and rotate+check after that. */
    private fun RepeatDialogUi.assertRotationClick(value: Repeat) {
        click(value)
        rotate.switch()
        assert()
    }
}