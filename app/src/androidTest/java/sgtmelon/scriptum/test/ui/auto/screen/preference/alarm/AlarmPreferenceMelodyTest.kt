package sgtmelon.scriptum.test.ui.auto.screen.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.MelodyItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.dialog.preference.MelodyDialogUi
import kotlin.random.Random

/**
 * Test for [AlarmPreferenceFragment] and [MelodyDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceMelodyTest : ParentUiTest(), IAlarmPreferenceTest {

    override fun setup() {
        super.setup()
        getLogic().alarmInteractor.updateSignal(booleanArrayOf(true, Random.nextBoolean()))
    }

    @Test fun dialogClose() = runTest {
        openMelodyDialog { onCloseSoft() }
        assert()
        openMelodyDialog { onClickCancel() }
        assert()
    }

    @Test fun dialogWork() {
        val list = runBlocking { getLogic().signalInteractor.getMelodyList() }

        val pair = switchValue(list)
        val initIndex = list.indexOf(pair.first)
        val valueIndex = list.indexOf(pair.second)

        assertNotEquals(initIndex, valueIndex)
        assertEquals(pair.first.uri, preferenceRepo.melodyUri)

        runTest {
            openMelodyDialog {
                onClickItem(valueIndex)
                    .onClickItem(initIndex)
                    .onClickItem(valueIndex)
                    .onClickApply()
            }
            assert()
        }

        assertEquals(pair.second.uri, preferenceRepo.melodyUri)
    }

    /**
     * Switch selected melody to another one.
     */
    private fun switchValue(list: List<MelodyItem>): Pair<MelodyItem, MelodyItem> {
        val value = list.random()
        var initValue: MelodyItem

        do {
            initValue = list.random()
            preferenceRepo.melodyUri = initValue.uri
        } while (preferenceRepo.melodyUri == value.uri)

        return Pair(initValue, value)
    }
}