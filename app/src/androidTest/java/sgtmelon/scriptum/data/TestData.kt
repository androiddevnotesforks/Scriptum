package sgtmelon.scriptum.data

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getCheck
import sgtmelon.scriptum.extension.getTime
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.room.entity.RollEntity
import java.util.UUID.randomUUID
import kotlin.random.Random

class TestData(private val context: Context) {

    // TODO testRoomDao

    private val iPreferenceRepo = PreferenceRepo(context)

    private fun openRoom() = RoomDb.getInstance(context)

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

    val rollList = object : ArrayList<RollEntity>() {
        init {
            for (i in 0 until 10) {
                add(RollEntity().apply {
                    position = i
                    isCheck = Random.nextBoolean()
                    text = i.toString() + " | " + context.getString(R.string.test_roll_text)
                })
            }
        }
    }

    val rankEntity: RankEntity get() = RankEntity(name = uniqueString)


    fun clear() = apply { openRoom().apply { clearAllTables() }.close() }

    fun insertRank(rankEntity: RankEntity = this.rankEntity): RankEntity {
        openRoom().apply { rankEntity.id = getRankDao().insert(rankEntity) }.close()

        return rankEntity
    }

    fun insertText(noteEntity: NoteEntity = textNote): NoteEntity {
        openRoom().apply { noteEntity.id = getNoteDao().insert(noteEntity) }.close()

        return noteEntity
    }

    fun insertRoll(noteEntity: NoteEntity = rollNote, rollList: List<RollEntity> = this.rollList)
            : NoteEntity {
        openRoom().apply {
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

    fun insertRollToBin(noteEntity: NoteEntity = rollNote, rollList: List<RollEntity> = this.rollList)
            : NoteEntity {
        noteEntity.isBin = true
        return insertRoll(noteEntity, rollList)
    }

    fun insertRankToNotes() = ArrayList<RankEntity>().apply {
        val noteEntity = if (Random.nextBoolean()) insertText() else insertRoll()

        (1..2).forEach {
            val rankEntity = insertRank(rankEntity.apply {
                name = "$it | $name"
                noteId.add(noteEntity.id)
                position = it
            })

            add(rankEntity)
        }

        forEach {
            noteEntity.rankId.add(it.id)
            noteEntity.rankPs.add(it.position.toLong())
        }

        openRoom().apply { getNoteDao().update(noteEntity) }.close()
    }

    fun insertRankToBin() = ArrayList<RankEntity>().apply {
        val noteEntity = if (Random.nextBoolean()) insertTextToBin() else insertRollToBin()

        (1..2).forEach {
            val rankEntity = insertRank(rankEntity.apply {
                name = "$it | $name"
                noteId.add(noteEntity.id)
                position = it
            })

            add(rankEntity)
        }

        forEach {
            noteEntity.rankId.add(it.id)
            noteEntity.rankPs.add(it.position.toLong())
        }

        openRoom().apply { getNoteDao().update(noteEntity) }.close()
    }

    fun insertNotification(noteEntity: NoteEntity) {
        openRoom().apply {
            getAlarmDao().insert(AlarmEntity(noteId = noteEntity.id, date = context.getTime()))
        }.close()
    }

    fun fillRank(count: Int = 20) = ArrayList<RankEntity>().apply {
        (0 until count).forEach {
            val rankEntity = insertRank(rankEntity.apply {
                name = "$it | $name"
                position = it
                isVisible = Random.nextBoolean()
            })

            add(rankEntity)
        }
    }

    fun fillNotes() = repeat(times = 20) {
        if (Random.nextBoolean()) insertText() else insertRoll()
    }

    fun fillBin() = repeat(times = 20) {
        if (Random.nextBoolean()) insertTextToBin() else insertRollToBin()
    }

    fun fillNotification() = repeat(times = 20) {
        val noteEntity = if (Random.nextBoolean()) insertText() else insertRoll()
        insertNotification(noteEntity)
    }


    // TODO !!!!NEW!!!!

    fun insertTextNote(note: NoteEntity = textNote): NoteModel {
        openRoom().apply { note.id = getNoteDao().insert(note) }.close()

        return NoteModel(note)
    }

    fun insertRollNote(note: NoteEntity = rollNote, list: ArrayList<RollEntity> = rollList): NoteModel {
        openRoom().apply {
            note.id = getNoteDao().insert(note)
            list.forEach {
                it.noteId = note.id
                getRollDao().insert(it)
            }
        }.close()

        return NoteModel(note, list)
    }

    fun insertTextNoteToBin(note: NoteEntity = textNote) =
            insertTextNote(note.apply { isBin = true })

    fun insertRollNoteToBin(note: NoteEntity = rollNote, list: ArrayList<RollEntity> = rollList) =
            insertRollNote(note.apply { isBin = true }, list)

    fun createTextNote() = NoteModel.getCreate(
            context.getTime(), iPreferenceRepo.defaultColor, NoteType.TEXT
    )

    fun createRollNote() = NoteModel.getCreate(
            context.getTime(), iPreferenceRepo.defaultColor, NoteType.ROLL
    )


}