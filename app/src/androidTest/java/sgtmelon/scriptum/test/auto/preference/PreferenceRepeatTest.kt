package sgtmelon.scriptum.test.auto.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [PreferenceActivity], [PreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class PreferenceRepeatTest : ParentUiTest() {

    // TODO fix all

    @Test fun dialogClose() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                openPreference {
                    openRepeatDialog { onCloseSoft() }.assert()
                    openRepeatDialog { onClickCancel() }.assert()
                }
            }
        }
    }


    @Test fun selectRepeatMin10() = startSelectRepeat(Repeat.MIN_10)

    @Test fun selectRepeatMin30() = startSelectRepeat(Repeat.MIN_30)

    @Test fun selectRepeatMin60() = startSelectRepeat(Repeat.MIN_60)

    @Test fun selectRepeatMin180() = startSelectRepeat(Repeat.MIN_180)

    @Test fun selectRepeatMin1440() = startSelectRepeat(Repeat.MIN_1440)

    private fun startSelectRepeat(@Repeat repeat: Int) = launch(before = {checkRepeat(repeat)}) {
        mainScreen {
            notesScreen(isEmpty = true) {
                openPreference { openRepeatDialog { onClickItem(repeat).onClickApply() } }
            }
        }
    }

    private fun checkRepeat(@Repeat repeat: Int) {
        val list = listOf(
                Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60, Repeat.MIN_180, Repeat.MIN_1440
        )

        while (preferenceRepo.repeat == repeat) {
            preferenceRepo.repeat = list.random()
        }
    }

}