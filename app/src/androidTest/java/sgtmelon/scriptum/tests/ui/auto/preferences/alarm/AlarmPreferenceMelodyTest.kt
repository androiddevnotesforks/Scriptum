package sgtmelon.scriptum.tests.ui.auto.preferences.alarm

import android.Manifest
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.cases.dialog.DialogWorkCase
import sgtmelon.scriptum.source.ui.screen.dialogs.select.MelodyDialogUi
import sgtmelon.scriptum.source.ui.screen.preference.alarm.AlarmPreferenceLogic
import sgtmelon.scriptum.source.ui.screen.preference.alarm.AlarmPreferenceScreen
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchAlarmPreference
import sgtmelon.test.common.getDifferentValues
import kotlin.random.Random

/**
 * Test for [AlarmPreferenceFragment] and [MelodyDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceMelodyTest : ParentUiRotationTest(),
    DialogCloseCase,
    DialogWorkCase,
    DialogRotateCase {

    // TODO inject getMelodyUseCase in [work] and [rotateWork].

    @get:Rule val permissionRule = getPermissionRule(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @Before override fun setUp() {
        super.setUp()

        preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
    }

    @Test override fun close() = launchAlarmPreference {
        openMelodyDialog { softClose() }
        assert()
        openMelodyDialog { cancel() }
        assert()
    }

    @Test override fun work() {
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

    @Test override fun rotateClose() = launchAlarmPreference {
        assertRotationClose { softClose() }
        assert()
        assertRotationClose { cancel() }
        assert()
    }

    /** Allow to [closeDialog] in different ways. */
    private fun AlarmPreferenceScreen.assertRotationClose(closeDialog: MelodyDialogUi.() -> Unit) {
        openMelodyDialog {
            rotate.switch()
            assert()
            closeDialog(this)
        }
        assert()
    }

    @Test override fun rotateWork() {
        val list = runBlocking { AlarmPreferenceLogic().getMelodyList() }

        val (setValue, initValue) = list.getDifferentValues()
        val initIndex = list.indexOf(initValue)
        val setIndex = list.indexOf(setValue)

        launchAlarmPreference({ preferences.melodyUri = initValue.uri }) {
            openMelodyDialog {
                assertRotationClick(setIndex)
                assertRotationClick(initIndex)
                assertRotationClick(setIndex)
                apply()
            }
            assert()
        }

        assertEquals(setValue.uri, preferences.melodyUri)
    }

    /** Allow to click different [index] and rotate+check after that. */
    private fun MelodyDialogUi.assertRotationClick(index: Int) {
        click(index)
        rotate.switch()
        assert()
    }
}