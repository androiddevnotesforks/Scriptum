package sgtmelon.scriptum.tests.ui.auto.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchAlarm
import sgtmelon.scriptum.source.ui.tests.launchAlarmClose
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.value.RepeatCase


/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmMoreDialogTest : ParentUiTest(),
    RepeatCase,
    DialogCloseCase {

    @Test override fun close() = launchAlarm(db.insertNote()) {
        openMoreDialog { softClose() }
        assert()
        openMoreDialog { swipeClose() }
        assert()
    }

    @Test override fun repeatMin10() = super.repeatMin10()

    @Test override fun repeatMin30() = super.repeatMin30()

    @Test override fun repeatMin60() = super.repeatMin60()

    @Test override fun repeatMin180() = super.repeatMin180()

    @Test override fun repeatMin1440() = super.repeatMin1440()

    override fun startTest(value: Repeat) = launchAlarmClose(db.insertNote()) {
        openMoreDialog { repeat(value) }
        setAlarm(it, value, resources)
    }
}