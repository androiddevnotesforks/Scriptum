package sgtmelon.scriptum.data

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestUtils.random
import sgtmelon.scriptum.extension.getCheck
import sgtmelon.scriptum.extension.getTime
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankItem
import sgtmelon.scriptum.room.entity.RollItem
import java.util.UUID.randomUUID
import kotlin.random.Random

class TestData(private val context: Context) {

    // TODO testRoomDao

    private val dataBase: RoomDb get() = RoomDb.getInstance(context)

    val uniqueString get() = randomUUID().toString().substring(0, 16)

    val textNote: NoteEntity
        get() = NoteEntity().apply {
            create = context.getTime()
            change = context.getTime()
            name = context.getString(R.string.test_note_name)
            text = context.getString(R.string.test_note_text)
            color = (0 until ColorData.size).random()
            type = NoteType.TEXT
        }

    val rollNote: NoteEntity
        get() = NoteEntity().apply {
            create = context.getTime()
            change = context.getTime()
            name = context.getString(R.string.test_note_name)
            setCompleteText(rollList.getCheck(), rollList.size)
            color = (0 until ColorData.size).random()
            type = NoteType.ROLL
        }

    val rollList = object : ArrayList<RollItem>() {
        init {
            for (i in 0 until 10) {
                add(RollItem().apply {
                    position = i
                    isCheck = Random.nextBoolean()
                    text = i.toString() + " | " + context.getString(R.string.test_roll_text)
                })
            }
        }
    }

    val rankItem: RankItem get() = RankItem(name = uniqueString)


    fun clear() = apply { dataBase.apply { clearAllTables() }.close() }

    fun insertRank(rankItem: RankItem = this.rankItem): RankItem {
        dataBase.apply { rankItem.id = getRankDao().insert(rankItem) }.close()

        return rankItem
    }

    fun insertText(noteEntity: NoteEntity = textNote): NoteEntity {
        dataBase.apply { noteEntity.id = getNoteDao().insert(noteEntity) }.close()

        return noteEntity
    }

    fun insertRoll(noteEntity: NoteEntity = rollNote, rollList: List<RollItem> = this.rollList)
            : NoteEntity {
        dataBase.apply {
            noteEntity.id = getNoteDao().insert(noteEntity)
            rollList.forEach {
                it.noteId = noteEntity.id
                getRollDao().insert(it)
            }
        }.close()

        return noteEntity
    }


    fun insertTextToBin(noteEntity: NoteEntity = textNote): NoteEntity {
        noteEntity.isBin = true
        return insertText(noteEntity)
    }

    fun insertRollToBin(noteEntity: NoteEntity = rollNote, rollList: List<RollItem> = this.rollList)
            : NoteEntity {
        noteEntity.isBin = true
        return insertRoll(noteEntity, rollList)
    }

    fun insertRankToNotes() = ArrayList<RankItem>().apply {
        val noteItem = if (Random.nextBoolean()) insertText() else insertRoll()

        (1..2).forEach {
            val rankItem = insertRank(rankItem.apply {
                name = "$it | $name"
                noteId.add(noteItem.id)
                position = it
            })

            add(rankItem)
        }

        forEach {
            noteItem.rankId.add(it.id)
            noteItem.rankPs.add(it.position.toLong())
        }

        dataBase.apply { getNoteDao().update(noteItem) }.close()
    }

    fun insertRankToBin() = ArrayList<RankItem>().apply {
        val noteItem = if (Random.nextBoolean()) insertTextToBin() else insertRollToBin()

        (1..2).forEach {
            val rankItem = insertRank(rankItem.apply {
                name = "$it | $name"
                noteId.add(noteItem.id)
                position = it
            })

            add(rankItem)
        }

        forEach {
            noteItem.rankId.add(it.id)
            noteItem.rankPs.add(it.position.toLong())
        }

        dataBase.apply { getNoteDao().update(noteItem) }.close()
    }

    fun insertNotification(noteEntity: NoteEntity) {
        dataBase.apply {
            getAlarmDao().insert(AlarmEntity(noteId = noteEntity.id, date = context.getTime()))
        }.close()
    }

    fun fillRank(times: Int = 10) = ArrayList<RankItem>().apply {
        (1..times).forEach {
            val rankItem = insertRank(rankItem.apply {
                name = "$it | $name"
                position = it
                isVisible = Random.nextBoolean()
            })

            add(rankItem)
        }
    }

    fun fillNotes(times: Int = 10) = repeat(times) {
        if (Random.nextBoolean()) insertText() else insertRoll()
    }

    fun fillBin(times: Int = 10) = repeat(times) {
        if (Random.nextBoolean()) insertTextToBin() else insertRollToBin()
    }

    fun fillNotification(times: Int = 10) = repeat(times) {
        val noteItem = if (Random.nextBoolean()) insertText() else insertRoll()
        insertNotification(noteItem)
    }

}