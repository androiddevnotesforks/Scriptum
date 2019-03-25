package sgtmelon.scriptum.data

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestUtils.random
import sgtmelon.scriptum.app.model.data.ColorData
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.office.utils.HelpUtils.Note.getCheck
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import java.util.UUID.randomUUID

class TestData(private val context: Context) {

    // TODO testRoomDao

    private val dataBase: RoomDb get() = RoomDb.getInstance(context)

    private val textNote: NoteItem
        get() = NoteItem().apply {
            create = context.getTime()
            change = context.getTime()
            name = context.getString(R.string.test_note_name)
            text = context.getString(R.string.test_note_text)
            color = (0 until ColorData.size).random()
            type = NoteType.TEXT
        }

    private val rollNote: NoteItem
        get() = NoteItem().apply {
            create = context.getTime()
            change = context.getTime()
            name = context.getString(R.string.test_note_name)
            setRollText(listRoll.getCheck(), listRoll.size)
            color = (0 until ColorData.size).random()
            type = NoteType.ROLL
        }

    private val listRoll = object : ArrayList<RollItem>() {
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

    private val rankItem: RankItem
        get() = RankItem().apply { name = randomUUID().toString() }

    fun clearAllData() = dataBase.apply { clearAllTables() }.close()

    fun insertText(noteItem: NoteItem = textNote): NoteItem {
        dataBase.apply {
            noteItem.id = getNoteDao().insert(noteItem)
        }.close()

        return noteItem
    }

    fun insertRoll(noteItem: NoteItem = rollNote, listRoll: List<RollItem> = this.listRoll)
            : NoteItem {
        dataBase.apply {
            noteItem.id = getNoteDao().insert(noteItem)
            listRoll.forEach {
                it.noteId = noteItem.id
                getRollDao().insert(it)
            }
        }.close()

        return noteItem
    }

    fun insertTextToBin(noteItem: NoteItem = textNote): NoteItem {
        noteItem.isBin = true
        return insertText(noteItem)
    }

    fun insertRollToBin(noteItem: NoteItem = rollNote, listRoll: List<RollItem> = this.listRoll)
            : NoteItem {
        noteItem.isBin = true
        return insertRoll(noteItem, listRoll)
    }

    fun fillNotes(times: Int = 10) = repeat(times) {
        if (Math.random() < 0.5) insertText() else insertRoll()
    }

    fun fillBin(times: Int = 10) = repeat(times) {
        if (Math.random() < 0.5) insertTextToBin() else insertRollToBin()
    }

}