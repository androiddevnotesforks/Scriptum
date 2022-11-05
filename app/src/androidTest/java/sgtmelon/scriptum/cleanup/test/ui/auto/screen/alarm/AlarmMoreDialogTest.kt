package sgtmelon.scriptum.cleanup.test.ui.auto.screen.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.test.parent.situation.IRepeatTest
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest


/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmMoreDialogTest : ParentUiTest(), IRepeatTest {

    @Test fun dialogClose() = db.insertNote().let {
        launchAlarm(it) {
            openAlarm(it) {
                openMoreDialog { onCloseSoft() }.assert()
                openMoreDialog { onCloseSwipe() }.assert()
            }
        }
    }

    @Test override fun repeatMin10() = super.repeatMin10()

    @Test override fun repeatMin30() = super.repeatMin30()

    @Test override fun repeatMin60() = super.repeatMin60()

    @Test override fun repeatMin180() = super.repeatMin180()

    @Test override fun repeatMin1440() = super.repeatMin1440()

    override fun startTest(value: Repeat) = db.insertNote().let {
        launchAlarm(it) { openAlarm(it) { openMoreDialog { onClickRepeat(value) } }.mainScreen() }
    }
}