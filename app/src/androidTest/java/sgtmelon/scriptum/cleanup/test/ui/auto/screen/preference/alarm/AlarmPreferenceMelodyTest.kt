package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.ui.dialog.preference.MelodyDialogUi
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test for [AlarmPreferenceFragment] and [MelodyDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceMelodyTest : ParentUiTest(), IAlarmPreferenceTest {

    @Before override fun setUp() {
        super.setUp()
        preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
    }

    @Test fun dialogClose() = runTest {
        openMelodyDialog { softClose() }
        assert()
        openMelodyDialog { onClickCancel() }
        assert()
    }

    @Test fun dialogWork() {
        val list = runBlocking { getLogic().getMelodyList() }

        val pair = switchValue(list)
        val initIndex = list.indexOf(pair.first)
        val valueIndex = list.indexOf(pair.second)

        assertNotEquals(initIndex, valueIndex)
        assertEquals(pair.first.uri, preferences.melodyUri)

        runTest {
            openMelodyDialog {
                onClickItem(valueIndex)
                    .onClickItem(initIndex)
                    .onClickItem(valueIndex)
                    .onClickApply()
            }
            assert()
        }

        assertEquals(pair.second.uri, preferences.melodyUri)
    }

    /**
     * Switch selected melody to another one.
     */
    private fun switchValue(list: List<MelodyItem>): Pair<MelodyItem, MelodyItem> {
        val value = list.random()
        var initValue: MelodyItem

        do {
            initValue = list.random()
            preferences.melodyUri = initValue.uri
        } while (initValue.uri == value.uri)

        return Pair(initValue, value)
    }
}