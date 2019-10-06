package sgtmelon.scriptum.test.content.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.ui.item.NoteItem
import sgtmelon.scriptum.ui.screen.main.BinScreen

/**
 * Test for [NoteItem] inside [BinScreen]
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

}