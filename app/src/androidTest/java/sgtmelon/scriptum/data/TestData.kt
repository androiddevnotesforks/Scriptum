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


    fun createText() = NoteModel.getCreate(
            context.getTime(), iPreferenceRepo.defaultColor, NoteType.TEXT
    )

    fun createRoll() = NoteModel.getCreate(
            context.getTime(), iPreferenceRepo.defaultColor, NoteType.ROLL
    )


    fun insertRank(rank: RankEntity = rankEntity): RankEntity {
        openRoom().apply { rank.id = getRankDao().insert(rank) }.close()

        return rank
    }

    fun insertText(note: NoteEntity = textNote): NoteModel {
        openRoom().apply { note.id = getNoteDao().insert(note) }.close()

        return NoteModel(note)
    }

    fun insertTextToBin(note: NoteEntity = textNote) = insertText(note.apply { isBin = true })

    fun insertRoll(note: NoteEntity = rollNote, list: ArrayList<RollEntity> = rollList): NoteModel {
        openRoom().apply {
            note.id = getNoteDao().insert(note)
            list.forEach {
                it.noteId = note.id
                getRollDao().insert(it)
            }
        }.close()

        return NoteModel(note, list)
    }

    fun insertRollToBin(note: NoteEntity = rollNote, list: ArrayList<RollEntity> = rollList) =
            insertRoll(note.apply { isBin = true }, list)

    fun insertNotification(noteModel: NoteModel): NoteModel {
        openRoom().apply {
            getAlarmDao().insert(AlarmEntity(noteId = noteModel.noteEntity.id, date = context.getTime()))
        }.close()

        return noteModel
    }


    fun fillRank(count: Int = 10) = ArrayList<RankEntity>().apply {
        (0 until count).forEach {
            add(insertRank(rankEntity.apply {
                name = "$it | $name"
                position = it
                isVisible = Random.nextBoolean()
            }))
        }
    }

    fun fillRankForNotes() = ArrayList<RankEntity>().apply {
        val noteModel = if (Random.nextBoolean()) insertText() else insertRoll()

        (0 until 2).forEach {
            add(insertRank(rankEntity.apply {
                name = "$it | $name"
                noteId.add(noteModel.noteEntity.id)
                position = it
            }))
        }

        forEach {
            noteModel.noteEntity.rankId.add(it.id)
            noteModel.noteEntity.rankPs.add(it.position.toLong())
        }

        openRoom().apply { getNoteDao().update(noteModel.noteEntity) }.close()
    }

    fun fillRankForBin() = ArrayList<RankEntity>().apply {
        val noteModel = if (Random.nextBoolean()) insertTextToBin() else insertRollToBin()

        (0 until 2).forEach {
            add(insertRank(rankEntity.apply {
                name = "$it | $name"
                noteId.add(noteModel.noteEntity.id)
                position = it
            }))
        }

        forEach {
            noteModel.noteEntity.rankId.add(it.id)
            noteModel.noteEntity.rankPs.add(it.position.toLong())
        }

        openRoom().apply { getNoteDao().update(noteModel.noteEntity) }.close()
    }

    fun fillNotes(count: Int = 10) = repeat(count) {
        if (Random.nextBoolean()) insertText() else insertRoll()
    }

    fun fillBin(count: Int = 10) = repeat(count) {
        if (Random.nextBoolean()) insertTextToBin() else insertRollToBin()
    }

    fun fillNotification(count: Int = 10) = repeat(count) {
        insertNotification(if (Random.nextBoolean()) insertText() else insertRoll())
    }


    fun clear() = apply { openRoom().apply { clearAllTables() }.close() }

}