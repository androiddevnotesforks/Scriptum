package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.RepeatDialogUi
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.value.RepeatCase

/**
 * Test for [AlarmPreferenceFragment] and [RepeatDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceRepeatTest : ParentUiTest(),
    IAlarmPreferenceTest,
    RepeatCase {

    @Test fun dialogClose() = runTest {
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
        val initValue = switchValue(value)

        assertNotEquals(initValue, value)

        runTest {
            openRepeatDialog {
                click(value)
                click(initValue)
                click(value)
                apply()
            }
            assert()
        }

        assertEquals(value, preferencesRepo.repeat)
    }

    /**
     * Switch [Repeat] to another one. Setup repeat for application which not equals [value].
     */
    private fun switchValue(value: Repeat): Repeat {
        val list = Repeat.values()
        var initValue: Repeat

        do {
            initValue = list.random()
            preferencesRepo.repeat = initValue
        } while (initValue == value)

        return initValue
    }
}