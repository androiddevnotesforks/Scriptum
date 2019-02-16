package sgtmelon.scriptum.test

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestUtils
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.ColorData
import sgtmelon.scriptum.office.utils.TimeUtils

class TestData(private val context: Context) {

    val textNoteItem: NoteItem
        get() = NoteItem(TimeUtils.getTime(context), "",
                context.getString(R.string.test_note_name),
                context.getString(R.string.test_note_text),
                TestUtils.random(0 until ColorData.size), NoteType.TEXT,
                ArrayList<Long>(), ArrayList<Long>(),
                false, false
        )

}