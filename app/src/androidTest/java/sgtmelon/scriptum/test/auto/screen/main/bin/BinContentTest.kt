package sgtmelon.scriptum.test.auto.screen.main.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.key.MainPage
import sgtmelon.scriptum.test.auto.screen.main.ParentNoteContentTest
import sgtmelon.scriptum.ui.item.NoteItemUi
import sgtmelon.scriptum.ui.screen.main.BinScreen

/**
 * Test for [NoteItemUi] inside [BinScreen]
 */
@RunWith(AndroidJUnit4::class)
class BinContentTest : ParentNoteContentTest(MainPage.BIN) {

    @Test override fun colorTextLight() = super.colorTextLight()

    @Test override fun colorTextDark() = super.colorTextDark()

    @Test override fun colorRollLight() = super.colorRollLight()

    @Test override fun colorRollDark() = super.colorRollDark()


    @Test override fun timeCreateText() = super.timeCreateText()

    @Test override fun timeCreateRoll() = super.timeCreateRoll()

    @Test override fun timeChangeText() = super.timeChangeText()

    @Test override fun timeChangeRoll() = super.timeChangeRoll()


    @Test override fun rollRow1() = super.rollRow1()

    @Test override fun rollRow2() = super.rollRow2()

    @Test override fun rollRow3() = super.rollRow3()

    @Test override fun rollRow4() = super.rollRow4()


    @Test override fun progressIndicator1() = super.progressIndicator1()

    @Test override fun progressIndicator2() = super.progressIndicator2()

    @Test override fun progressIndicator3() = super.progressIndicator3()

    @Test override fun progressIndicator4() = super.progressIndicator4()


    @Test override fun rankTextLight() = super.rankTextLight()

    @Test override fun rankTextDark() = super.rankTextDark()

    @Test override fun rankRollLight() = super.rankRollLight()

    @Test override fun rankRollDark() = super.rankRollDark()

    @Test override fun rankTextCancel() = super.rankTextCancel()

    @Test override fun rankRollCancel() = super.rankRollCancel()

}