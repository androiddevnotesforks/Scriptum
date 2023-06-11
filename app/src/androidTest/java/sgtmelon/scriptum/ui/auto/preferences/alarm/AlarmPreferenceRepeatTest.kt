package sgtmelon.scriptum.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.ui.screen.dialogs.select.RepeatDialogUi
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchAlarmPreference
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.value.RepeatCase
import sgtmelon.test.common.getDifferentValues

/**
 * Test for [AlarmPreferenceFragment] and [RepeatDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceRepeatTest : ParentUiTest(),
    DialogCloseCase,
    RepeatCase {

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
}