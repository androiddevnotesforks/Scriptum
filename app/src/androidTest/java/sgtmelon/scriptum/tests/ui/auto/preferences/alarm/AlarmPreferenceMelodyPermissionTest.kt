package sgtmelon.scriptum.tests.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import kotlin.random.Random


/**
 * Test for [AlarmPreferenceFragment] and [MelodyPermissionDialogUi]. TODO!!!
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceMelodyPermissionTest : ParentUiTest()
 /*   DialogCloseCase,
    DialogWorkCase */{

    @Before override fun setUp() {
        super.setUp()

        TODO("Check permission dialog work")

        preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
    }

    @Test fun todo() {
        TODO("Not yet implemented")
    }
}