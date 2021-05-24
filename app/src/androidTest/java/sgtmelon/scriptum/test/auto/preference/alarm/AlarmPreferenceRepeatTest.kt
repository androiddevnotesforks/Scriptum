package sgtmelon.scriptum.test.auto.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IRepeatTest
import sgtmelon.scriptum.ui.dialog.preference.RepeatDialogUi

/**
 * Test for [AlarmPreferenceFragment] and [RepeatDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceRepeatTest : ParentUiTest(), IRepeatTest {

    // TODO fix all

    @Test fun dialogClose() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                openPreference {
                    TODO()
                    //                    openRepeatDialog { onCloseSoft() }.assert()
                    //                    assert()
                    //                    openRepeatDialog { onClickCancel() }.assert()
                    //                    assert()
                }
            }
        }
    }

    @Test override fun repeatMin10() = super.repeatMin10()

    @Test override fun repeatMin30() = super.repeatMin30()

    @Test override fun repeatMin60() = super.repeatMin60()

    @Test override fun repeatMin180() = super.repeatMin180()

    @Test override fun repeatMin1440() = super.repeatMin1440()

    override fun startTest(@Repeat repeat: Int) = launch({ switchValue(repeat) }) {
        mainScreen {
            notesScreen(isEmpty = true) {
                openPreference {
                    TODO()
                    //                    openRepeatDialog { onClickItem(repeat).onClickApply() }
                }
            }
        }
    }

    /**
     * Switch [Repeat] to another one.
     */
    private fun switchValue(@Repeat repeat: Int) {
        val list = Repeat.list

        while (preferenceRepo.repeat == repeat) {
            preferenceRepo.repeat = list.random()
        }
    }

}