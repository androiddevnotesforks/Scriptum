package sgtmelon.scriptum.test.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentUiTest

@RunWith(AndroidJUnit4::class)
class NotesContent : ParentUiTest() {

    @Test fun testTest() = data.insertText(data.textNote.copy()).let {
        launch { mainScreen { openNotesPage { onAssertItem(it) } } }
    }

}