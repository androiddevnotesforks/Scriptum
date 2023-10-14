package sgtmelon.scriptum.tests.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.cases.permissions.MelodyPermissionCase
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchAlarmPreference
import kotlin.random.Random


/**
 * Test of permission request in [AlarmPreferenceFragment] for select alarm melody feature.
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceMelodyPermissionTest : ParentUiRotationTest(),
    MelodyPermissionCase {

    @Before override fun setUp() {
        super.setUp()

        preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())

        throwOnWrongApi()
        assertPermissionNotGranted(context)
    }

    @Test override fun info() = launchAlarmPreference {
        melodyPermission { positive() }
    }

    @Test override fun infoClose() = launchAlarmPreference {
        melodyPermission { softClose() }
        assert()
    }

    @Test override fun infoRotateWork() = launchAlarmPreference {
        melodyPermission {
            rotate.switch()
            positive()
        }
    }

    @Test override fun infoRotateClose() = launchAlarmPreference {
        melodyPermission {
            rotate.switch()
            softClose()
        }
        assert()
    }

    @Test override fun allow() {
        TODO("Not yet implemented")
    }

    @Test override fun deny() {
        TODO("Not yet implemented")
    }
}