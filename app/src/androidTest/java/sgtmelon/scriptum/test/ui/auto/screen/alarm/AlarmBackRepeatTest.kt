package sgtmelon.scriptum.test.ui.auto.screen.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.annotation.Repeat
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IRepeatTest


/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmBackRepeatTest : ParentUiTest(), IRepeatTest {

    @Test override fun repeatMin10() = super.repeatMin10()

    @Test override fun repeatMin30() = super.repeatMin30()

    @Test override fun repeatMin60() = super.repeatMin60()

    @Test override fun repeatMin180() = super.repeatMin180()

    @Test override fun repeatMin1440() = super.repeatMin1440()

    override fun startTest(@Repeat value: Int) {
        preferenceRepo.repeat = value

        data.insertNote().let {
            launchAlarm(it) { openAlarm(it) { onPressBack() }.mainScreen() }
        }
    }
}