package sgtmelon.scriptum.tests.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.cases.permissions.MelodyPermissionCase
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import kotlin.random.Random


/**
 * Test for [AlarmPreferenceFragment] and [MelodyPermissionDialogUi]. TODO!!!
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceMelodyPermissionTest : ParentUiTest(),
    MelodyPermissionCase {

    @Before override fun setUp() {
        super.setUp()

        preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())

        throwOnWrongApi()
        assertPermissionNotGranted(context)
    }

    @Test override fun info() {
        TODO("Not yet implemented")
    }

    @Test override fun infoClose() {
        TODO("Not yet implemented")
    }

    @Test override fun infoRotateWork() {
        TODO("Not yet implemented")
    }

    @Test override fun infoRotateClose() {
        TODO("Not yet implemented")
    }

    @Test override fun allow() {
        TODO("Not yet implemented")
    }

    @Test override fun deny() {
        TODO("Not yet implemented")
    }
}