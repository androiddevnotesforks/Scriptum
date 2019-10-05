package sgtmelon.scriptum.test.content.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.extension.getTime
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.ui.item.NoteItem
import sgtmelon.scriptum.ui.screen.main.BinScreen

/**
 * Test for [NoteItem] inside [BinScreen]
 */
@RunWith(AndroidJUnit4::class)
class BinContentTest : ParentNoteContentTest(MainPage.BIN) {

    @Test override fun colorTextLight() = startColorTest(NoteType.TEXT, Theme.LIGHT)

    @Test override fun colorTextDark() = startColorTest(NoteType.TEXT, Theme.DARK)

    @Test override fun colorRollLight() = startColorTest(NoteType.ROLL, Theme.LIGHT)

    @Test override fun colorRollDark() = startColorTest(NoteType.ROLL, Theme.DARK)

    @Test override fun timeText() = onAssertList(ArrayList<NoteModel>().also { list ->
        lastArray.forEach {
            val time = getTime(it)
            list.add(data.insertTextToBin(data.textNote.copy(create = time, change = time)))
        }
    })

    @Test override fun timeRoll() = onAssertList(ArrayList<NoteModel>().also { list ->
        lastArray.forEach {
            val time = getTime(it)
            list.add(data.insertRollToBin(data.rollNote.copy(create = time, change = time)))
        }
    })

}