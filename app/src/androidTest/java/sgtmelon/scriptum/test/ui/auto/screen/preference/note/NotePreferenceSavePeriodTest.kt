package sgtmelon.scriptum.test.ui.auto.screen.preference.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.ISavePeriodTest
import sgtmelon.scriptum.ui.dialog.preference.SavePeriodDialogUi

/**
 * Test for [NotePreferenceFragment] and [SavePeriodDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceSavePeriodTest : ParentUiTest(),
    INotePreferenceTest,
    ISavePeriodTest {

    @Test fun dialogClose() = runTest({ preferences.isAutoSaveOn = true }) {
        openSavePeriodDialog { onClickCancel() }
        assert()
        openSavePeriodDialog { onCloseSoft() }
        assert()
    }

    @Test override fun savePeriodMin1() = super.savePeriodMin1()

    @Test override fun savePeriodMin3() = super.savePeriodMin3()

    @Test override fun savePeriodMin7() = super.savePeriodMin7()

    override fun startText(value: SavePeriod) {
        val initValue = switchValue(value)

        assertNotEquals(initValue, value)

        runTest({ preferences.isAutoSaveOn = true }) {
            openSavePeriodDialog { onClickItem(value).onClickApply() }
            assert()
        }

        assertEquals(value, preferences.savePeriod)
    }

    /**
     * Switch [SavePeriod] to another one.
     */
    private fun switchValue(value: SavePeriod): SavePeriod {
        val list = SavePeriod.values()
        var initValue: SavePeriod

        do {
            initValue = list.random()
            preferencesRepo.savePeriod = initValue
        } while (initValue == value)

        return initValue
    }
}