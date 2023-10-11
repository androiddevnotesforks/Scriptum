package sgtmelon.scriptum.tests.ui.auto.alarm

import android.content.res.Resources
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.source.cases.value.RepeatCase
import sgtmelon.scriptum.source.di.TestInjector
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchAlarmClose


/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmRepeatBackTest : ParentUiTest(), RepeatCase {

    override val resources: Resources = TestInjector.getResources()

    @Test override fun repeatMin10() = super.repeatMin10()

    @Test override fun repeatMin30() = super.repeatMin30()

    @Test override fun repeatMin60() = super.repeatMin60()

    @Test override fun repeatMin180() = super.repeatMin180()

    @Test override fun repeatMin1440() = super.repeatMin1440()

    /** If click 'back' button - dismiss notification. */
    override fun startTest(value: Repeat) {
        preferencesRepo.repeat = value
        launchAlarmClose(db.insertNote()) { pressBack() }
    }
}