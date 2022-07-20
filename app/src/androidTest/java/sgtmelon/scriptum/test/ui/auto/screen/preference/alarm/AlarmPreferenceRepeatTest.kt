package sgtmelon.scriptum.test.ui.auto.screen.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IRepeatTest
import sgtmelon.scriptum.ui.dialog.preference.RepeatDialogUi

/**
 * Test for [AlarmPreferenceFragment] and [RepeatDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceRepeatTest : ParentUiTest(),
    IAlarmPreferenceTest,
    IRepeatTest {

    @Test fun dialogClose() = runTest {
        openRepeatDialog { onCloseSoft() }
        assert()
        openRepeatDialog { onClickCancel() }
        assert()
    }

    @Test override fun repeatMin10() = super.repeatMin10()

    @Test override fun repeatMin30() = super.repeatMin30()

    @Test override fun repeatMin60() = super.repeatMin60()

    @Test override fun repeatMin180() = super.repeatMin180()

    @Test override fun repeatMin1440() = super.repeatMin1440()

    override fun startTest(repeat: Repeat) {
        val initValue = switchValue(repeat)

        assertNotEquals(initValue, repeat)

        runTest {
            openRepeatDialog {
                onClickItem(repeat).onClickItem(initValue).onClickItem(repeat).onClickApply()
            }
            assert()
        }

        assertEquals(repeat, preferences.repeat)
    }

    /**
     * Switch [Repeat] to another one.
     */
    private fun switchValue(repeat: Repeat): Repeat {
        val list = Repeat.values()
        var initValue: Repeat

        do {
            initValue = list.random()
            preferencesRepo.repeat = initValue
        } while (initValue == repeat)

        return initValue
    }
}