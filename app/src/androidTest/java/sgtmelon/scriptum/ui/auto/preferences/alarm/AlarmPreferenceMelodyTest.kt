package sgtmelon.scriptum.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.MelodyDialogUi
import sgtmelon.scriptum.parent.ui.screen.preference.alarm.AlarmPreferenceLogic
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.ui.cases.dialog.DialogWorkCase
import sgtmelon.test.common.getDifferentValues

/**
 * Test for [AlarmPreferenceFragment] and [MelodyDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceMelodyTest : ParentUiTest(),
    DialogCloseCase,
    DialogWorkCase {

    @Before override fun setUp() {
        super.setUp()
        preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
    }

    @Test override fun close() = startAlarmPreferenceTest {
        openMelodyDialog { softClose() }
        assert()
        openMelodyDialog { cancel() }
        assert()
    }

    @Test override fun work() {
        //        TODO("Return mockked list")
        // TODO inject getMelodyUseCase
        val list = runBlocking { AlarmPreferenceLogic().getMelodyList() }

        val (setValue, initValue) = list.getDifferentValues()
        val initIndex = list.indexOf(initValue)
        val setIndex = list.indexOf(setValue)

        startAlarmPreferenceTest({ preferences.melodyUri = initValue.uri }) {
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