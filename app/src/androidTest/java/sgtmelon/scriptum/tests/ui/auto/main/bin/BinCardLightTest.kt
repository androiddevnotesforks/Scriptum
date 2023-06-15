package sgtmelon.scriptum.tests.ui.auto.main.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.ui.item.NoteItemUi
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.source.ui.screen.main.BinScreen
import sgtmelon.scriptum.source.cases.note.NoteCardTestCase

/**
 * Test for [NoteItemUi] inside [BinScreen].
 */
@RunWith(AndroidJUnit4::class)
class BinCardLightTest : NoteCardTestCase(ThemeDisplayed.LIGHT, MainPage.BIN) {

    @Test override fun colorText() = super.colorText()

    @Test override fun colorRoll() = super.colorRoll()


    @Test override fun timeCreateText() = super.timeCreateText()

    @Test override fun timeCreateRoll() = super.timeCreateRoll()

    @Test override fun timeChangeText() = super.timeChangeText()

    @Test override fun timeChangeRoll() = super.timeChangeRoll()


    @Test override fun rollRow1() = super.rollRow1()

    @Test override fun rollRow2() = super.rollRow2()

    @Test override fun rollRow3() = super.rollRow3()

    @Test override fun rollRow4() = super.rollRow4()


    @Test override fun rollNoneDoneVisible() = super.rollNoneDoneVisible()

    @Test override fun rollNoneDoneInvisible() = super.rollNoneDoneInvisible()

    @Test override fun rollPartDoneVisible() = super.rollPartDoneVisible()

    @Test override fun rollPartDoneInvisible() = super.rollPartDoneInvisible()

    @Test override fun rollAllDoneVisible() = super.rollAllDoneVisible()

    @Test override fun rollAllDoneInvisible() = super.rollAllDoneInvisible()


    @Test override fun progressIndicator1() = super.progressIndicator1()

    @Test override fun progressIndicator2() = super.progressIndicator2()

    @Test override fun progressIndicator3() = super.progressIndicator3()

    @Test override fun progressIndicator4() = super.progressIndicator4()


    @Test override fun rankText() = super.rankText()

    @Test override fun rankRoll() = super.rankRoll()

    @Test override fun rankTextCancel() = super.rankTextCancel()

    @Test override fun rankRollCancel() = super.rankRollCancel()

}