package sgtmelon.scriptum.test.auto.preference.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.ISavePeriodTest
import sgtmelon.scriptum.ui.dialog.preference.SavePeriodDialogUi
import sgtmelon.scriptum.ui.screen.preference.NotePreferenceScreen

/**
 * Test for [NotePreferenceFragment] and [SavePeriodDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceSavePeriodTest : ParentUiTest(), ISavePeriodTest {

    private fun runTest(before: () -> Unit = {}, func: NotePreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openNote(func) } } }
        }
    }

    @Test fun dialogClose() = runTest({ preferenceRepo.autoSaveOn = true }) {
        openSavePeriodDialog { onClickCancel() }
        assert()
        openSavePeriodDialog { onCloseSoft() }
        assert()
    }

    @Test override fun savePeriodMin1() = super.savePeriodMin1()

    @Test override fun savePeriodMin3() = super.savePeriodMin3()

    @Test override fun savePeriodMin7() = super.savePeriodMin7()

    override fun startText(@SavePeriod savePeriod: Int) = runTest({
        preferenceRepo.autoSaveOn = true
        switchValue(savePeriod)
    }) {
        openSavePeriodDialog { onClickItem(savePeriod).onClickApply() }
        assert()
    }

    /**
     * Switch [SavePeriod] to another one.
     */
    private fun switchValue(@SavePeriod savePeriod: Int) {
        val list = SavePeriod.list

        while (preferenceRepo.savePeriod == savePeriod) {
            preferenceRepo.savePeriod = list.random()
        }
    }
}