package sgtmelon.scriptum.tests.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.infrastructure.utils.extensions.isPermissionGranted
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.cases.dialog.DialogWorkCase
import sgtmelon.scriptum.source.permission.GrantWriteExternalPermission
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
    GrantWriteExternalPermission,
    DialogCloseCase,
    DialogWorkCase,
    DialogRotateCase {

    // TODO inject getMelodyUseCase in [work] and [rotateWork].

    @Before override fun setUp() {
        super.setUp()

        assertTrue(context.isPermissionGranted(Permission.WriteExternalStorage))
        preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
    }

    @Test override fun close() = launchAlarmPreference {
        openMelodyDialog { softClose() }
        assert()
        openMelodyDialog { cancel() }
        assert()
    }

    @Test override fun work() = runWorkTest { click(it) }

    @Test override fun rotateClose() = launchAlarmPreference {
        assertRotationClose { softClose() }
        assertRotationClose { cancel() }
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

    @Test override fun rotateWork() = runWorkTest { assertRotationClick(it) }

    /** Allow to click different [value] and rotate+check after that. */
    private fun MelodyDialogUi.assertRotationClick(value: Int) {
        click(value)
        rotate.switch()
        assert()
    }

    /** Allow to run work test with different [action]. */
    private fun runWorkTest(action: MelodyDialogUi.(value: Int) -> Unit) {
        val list = runBlocking { AlarmPreferenceLogic().getMelodyList() }

        val (setValue, initValue) = list.getDifferentValues()
        val initIndex = list.indexOf(initValue)
        val setIndex = list.indexOf(setValue)

        launchAlarmPreference({ preferences.melodyUri = initValue.uri }) {
            openMelodyDialog {
                action(setIndex)
                action(initIndex)
                action(setIndex)
                apply()
            }
            assert()
        }

        assertEquals(setValue.uri, preferences.melodyUri)
    }
}