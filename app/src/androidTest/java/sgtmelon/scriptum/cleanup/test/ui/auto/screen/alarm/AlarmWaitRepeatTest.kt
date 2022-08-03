package sgtmelon.scriptum.cleanup.test.ui.auto.screen.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest
import sgtmelon.scriptum.cleanup.test.parent.situation.IRepeatTest
import sgtmelon.scriptum.infrastructure.model.key.Repeat

/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmWaitRepeatTest : ParentUiTest(), IRepeatTest {

    @Test override fun repeatMin10() = super.repeatMin10()

    @Test override fun repeatMin30() = super.repeatMin30()

    @Test override fun repeatMin60() = super.repeatMin60()

    @Test override fun repeatMin180() = super.repeatMin180()

    @Test override fun repeatMin1440() = super.repeatMin1440()

    override fun startTest(value: Repeat) {
        preferencesRepo.repeat = value

        data.insertNote().let {
            launchAlarm(it) { openAlarm(it) { waitRepeat() }.mainScreen() }
        }
    }
}