package sgtmelon.scriptum.test.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.item.NoteItem
import sgtmelon.scriptum.ui.screen.main.NotesScreen

/**
 * Test for [NoteItem] inside [NotesScreen]
 */
@RunWith(AndroidJUnit4::class)
class NotesContent : ParentUiTest() {

    @Test fun colorTextLight() = startColorTest(NoteType.TEXT, Theme.LIGHT)

    @Test fun colorTextDark() = startColorTest(NoteType.TEXT, Theme.DARK)

    @Test fun colorRollLight() = startColorTest(NoteType.ROLL, Theme.LIGHT)

    @Test fun colorRollDark() = startColorTest(NoteType.ROLL, Theme.DARK)

    private fun startColorTest(type: NoteType, @Theme theme: Int) {
        iPreferenceRepo.theme = theme

        onAssertList(ArrayList<NoteModel>().also { list ->
            Color.list.forEach {
                list.add(when (type) {
                    NoteType.TEXT -> with(data) { insertText(textNote.copy(color = it)) }
                    NoteType.ROLL -> with(data) { insertRoll(rollNote.copy(color = it)) }
                })
            }
        })
    }


    private fun onAssertList(list: List<NoteModel>) {
        launch {
            mainScreen {
                openNotesPage { list.forEachIndexed { p, model -> onAssertItem(p, model) } }
            }
        }
    }

}