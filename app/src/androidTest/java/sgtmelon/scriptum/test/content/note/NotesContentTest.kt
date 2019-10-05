package sgtmelon.scriptum.test.content.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.ui.item.NoteItem
import sgtmelon.scriptum.ui.screen.main.NotesScreen

/**
 * Test for [NoteItem] inside [NotesScreen]
 */
@RunWith(AndroidJUnit4::class)
class NotesContentTest : ParentNoteContentTest(MainPage.NOTES) {

    @Test override fun colorTextLight() = super.colorTextLight()

    @Test override fun colorTextDark() = super.colorTextDark()

    @Test override fun colorRollLight() = super.colorRollLight()

    @Test override fun colorRollDark() = super.colorRollDark()


    @Test override fun timeText() = super.timeText()

    @Test override fun timeRoll() = super.timeRoll()

}