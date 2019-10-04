package sgtmelon.scriptum.test.content.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
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

    @Test fun colorTextLight() = startColorTest(NoteType.TEXT, Theme.LIGHT)

    @Test fun colorTextDark() = startColorTest(NoteType.TEXT, Theme.DARK)

    @Test fun colorRollLight() = startColorTest(NoteType.ROLL, Theme.LIGHT)

    @Test fun colorRollDark() = startColorTest(NoteType.ROLL, Theme.DARK)

    // TODO date content

}