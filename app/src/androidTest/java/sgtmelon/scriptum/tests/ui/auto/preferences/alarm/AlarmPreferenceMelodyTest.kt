package sgtmelon.scriptum.tests.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogWorkCase
import sgtmelon.scriptum.source.ui.screen.dialogs.select.MelodyDialogUi
import sgtmelon.scriptum.source.ui.screen.preference.alarm.AlarmPreferenceLogic
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchAlarmPreference
import sgtmelon.test.common.getDifferentValues
import kotlin.random.Random

/**
 * Test for [AlarmPreferenceFragment] and [MelodyDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceMelodyTest : ParentUiTest(),
    DialogCloseCase,
    DialogWorkCase {

    @Before override fun setUp() {
        super.setUp()

        TODO("Grant permission for simple check")

        preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
    }

    @Test override fun close() = launchAlarmPreference {
        openMelodyDialog { softClose() }
        assert()
        openMelodyDialog { cancel() }
        assert()
    }

    @Test override fun work() {
        // TODO inject getMelodyUseCase
        val list = runBlocking { AlarmPreferenceLogic().getMelodyList() }

        val (setValue, initValue) = list.getDifferentValues()
        val initIndex = list.indexOf(initValue)
        val setIndex = list.indexOf(setValue)

        launchAlarmPreference({ preferences.melodyUri = initValue.uri }) {
            openMelodyDialog {
                click(setIndex)
                click(initIndex)
                click(setIndex)
                apply()
            }
            assert()
        }

        assertEquals(setValue.uri, preferences.melodyUri)
    }
}