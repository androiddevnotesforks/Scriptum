package sgtmelon.scriptum.data

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestUtils.random
import sgtmelon.scriptum.app.model.data.ColorData
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.office.utils.HelpUtils.Note.getCheck
import sgtmelon.scriptum.office.utils.TimeUtils.getTime

class TestData(private val context: Context) {

    // TODO testRoomDao

    private val dataBase: RoomDb get() = RoomDb.provideDb(context)

    val textNote: NoteItem
        get() = NoteItem().apply {
            create = context.getTime()
            change = context.getTime()
            name = context.getString(R.string.test_note_name)
            text = context.getString(R.string.test_note_text)
            color = (0 until ColorData.size).random()
            type = NoteType.TEXT
        }

    val rollNote: NoteItem
        get() = NoteItem().apply {
            create = context.getTime()
            change = context.getTime()
            name = context.getString(R.string.test_note_name)
            setRollText(listRoll.getCheck(), listRoll.size)
            color = (0 until ColorData.size).random()
            type = NoteType.ROLL
        }

    val listRoll = object : ArrayList<RollItem>() {
        init {
            for (i in 0 until 10) {
                add(RollItem().apply {
                    position = i
                    isCheck = Math.random() < 0.5
                    text = i.toString() + " | " + context.getString(R.string.test_roll_text)
                })
            }
        }
    }

    fun clearAllData() = dataBase.apply { clearAllTables() }.close()

    fun insertTextNote(noteItem: NoteItem = textNote): NoteItem {
        dataBase.apply {
            noteItem.id = daoNote().insert(noteItem)
        }.close()

        return noteItem
    }

    fun insertRollNote(noteItem: NoteItem = rollNote,
                       listRoll: List<RollItem> = this.listRoll): NoteItem {
        dataBase.apply {
            noteItem.id = daoNote().insert(noteItem)
            listRoll.forEach {
                it.noteId = noteItem.id
                daoRoll().insert(it)
            }
        }.close()

        return noteItem
    }

}