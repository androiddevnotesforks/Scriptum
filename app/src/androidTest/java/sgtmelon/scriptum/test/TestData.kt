package sgtmelon.scriptum.test

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestUtils
import sgtmelon.scriptum.app.model.data.ColorData
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.office.utils.TimeUtils.getTime

class TestData(private val context: Context) {

    val textNoteItem: NoteItem
        get() = NoteItem().apply {
            create = context.getTime()
            name = context.getString(R.string.test_note_name)
            text = context.getString(R.string.test_note_text)
            color = TestUtils.random(0 until ColorData.size)
            type = NoteType.TEXT
        }

}